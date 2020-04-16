package com.alps.oauth.uaa.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author:Yujie.lee
 * Date:2019年11月27日
 * 平台认证服务
 * 提供微服务间oauth2统一平台认证服务
 * 提供认证客户端、令牌、已授权管理`
 */
@EnableCaching
@EnableFeignClients
@SpringBootApplication
@EnableDiscoveryClient
public class UaaAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(UaaAdminApplication.class, args);
    }


}
