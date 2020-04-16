package com.alps.base.api.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alps.common.core.domain.ResultBody;

@FeignClient(value="basePro")
public interface BSysMenuService {
	
	@RequestMapping(value="/menu/dicMenuData",method=RequestMethod.GET)
	public ResultBody<?> dicMenuData();
	

}
