package com.qzl.ucenter.dao

import com.qzl.model.ucenter.XcCompanyUser
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Created by Administrator.
 */
interface XcCompanyUserRepository : JpaRepository<XcCompanyUser, String> {
    //根据用户id查询该用户所属的公司id
    fun findByUserId(userId: String): XcCompanyUser
}
