package com.qzl.api.ucenter

import com.qzl.model.ucenter.ext.XcUserExt
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation

/**
 * @author 强周亮
 * @desc 用户中心接口
 * @email 2538096489@qq.com
 * @time 2019/8/20 17:06
 */
@Api(value = "用户中心", description = "用户中心管理")
interface UcenterControllerApi {
    @ApiOperation(value = "根据用户账号查询用户信息")
    fun getUserext(username: String): XcUserExt
}
