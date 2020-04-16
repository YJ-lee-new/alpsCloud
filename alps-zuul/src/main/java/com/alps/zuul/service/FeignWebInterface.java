package com.alps.zuul.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

//申明feign调用的服务名，
/**
 * @author:Yujie.lee
 * Date:2019年10月23日
 * TodoTODO
 */
@FeignClient(value = "alpsWeb",fallback = FeignWebIterfaceImp.class)
public interface FeignWebInterface {
   //服务方法指定
	@PostMapping("/login")
   String callClient();
}