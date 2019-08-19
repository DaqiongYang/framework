package com.qzl.model.ucenter.response


import com.qzl.common.model.response.ResponseResult
import com.qzl.common.model.response.ResultCode

/**
 * Created by mrt on 2018/5/21.
 */
class JwtResult(resultCode: ResultCode, private val jwt: String) : ResponseResult(resultCode)
