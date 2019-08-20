package com.qzl.api.ucenter

import com.qzl.model.ucenter.ext.XcUserExt
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation

/**
 * Created by Administrator.
 */
@Api(value = "用户中心", description = "用户中心管理")
interface UcenterControllerApi {
    @ApiOperation("根据用户账号查询用户信息")
    fun getUserext(username: String): XcUserExt
}
