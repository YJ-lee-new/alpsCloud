package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 *@date:2019年10月22日
 *@author:Yujie.Lee
 *TODO
 */
@SpringBootApplication
@EnableEurekaServer
public class AlpsServerApplication {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SpringApplication.run( AlpsServerApplication.class, args );
	}

}
