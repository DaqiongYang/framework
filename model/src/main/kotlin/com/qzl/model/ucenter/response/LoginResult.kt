package com.qzl.model.ucenter.response


import com.qzl.common.model.response.ResponseResult
import com.qzl.common.model.response.ResultCode

/**
 * Created by mrt on 2018/5/21.
 */
data class LoginResult(private var resultCode: ResultCode, var token: String?) : ResponseResult(resultCode)
