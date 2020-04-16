/**
 *@date:2020年1月11日
 *@author:Yujie.Lee
 *TODO
 */
package com.alps.gateway.client.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alps.gateway.dynamic.service.DynamicRouteService;

/**
 * @author:Yujie.lee
 * Date:2020年1月11日
 * TodoTODO
 */
@Configuration
public class RouteLoadConfig {
	 @Autowired
	 private DynamicRouteService dynamicRouteService;
	 
	 @Bean
      public void loadingAllroute() {
		 dynamicRouteService.synchronization();
      }
}
