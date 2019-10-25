package com.qzl.ucenter.auth.controller

import com.qzl.api.auth.AuthControllerApi
import com.qzl.common.exception.ExceptionCast
import com.qzl.common.model.response.CommonCode
import com.qzl.common.model.response.ResponseResult
import com.qzl.common.web.BaseController
import com.qzl.model.ucenter.request.LoginRequest
import com.qzl.model.ucenter.response.AuthCode
import com.qzl.model.ucenter.response.JwtResult
import com.qzl.model.ucenter.response.LoginResult
import com.qzl.ucenter.auth.service.AuthService
import com.qzl.util.CookieUtil
import org.apache.commons.codec.binary.Base64
import org.apache.commons.lang.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes



/**
 * @author Administrator
 * @version 1.0
 */
@RestController
@RequestMapping("/")
class AuthController : BaseController(),AuthControllerApi {

    @Value("\${auth.clientId}")
    internal var clientId: String? = null
    @Value("\${auth.clientSecret}")
    internal var clientSecret: String? = null
    @Value("\${auth.cookieDomain}")
    internal var cookieDomain: String? = null
    @Value("\${auth.cookieMaxAge}")
    internal var cookieMaxAge: Int = 0

    @Autowired
    lateinit var authService: AuthService

    //取出cookie中的身份令牌
    private val tokenFormCookie: String?
        get() {
            val request = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request
            val map = CookieUtil.readCookie(request, "uid")
            return if (map["uid"] != null) {
                map["uid"]
            } else null
        }

    @PostMapping("/userlogin")
    override fun login(loginRequest: LoginRequest): LoginResult {
        if (StringUtils.isEmpty(loginRequest.username)) {
            ExceptionCast.cast(AuthCode.AUTH_USERNAME_NONE)
        }
        if (StringUtils.isEmpty(loginRequest.password)) {
            ExceptionCast.cast(AuthCode.AUTH_PASSWORD_NONE)
        }
        //账号
        val username = loginRequest.username
        //密码
        val password = loginRequest.password

        //申请令牌
        val authToken = authService.login(username, password, clientId, clientSecret)

        //用户身份令牌
        val access_token = authToken?.access_token
        //将令牌存储到cookie
        this.saveCookie(access_token)

        return LoginResult(CommonCode.SUCCESS, access_token)
    }

    //将令牌存储到cookie
    private fun saveCookie(token: String?) {

        val response = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).response
        //HttpServletResponse response,String domain,String path, String name, String value, int maxAge,boolean httpOnly
        CookieUtil.addCookie(response, cookieDomain, "/", "uid", token, cookieMaxAge, false)

    }

    //从cookie删除token
    private fun clearCookie(token: String) {

        val response = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).response
        //HttpServletResponse response,String domain,String path, String name, String value, int maxAge,boolean httpOnly
        CookieUtil.addCookie(response, cookieDomain, "/", "uid", token, 0, false)

    }

    //退出
    @PostMapping("/userlogout")
    override fun logout(): ResponseResult {
        //取出cookie中的用户身份令牌
        val uid = tokenFormCookie
        //删除redis中的token
        val result = authService.delToken(uid!!)
        //清除cookie
        this.clearCookie(uid)
        return ResponseResult(CommonCode.SUCCESS)
    }

    @GetMapping("/userjwt")
    override fun userjwt(): JwtResult? {
        var uid = request.getParameter("uid")
        //如果没有 则从cookie中获取
        if (StringUtils.isEmpty(uid)) {
            //取出cookie中的用户身份令牌
            uid = tokenFormCookie
        }

        if (StringUtils.isEmpty(uid)) {
            //取出cookie中的用户身份令牌
            return JwtResult(CommonCode.FAIL, null)
        }

        //拿身份令牌从redis中查询jwt令牌
        val userToken = authService.getUserToken(uid)
        if (userToken != null) {
            //将jwt令牌返回给用户
            val jwt_token = userToken.jwt_token
            // 获取到的数据需要解密，jwt_token中的三部分内容是通过.来连接的，使用的时候讲中间内容部分用base64解码皆可以得到想要的值
            val substring = jwt_token.substring(jwt_token.indexOf("."), jwt_token.lastIndexOf("."))
            val encodeBase64 = Base64.decodeBase64(substring.toByteArray(charset("UTF-8")))
            println("RESULT: " + String(encodeBase64))
            return JwtResult(CommonCode.SUCCESS, jwt_token)
        }
        return null
    }

}