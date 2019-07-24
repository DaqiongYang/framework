package com.qzl.api

import com.qzl.common.model.response.QueryResponseResult
//import io.swagger.annotations.Api

//@Api(value = "测试接口", description = "框架搭建测试接口")
interface TestApi {
    fun test(name:String): QueryResponseResult
}