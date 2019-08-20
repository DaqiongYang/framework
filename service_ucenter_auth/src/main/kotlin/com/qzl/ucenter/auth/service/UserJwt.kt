package com.qzl.ucenter.auth.service

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User

class UserJwt(username: String, password: String, authorities: Collection<GrantedAuthority>) : User(username, password, authorities) {

    var id: String? = null
    var name: String? = null
    var userpic: String? = null
    var utype: String? = null
    var companyId: String? = null

}
