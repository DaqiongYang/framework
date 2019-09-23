package com.qzl.govern.gateway.service

import com.qzl.util.CookieUtil
import org.apache.commons.lang.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit
import javax.servlet.http.HttpServletRequest

@Service
class AuthService {

    @Autowired
    lateinit var stringRedisTemplate: StringRedisTemplate

    //从头取出jwt令牌
    fun getJwtFromHeader(request: HttpServletRequest): String? {
        //取出头信息
        val authorization = request.getHeader("Authorization")
        if (StringUtils.isEmpty(authorization)) {
            return null
        }
        return if (!authorization.startsWith("Bearer ")) {
            null
        } else authorization.substring(7)
        //取到jwt令牌


    }

    //从cookie取出token
    //查询身份令牌
    fun getTokenFromCookie(request: HttpServletRequest): String? {
        val cookieMap = CookieUtil.readCookie(request, "uid")
        val access_token = cookieMap["uid"]
        return if (StringUtils.isEmpty(access_token)) {
            null
        } else access_token
    }

    //查询令牌的有效期
    fun getExpire(access_token: String?): Long {
        //key
        val key = "user_token:$access_token"
        return stringRedisTemplate.getExpire(key, TimeUnit.SECONDS)
    }
}
