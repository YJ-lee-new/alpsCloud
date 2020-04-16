package com.alps.gateway.client.filter;

import java.util.function.Consumer;

import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.alps.common.constant.TraceConstant;
import com.alps.common.constant.UaaConstant;
import com.alps.gateway.client.utils.TokenUtil;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
/**
 * @author:Yujie.lee
 * Date:2019年11月23日
 * TodoTODO
 */
@Slf4j
@Component
public class RequestFilter implements GlobalFilter, Ordered {


	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return -501;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		 
		String traceId = MDC.get(TraceConstant.LOG_B3_TRACEID);
		MDC.put(TraceConstant.LOG_TRACE_ID, traceId);
		String accessToken = TokenUtil.extractToken(exchange.getRequest());
		log.info("accessToken = " + accessToken);
		Consumer<HttpHeaders> httpHeaders = httpHeader -> {
            httpHeader.set(TraceConstant.HTTP_HEADER_TRACE_ID, traceId);
            httpHeader.set(UaaConstant.TOKEN_HEADER, accessToken);
        };
        ServerHttpRequest serverHttpRequest = exchange.getRequest().mutate().headers(httpHeaders).build();
        ServerWebExchange build = exchange.mutate().request(serverHttpRequest).build();
        
        log.info("request url = " + exchange.getRequest().getURI() + ", traceId = " + traceId);
	 
		
        return chain.filter(build);

		
	}

	 

}
