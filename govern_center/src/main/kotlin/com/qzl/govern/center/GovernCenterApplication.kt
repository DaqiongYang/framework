package com.qzl.govern.center

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

/**
 * EurekaServer 服务
 */
@EnableEurekaServer  //标识此工程是一个EurekaServer
@SpringBootApplication
class GovernCenterApplication

fun main(args: Array<String>) {
    SpringApplication.run(GovernCenterApplication::class.java, *args)
}