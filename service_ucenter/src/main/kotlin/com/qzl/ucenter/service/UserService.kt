package com.qzl.ucenter.service

import com.qzl.model.ucenter.XcUser
import com.qzl.model.ucenter.ext.XcUserExt
import com.qzl.ucenter.dao.XcCompanyUserRepository
import com.qzl.ucenter.dao.XcUserRepository
import org.springframework.beans.BeanUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author Administrator
 * @version 1.0
 */
@Service
class UserService {

    @Autowired
    lateinit var xcUserRepository: XcUserRepository

    @Autowired
    lateinit var xcCompanyUserRepository: XcCompanyUserRepository

    //根据账号查询xcUser信息
    fun findXcUserByUsername(username: String): XcUser {
        return xcUserRepository.findByUsername(username)
    }

    //根据账号查询用户信息
    fun getUserExt(username: String): XcUserExt? {
        //根据账号查询xcUser信息
        val xcUser = this.findXcUserByUsername(username) ?: return null
//用户id
        val userId = xcUser.id
        //根据用户id查询用户所属公司id
        val xcCompanyUser = xcCompanyUserRepository.findByUserId(userId!!)
        //取到用户的公司id
        var companyId: String? = null
        if (xcCompanyUser != null) {
            companyId = xcCompanyUser.companyId
        }
        val xcUserExt = XcUserExt()
        BeanUtils.copyProperties(xcUser, xcUserExt)
        xcUserExt.companyId = companyId
        return xcUserExt

    }

}
