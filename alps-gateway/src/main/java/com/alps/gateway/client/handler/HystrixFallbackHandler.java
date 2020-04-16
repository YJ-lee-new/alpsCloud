package com.alps.gateway.client.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
/**
 * @author:Yujie.lee
 * Date:2019年11月23日
 * TodoTODO
 */
@Slf4j
@Component
public class HystrixFallbackHandler implements HandlerFunction<ServerResponse> {

     
    @Override
    public Mono<ServerResponse> handle(ServerRequest serverRequest) {
        serverRequest.attribute(ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR)
            .ifPresent(originalUrls ->  System.out.println("asd"));

        Map resp = new HashMap();
        resp.put("resp_code", "9999");
        resp.put("resp_msg", "程序失败");
        return ServerResponse
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .body(BodyInserters.fromObject(resp));
    }
}
