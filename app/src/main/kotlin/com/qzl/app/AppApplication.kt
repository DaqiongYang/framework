package com.qzl.app
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan("com.qzl")//根据自己需要填写包名
class AppApplication

fun main(args: Array<String>) {
    SpringApplication.run(AppApplication::class.java, *args)
}
