package com.qzl.api

import com.qzl.common.model.response.QueryResponseResult
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation

@Api(value = "测试接口", description = "框架搭建测试接口")
interface TestApi {
    @ApiOperation(value = "测试方法")
    fun test(name:String): QueryResponseResult
}