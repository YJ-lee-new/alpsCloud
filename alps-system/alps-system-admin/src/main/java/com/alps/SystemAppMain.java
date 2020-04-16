package com.alps;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.RestController;
 
/**
 * @author:Yujie.lee
 * Date:2019年10月24日
 * TodoTODO
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class})
@EnableEurekaClient //Eureka
@RestController
@MapperScan("com.alps.*.mapper")
public class SystemAppMain {
    public static void main(String[] args) {
    	SpringApplication.run( SystemAppMain.class, args );
    	System.out.println(" wellcome ======> Advanced live production system bas been started!  ");
    }
}