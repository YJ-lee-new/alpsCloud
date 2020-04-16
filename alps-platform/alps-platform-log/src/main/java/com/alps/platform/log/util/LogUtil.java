package com.alps.platform.log.util;

import java.util.concurrent.ThreadLocalRandom;

import com.alps.common.constant.TraceConstant;

import lombok.extern.slf4j.Slf4j;
/**
 * @author:Yujie.lee
 * Date:2019年11月23日
 * TodoTODO
 */
@Slf4j
public class LogUtil {
	 
	public static TraceConstant traceConstant ;
   
    /**
	 * 生成日志随机数
	 * 
	 * @return
	 */
	public static String getTraceId() {
		int i = 0;
		StringBuilder st = new StringBuilder();
		while (i < 5) {
			i++;
			st.append(ThreadLocalRandom.current().nextInt(10));
		}
		return st.toString() + System.currentTimeMillis();
	}
	 
}
