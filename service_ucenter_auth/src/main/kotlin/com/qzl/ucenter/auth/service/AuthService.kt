package com.qzl.ucenter.auth.service

import com.alibaba.fastjson.JSON
import com.qzl.common.client.ServiceList
import com.qzl.common.exception.ExceptionCast
import com.qzl.model.ucenter.ext.AuthToken
import com.qzl.model.ucenter.response.AuthCode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.client.ClientHttpResponse
import org.springframework.stereotype.Service
import org.springframework.util.Base64Utils
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.DefaultResponseErrorHandler
import org.springframework.web.client.RestTemplate
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * @author Administrator
 * @version 1.0
 */
@Service
class AuthService {

    @Value("\${auth.tokenValiditySeconds}")
    internal var tokenValiditySeconds: Int = 0
    @Autowired
    lateinit var loadBalancerClient: LoadBalancerClient
    @Autowired
    lateinit var stringRedisTemplate: StringRedisTemplate

    @Autowired
    lateinit var restTemplate: RestTemplate

    //用户认证申请令牌，将令牌存储到redis
    fun login(username: String?, password: String?, clientId: String?, clientSecret: String?): AuthToken? {

        //请求spring security申请令牌
        val authToken = this.applyToken(username, password, clientId, clientSecret)
        if (authToken == null) {
            ExceptionCast.cast(AuthCode.AUTH_LOGIN_APPLYTOKEN_FAIL)
        }
        //用户身份令牌
        val access_token = authToken?.access_token
        //存储到redis中的内容
        val jsonString = JSON.toJSONString(authToken)
        //将令牌存储到redis
        val result = this.saveToken(access_token, jsonString, tokenValiditySeconds.toLong())
        if (!result) {
            ExceptionCast.cast(AuthCode.AUTH_LOGIN_TOKEN_SAVEFAIL)
        }
        return authToken

    }
    //存储到令牌到redis

    /**
     *
     * @param access_token 用户身份令牌
     * @param content  内容就是AuthToken对象的内容
     * @param ttl 过期时间
     * @return
     */
    private fun saveToken(access_token: String?, content: String, ttl: Long): Boolean {
        val key = "user_token:$access_token"
        stringRedisTemplate.boundValueOps(key).set(content, ttl, TimeUnit.SECONDS)
        val expire = stringRedisTemplate.getExpire(key, TimeUnit.SECONDS)
        return expire > 0
    }

    //删除token
    fun delToken(access_token: String): Boolean {
        val key = "user_token:$access_token"
        stringRedisTemplate.delete(key)
        return true
    }

    //从redis查询令牌
    fun getUserToken(token: String): AuthToken? {
        val key = "user_token:$token"
        //从redis中取到令牌信息
        val value = stringRedisTemplate.opsForValue().get(key)
        //转成对象
        try {
            return JSON.parseObject(value, AuthToken::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

    }

    //申请令牌
    private fun applyToken(username: String?, password: String?, clientId: String?, clientSecret: String?): AuthToken? {
        //从eureka中获取认证服务的地址（因为spring security在认证服务中）
        //从eureka中获取认证服务的一个实例的地址
        val serviceInstance = loadBalancerClient.choose(ServiceList.SERVICE_UCENTER_AUTH)
        //此地址就是http://ip:port
        val uri = serviceInstance.uri
        //令牌申请的地址 http://localhost:40400/auth/oauth/token
        val authUrl = "$uri/auth/oauth/token"
        //定义header
        val header = LinkedMultiValueMap<String, String>()
        val httpBasic = getHttpBasic(clientId, clientSecret)
        header.add("Authorization", httpBasic)

        //定义body
        val body = LinkedMultiValueMap<String, String>()
        body.add("grant_type", "password")
        body.add("username", username)
        body.add("password", password)

        val httpEntity = HttpEntity<MultiValueMap<String, String>>(body, header)
        //String url, HttpMethod method, @Nullable HttpEntity<?> requestEntity, Class<T> responseType, Object... uriVariables

        //设置restTemplate远程调用时候，对400和401不让报错，正确返回数据
        restTemplate.errorHandler = object : DefaultResponseErrorHandler() {
            @Throws(IOException::class)
            override fun handleError(response: ClientHttpResponse) {
                if (response.rawStatusCode != 400 && response.rawStatusCode != 401) {
                    super.handleError(response)
                }
            }
        }

        val exchange = restTemplate.exchange(authUrl, HttpMethod.POST, httpEntity, Map::class.java)

        //申请令牌信息
        val bodyMap = exchange.body
        if (bodyMap == null ||
                bodyMap["access_token"] == null ||
                bodyMap["refresh_token"] == null ||
                bodyMap["jti"] == null) {

            //解析spring security返回的错误信息
            if (bodyMap != null && bodyMap["error_description"] != null) {
                val error_description = bodyMap["error_description"] as String
                if (error_description.indexOf("UserDetailsService returned null") >= 0) {
                    ExceptionCast.cast(AuthCode.AUTH_ACCOUNT_NOTEXISTS)
                } else if (error_description.indexOf("坏的凭证") >= 0) {
                    ExceptionCast.cast(AuthCode.AUTH_CREDENTIAL_ERROR)
                }
            }


            return null
        }
        val authToken = AuthToken()
        authToken.access_token = bodyMap["jti"].toString()//用户身份令牌
        authToken.refresh_token = bodyMap["refresh_token"].toString()//刷新令牌
        authToken.jwt_token = bodyMap["access_token"].toString()//jwt令牌
        return authToken
    }


    //获取httpbasic的串
    private fun getHttpBasic(clientId: String?, clientSecret: String?): String {
        val string = "$clientId:$clientSecret"
        //将串进行base64编码
        val encode = Base64Utils.encode(string.toByteArray())
        return "Basic " + String(encode)
    }
}
