package com.qzl.govern.manage

import com.qzl.util.Util
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
class TestController {
    // 使用FeignClient实现负载均衡
    @Autowired lateinit var restController: RestControllers
//    resttemplate 实现负载均衡
    @Autowired lateinit var restTemplate: RestTemplate

    @RequestMapping("/**")
    fun test1(request: HttpServletRequest, response: HttpServletResponse):String {
        //确定要获取的服务名称
        val serviceID = "APP"
        val servletPath = request.servletPath
        //请求参数
        val parames = Util.getParamter(request.parameterMap)
        //ribbon客户端从eurekaServer中获取服务列表,根据服务名获取服务列表
        val forEntity = restTemplate.getForEntity("http://"+serviceID+servletPath+parames, String::class.java)
        return forEntity?.body.toString()
    }

    @GetMapping("/testK1")
    fun test(@RequestParam("name") name:String): String {
        //暂时用静态数据
        val test = restController.testK(name)
        return test
    }
}