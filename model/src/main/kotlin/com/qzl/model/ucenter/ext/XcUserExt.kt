package com.qzl.model.ucenter.ext


import com.qzl.model.ucenter.XcMenu
import com.qzl.model.ucenter.XcUser

/**
 * Created by admin on 2018/3/20.
 */
class XcUserExt : XcUser() {

    //权限信息
    var permissions: List<XcMenu>? = null

    //企业信息
    var companyId: String? = null
}
