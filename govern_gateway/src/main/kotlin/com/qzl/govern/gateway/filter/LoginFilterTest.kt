package com.qzl.govern.gateway.filter

import com.alibaba.fastjson.JSON
import com.netflix.zuul.ZuulFilter
import com.netflix.zuul.context.RequestContext
import com.netflix.zuul.exception.ZuulException
import com.qzl.common.model.response.CommonCode
import com.qzl.common.model.response.ResponseResult
import org.apache.commons.lang.StringUtils

/**
 * @author Administrator
 * @version 1.0
 */

//@Component
class LoginFilterTest : ZuulFilter() {

    //过虑器的类型
    override fun filterType(): String {
        /**
         * pre：请求在被路由之前执行
         *
         * routing：在路由请求时调用
         *
         * post：在routing和errror过滤器之后调用
         *
         * error：处理请求时发生错误调用
         *
         */
        return "pre"
    }

    //过虑器序号，越小越被优先执行
    override fun filterOrder(): Int {
        return 0
    }

    override fun shouldFilter(): Boolean {
        //返回true表示要执行此过虑器
        return true
    }

    //过虑器的内容
    //测试的需求：过虑所有请求，判断头部信息是否有Authorization，如果没有则拒绝访问，否则转发到微服务。
    @Throws(ZuulException::class)
    override fun run(): Any? {
        val requestContext = RequestContext.getCurrentContext()
        //得到request
        val request = requestContext.request
        //得到response
        val response = requestContext.response
        //得到Authorization头
        val authorization = request.getHeader("Authorization")
        if (StringUtils.isEmpty(authorization)) {
            //拒绝访问
            requestContext.setSendZuulResponse(false)
            //设置响应代码
            requestContext.responseStatusCode = 200
            //构建响应的信息
            val responseResult = ResponseResult(CommonCode.UNAUTHENTICATED)
            //转成json
            val jsonString = JSON.toJSONString(responseResult)
            requestContext.responseBody = jsonString
            //转成json，设置contentType
            response.contentType = "application/json;charset=utf-8"
            return null
        }

        return null
    }
}
