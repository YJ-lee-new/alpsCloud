package com.alps.oauth.uaa.client.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


/**
 * @author:Yujie.lee
 * Date:2020年2月14日
 * 提供非vue外的前端进行快速权限配置
 */
@EnableCaching
@EnableFeignClients
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(basePackages = "com.alps.oauth.uaa.client.web.mapper")
public class AlpsOauthClientWebApplication {

	public static void main(String[] args) {
		 SpringApplication.run(AlpsOauthClientWebApplication.class, args);

	}

}
