package com.alps.gateway;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import com.alps.gateway.client.vo.AuthIgnored;
/**
 * @author:Yujie.lee
 * Date:2019年11月21日
 * TodoTODO
 */
@EnableCircuitBreaker
@SpringBootApplication
@EnableDiscoveryClient
@EnableConfigurationProperties(AuthIgnored.class)
public class AlpsGateWayApplication {
    public static void main(String[] args) {
        SpringApplication.run(AlpsGateWayApplication.class, args);
    }
}  