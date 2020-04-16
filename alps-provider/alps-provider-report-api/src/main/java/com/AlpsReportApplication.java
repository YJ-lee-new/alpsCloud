/**
 *@date:2019年11月4日
 *@author:Yujie.Lee
 *TODO
 */
package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author:Yujie.lee
 * Date:2019年11月4日
 * TodoTODO
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableEurekaClient //Eureka Client
@RestController
public class AlpsReportApplication {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SpringApplication.run( AlpsReportApplication.class, args );

	}

}
