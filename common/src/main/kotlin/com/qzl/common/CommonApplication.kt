package com.qzl.common
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan("com.qzl")//根据自己需要填写包名
class CommonApplication

fun main(args: Array<String>) {
    SpringApplication.run(CommonApplication::class.java, *args)
}
