[toc]

---

github地址：https://github.com/qiangzhouliang/framework  
分支：eureka_server
# 1 服务发现者 EurekaServer 服务
## 1.1 引入配置
在父工程中引入spring-cloud-dependencies
```
dependencyManagement {
    imports {
        mavenBom "org.springframework.cloud:spring-cloud-dependencies:Finchley.SR1"
    }
}
```
在子工程中引入
```
dependencies {
    implementation 'org.springframework.cloud:spring-cloud-starter-netflix-eureka-server'
}
```
## 1.2 配置文件编写
```
server:
  port: ${PORT:50101} #服务端口
spring:
  application:
    name: xc-govern-center #指定服务名
  freemarker:
    prefer-file-system-access: false
eureka:
  client:
    register-with-eureka: true #服务注册，是否将自己注册到Eureka服务中
    fetch-registry: true #服务发现，是否从Eureka中获取注册信息
    service-url: #Eureka客户端与Eureka服务端的交互地址，高可用状态配置对方的地址，单机状态配置自己（如果不配置则默认本机8761端口）
      defaultZone: ${EUREKA_SERVER:http://eureka02:50102/eureka/}
  server:
    enable-self-preservation: false #是否开启自我保护模式
    eviction-interval-timer-in-ms: 60000 #服务注册表清理间隔（单位毫秒，默认是60*1000）
  instance:
    hostname: ${EUREKA_DOMAIN:eureka01}
```
## 1.3 启动类
```
package com.qzl.govern.center

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer

/**
 * EurekaServer 服务
 */
@EnableEurekaServer  //标识此工程是一个EurekaServer
@SpringBootApplication
class GovernCenterApplication

fun main(args: Array<String>) {
    SpringApplication.run(GovernCenterApplication::class.java, *args)
}
```
# 2 将自己的服务注册进去
## 2.1 引入配置
```
dependencies {
    implementation 'org.springframework.cloud:spring-cloud-starter-netflix-eureka-client'
}
```
## 2.2 添加配置文件信息
```
eureka:
  client:
    registerWithEureka: true #服务注册开关
    fetchRegistry: true #服务发现开关
    serviceUrl: #Eureka客户端与Eureka服务端进行交互的地址，多个中间用逗号分隔
      defaultZone: ${EUREKA_SERVER:http://localhost:50101/eureka/,http://localhost:50102/eureka/}
  instance:
    prefer-ip-address: true #将自己的ip地址注册到Eureka服务中
    ip-address: ${IP_ADDRESS:127.0.0.1}
    instance-id: ${spring.application.name}:${server.port} #指定实例id
```
## 2.3 启动类中添加注解
```
@EnableDiscoveryClient  //一个EurekaClient从EurekaServer发现服务
```
# 3 服务调度，实现负载均衡
## 3.1 引入配置
```
dependencies {
    implementation 'org.springframework.cloud:spring-cloud-starter-netflix-eureka-client'
    implementation 'org.springframework.cloud:spring-cloud-starter-openfeign'
    implementation 'com.squareup.okhttp3:okhttp:3.9.1'
}
```
## 3.2 启动类中添加注解
```
package com.qzl.govern.manage

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.ComponentScan

/**
 * 负载均衡实现
 */
@EnableFeignClients  //开启FeignClient
@EnableDiscoveryClient  //一个EurekaClient从EurekaServer发现服务
@SpringBootApplication
@ComponentScan("com.qzl")//根据自己需要填写包名
class GovernManageApplication

fun main(args: Array<String>) {
    SpringApplication.run(GovernManageApplication::class.java, *args)
}
```
## 3.3 调度配置  

