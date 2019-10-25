package com.qzl.ucenter.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;


/**
 * @author 强周亮
 * @desc 用户认证中心
 * @email 2538096489@qq.com
 * @time 2019/8/17 17:45
 * @class ServiceUcenterAuthApplication
 * @package com.qzl.ucenter.auth
 */
@EnableDiscoveryClient
@EnableFeignClients
@EntityScan("com.qzl.model") //扫描实体类
@ComponentScan("com.qzl")
@SpringBootApplication
public class UcenterAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(UcenterAuthApplication.class, args);
    }
}
