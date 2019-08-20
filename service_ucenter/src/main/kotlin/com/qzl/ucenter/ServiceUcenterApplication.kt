package com.qzl.ucenter

import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.ComponentScan

/**
 * @author 强周亮
 * @desc 用户中心
 * @email 2538096489@qq.com
 * @time 2019/8/20 15:33
 */
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan("com.qzl.ucenter.dao")
@EntityScan("com.qzl.model.ucenter") //扫描实体类
@ComponentScan("com.qzl")//根据自己需要填写包名
@SpringBootApplication
class ServiceUcenterApplication

fun main(args: Array<String>) {
    SpringApplication.run(ServiceUcenterApplication::class.java, *args)
}

