package com.alps.platform.log.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;

import com.alps.common.constant.TraceConstant;
/**
 * @author:Yujie.lee
 * Date:2019年11月23日
 * Todo 首先创建拦截器，加入拦截列表中，在请求到达时生成traceId。
 */
public class LogInterceptor implements HandlerInterceptor {
	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // "traceId"
       
        String traceId = request.getHeader(TraceConstant.HTTP_HEADER_TRACE_ID);
        if (StringUtils.isNotEmpty(traceId)) {
        	 MDC.put(TraceConstant.LOG_TRACE_ID, traceId);
        }

        return true;
    }
}