package com.qzl.app
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.context.annotation.ComponentScan

@EnableDiscoveryClient  //一个EurekaClient从EurekaServer发现服务
@SpringBootApplication
@ComponentScan("com.qzl")//根据自己需要填写包名
class AppApplication

fun main(args: Array<String>) {
    SpringApplication.run(AppApplication::class.java, *args)
}
