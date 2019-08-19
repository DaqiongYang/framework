package com.qzl.api.auth

import com.qzl.common.model.response.ResponseResult
import com.qzl.model.ucenter.request.LoginRequest
import com.qzl.model.ucenter.response.JwtResult
import com.qzl.model.ucenter.response.LoginResult
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation

/**
 * 用户认证接口定义
 */
@Api(value = "用户认证", description = "用户认证接口")
interface AuthControllerApi {
    @ApiOperation("登录")
    fun login(loginRequest: LoginRequest): LoginResult

    @ApiOperation("退出")
    fun logout(): ResponseResult

    @ApiOperation("查询用户jwt令牌")
    fun userjwt(): JwtResult?
}
