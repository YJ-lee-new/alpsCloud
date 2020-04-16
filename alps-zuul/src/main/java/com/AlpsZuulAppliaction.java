package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;


/**
 * @author:Yujie.lee
 * Date:2019年10月28日
 * Todo Application
 */
@EnableZuulProxy
@EnableDiscoveryClient
@EnableHystrix
@EnableFeignClients
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class AlpsZuulAppliaction {
	public static void main(String[] args) {
		SpringApplication.run( AlpsZuulAppliaction.class, args );
	}

}
