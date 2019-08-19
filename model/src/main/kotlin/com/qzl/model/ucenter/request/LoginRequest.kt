package com.qzl.model.ucenter.request


import com.qzl.common.model.request.RequestData

/**
 * Created by admin on 2018/3/5.
 */
class LoginRequest : RequestData() {

    var username: String? = null
    var password: String? = null
    var verifycode: String? = null

}
