package com.alps.gateway.client.filter;
import java.nio.charset.StandardCharsets;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;

import com.alibaba.fastjson.JSONObject;
import com.alps.common.config.PermitUrlProperties;
import com.alps.common.constant.UaaConstant;
import com.alps.gateway.client.utils.TokenUtil;
import com.alps.gateway.client.vo.GateWayResource;

import reactor.core.publisher.Mono;
/**
 * @author:Yujie.lee
 * Date:2019年11月23日
 * TodoTODO
 */
@Component
@EnableConfigurationProperties(PermitUrlProperties.class)
public class AccessFilter implements GlobalFilter, Ordered {

	// url匹配器
	private AntPathMatcher pathMatcher = new AntPathMatcher();
// 使用StringRedisSerializer序列化key时需要使用StringRedisTemplate拿值,否则会返回null
//    @Autowired
//    private RedisTemplate<?, ?>  redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
	
	@Resource
	private PermitUrlProperties permitUrlProperties;
	
	@Autowired
	private GateWayResource gateWayResource;

	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return -500;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		// TODO Auto-generated method stub

		String accessToken = TokenUtil.extractToken(exchange.getRequest());
		String uri =  exchange.getRequest().getPath().value();
		

		// 默认
		boolean flag = false;

		for (String ignored :permitUrlProperties.getIgnored()) {

			if (pathMatcher.match(ignored,uri)) {
				flag = true; // 白名单
			}

		}
				
		if (flag) {
			return chain.filter(exchange);
		} else {
			Object params = stringRedisTemplate.opsForValue().get(UaaConstant.AUTH +":" + accessToken);
			//check访问是否带Token
			if (params != null) {
				String url = gateWayResource.getRouteId(uri);
				//check该uri是否合法
				if(url != null) {
					Object toekn2api =stringRedisTemplate.boundHashOps(UaaConstant.API_OKEN + ":" + accessToken).get(url);
					//check该token是否是有该API的访问权限. 
					if(toekn2api != null) {
						return chain.filter(exchange);
					}else {
						return	MonoErrorMsg(exchange, "4031", "The token not be allowed to access this url:" + url);
					}
				}else {
					return	MonoErrorMsg(exchange, "404", "Route not found!");
				}
				
			} else {
				return MonoErrorMsg(exchange, "403", "Token is invalid!");
			}
		}
	}
	
	public Mono<Void> MonoErrorMsg(ServerWebExchange exchange,String cod,String msg){
		exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
		ServerHttpResponse response = exchange.getResponse();
		JSONObject message = new JSONObject();
		message.put("resp_code", cod);
		message.put("resp_msg", msg);
		byte[] bits = message.toJSONString().getBytes(StandardCharsets.UTF_8);
		DataBuffer buffer = response.bufferFactory().wrap(bits);
		response.setStatusCode(HttpStatus.UNAUTHORIZED);
		// 指定编码，否则在浏览器中会中文乱码
		response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
		return response.writeWith(Mono.just(buffer));
	}


}