- 1 FeignClient 进行负载均衡
```
package com.qzl.govern.manage

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(value = "APP")  //指定远程调用的服务名
interface RestControllers {
    //根据页面id查询页面信息，远程调用cms请求数据
    @GetMapping("/testK")//这里的请求路径需要和APP中的请求路径一致
    fun testK(@RequestParam("name") name:String): String//这里的方法名需要和eureka-client中的方法名一致
}
```
- 2 restTemplate 实现负载均衡注册bean
```
package com.qzl.govern.manage.config

import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory
import org.springframework.web.client.RestTemplate

@Configuration
class RestTemplateConfig {

    /**
     * 构建一个使用默认配置RestTemplate Bean
     */
    @Bean
    @LoadBalanced
    fun restTemplate(): RestTemplate {
        return RestTemplate(OkHttp3ClientHttpRequestFactory())
    }
}
```
- 3 具体实现
```
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
```
- 4 工具类
```
package com.qzl.util;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.*;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Util {
	/**
	 * @Title: getPostParamter  
	 * @Description: 获取请求get参数
	 * @author 强周亮   
	 * @param @param map
	 * @param @return    参数  
	 * @return String    返回类型
	 * @date 2018年11月15日 上午10:24:54  
	 * @throws
	 */
	public static String getParamter(Map map) {
		Set keSet=map.entrySet();
	    String string = "?";
	    for(Iterator itr = keSet.iterator(); itr.hasNext();){
	        Map.Entry me=(Map.Entry)itr.next();
	        Object ok=me.getKey();  
	        Object ov=me.getValue();  
	        String[] value=new String[1];  
	        if(ov instanceof String[]){  
	            value=(String[])ov;  
	        }else{  
	            value[0]=ov.toString();  
	        }  
	  
	        for(int k=0;k<value.length;k++){  
	            string +=ok+"="+value[k]+"&";
	        }  
	      }
		return string;
	}
	/**
	 * @Title: input2byte  
	 * @Description: 字节流转换为byte
	 * @author 强周亮   
	 * @param @param inStream
	 * @param @return
	 * @param @throws IOException    参数  
	 * @return byte[]    返回类型
	 * @date 2018年11月15日 下午5:11:31  
	 * @throws
	 */
	public static byte[] input2byte(InputStream inStream) throws IOException {
         ByteArrayOutputStream swapStream = new ByteArrayOutputStream();  
         byte[] buff = new byte[100];  
         int rc = 0;  
         while ((rc = inStream.read(buff, 0, 100)) > 0) {  
            swapStream.write(buff, 0, rc);  
         }  
         byte[] in2b = swapStream.toByteArray();  
         return in2b;  
     }
	
	/**
	 * @Title: inputstreamtofile  
	 * @Description: 输入流转文件
	 * @author 强周亮   
	 * @param @param ins
	 * @param @param file    参数  
	 * @return void    返回类型
	 * @date 2018年11月15日 下午5:16:09  
	 * @throws
	 */
	public static void inputstreamtofile(InputStream ins,File file){
		OutputStream os;
		try {
			os = new FileOutputStream(file);
			int bytesRead = 0;
			   byte[] buffer = new byte[8192];
			   while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
			      os.write(buffer, 0, bytesRead);
			   }
			   os.close();
			   ins.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**  
	 * @Title: getParamter  
	 * @Description: 获取post参数
	 * @author 强周亮   
	 * @param @param map
	 * @param @return    参数  
	 * @return String    返回类型
	 * @date 2018年11月14日 下午8:23:51  
	 * @throws  
	 */ 
	public static MultiValueMap getPostParamter(Map map) {
		Set keSet=map.entrySet();  
	    MultiValueMap valueMap= new LinkedMultiValueMap();
	    for(Iterator itr=keSet.iterator();itr.hasNext();){  
	        Map.Entry me=(Map.Entry)itr.next();  
	        Object ok=me.getKey();  
	        Object ov=me.getValue();  
	        String[] value=new String[1];  
	        if(ov instanceof String[]){  
	            value=(String[])ov;  
	        }else{  
	            value[0]=ov.toString();
	        }  
	  
	        for(int k=0;k<value.length;k++){  
	            valueMap.add(ok, value[k]);
	        }  
	      }
		return valueMap;
	}
	/**
     * 递归删除目录下的所有文件及子目录下所有文件
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     *                 If a deletion fails, the method stops attempting to
     *                 delete and returns "false".
     */
    public static boolean deleteDirFile(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
            	new File(dir, children[i]).delete();
            }
        }
        return true;
    }
	/**
     * 递归删除目录下的所有文件及子目录下所有文件
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     *                 If a deletion fails, the method stops attempting to
     *                 delete and returns "false".
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return true;
    }
}
```
