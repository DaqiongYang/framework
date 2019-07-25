package com.qzl.model
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan("com.qzl")//根据自己需要填写包名
class ModerApplication

fun main(args: Array<String>) {
    SpringApplication.run(ModerApplication::class.java, *args)
}
