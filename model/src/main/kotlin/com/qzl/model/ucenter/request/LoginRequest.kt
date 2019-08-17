package com.qzl.model.ucenter.request


import com.qzl.common.model.request.RequestData

/**
 * Created by admin on 2018/3/5.
 */
class LoginRequest : RequestData() {

    internal var username: String? = null
    internal var password: String? = null
    internal var verifycode: String? = null

}
