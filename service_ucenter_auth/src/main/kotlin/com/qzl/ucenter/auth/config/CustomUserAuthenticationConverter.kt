package com.qzl.ucenter.auth.config

import com.qzl.ucenter.auth.service.UserJwt
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter
import org.springframework.stereotype.Component

import java.util.LinkedHashMap

@Component
class CustomUserAuthenticationConverter : DefaultUserAuthenticationConverter() {
    @Autowired
    internal var userDetailsService: UserDetailsService? = null

    override fun convertUserAuthentication(authentication: Authentication): Map<String, *> {
        val response = LinkedHashMap<String,Any?>()
        val name = authentication.name
        response.put("user_name", name)

        val principal = authentication.principal
        var userJwt: UserJwt? = null
        if (principal is UserJwt) {
            userJwt = principal
        } else {
            //refresh_token默认不去调用userdetailService获取用户信息，这里我们手动去调用，得到 UserJwt
            val userDetails = userDetailsService!!.loadUserByUsername(name)
            userJwt = userDetails as UserJwt
        }
        response.put("name", userJwt.name)
        response.put("id", userJwt.id)
        response.put("utype", userJwt.utype)
        response.put("userpic", userJwt.userpic)
        response.put("companyId", userJwt.companyId)
        if (authentication.authorities != null && !authentication.authorities.isEmpty()) {
            response.put("authorities", AuthorityUtils.authorityListToSet(authentication.authorities))
        }

        return response
    }


}
