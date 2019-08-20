package com.qzl.ucenter.controller

import com.qzl.api.ucenter.UcenterControllerApi
import com.qzl.model.ucenter.ext.XcUserExt
import com.qzl.ucenter.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * @author Administrator
 * @version 1.0
 */
@RestController
@RequestMapping("/ucenter")
class UcenterController : UcenterControllerApi {
    @Autowired
    lateinit var userService: UserService

    @GetMapping("/getuserext")
    override fun getUserext(@RequestParam("username") username: String): XcUserExt? {
        return userService.getUserExt(username)
    }
}
