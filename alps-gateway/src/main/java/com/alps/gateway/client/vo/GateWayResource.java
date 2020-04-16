/**
 *@date:2020年1月7日
 *@author:Yujie.Lee
 *TODO
 */
package com.alps.gateway.client.vo;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;


/**
 * @author:Yujie.lee
 * Date:2020年1月7日
 * TodoTODO
 */
@Component
public class GateWayResource {
	
	@Resource
	private GatewayProperties properties;
	
	private AntPathMatcher pathMatcher = new AntPathMatcher();
	public String getRouteId(String uri){
		List<RouteDefinition> routes = properties.getRoutes();
		/**
		 * 优先匹配原则,确保路由唯一
		 */
		for(RouteDefinition route : routes ) {
			Map<String,String> arg = route.getPredicates().get(0).getArgs();
			String routeUri = arg.get("_genkey_0");
           if(pathMatcher.match(routeUri,uri)) {
        	   routeUri = routeUri.replace("/**","");
        	   uri = uri.replace(routeUri, "");
        	   return uri;
           }
		}
	    return null;
	}

}
