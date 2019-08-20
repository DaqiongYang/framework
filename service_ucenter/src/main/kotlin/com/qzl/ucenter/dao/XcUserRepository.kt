package com.qzl.ucenter.dao

import com.qzl.model.ucenter.XcUser
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Created by Administrator.
 */
interface XcUserRepository : JpaRepository<XcUser, String> {
    //根据账号查询用户信息
    fun findByUsername(username: String): XcUser
}
