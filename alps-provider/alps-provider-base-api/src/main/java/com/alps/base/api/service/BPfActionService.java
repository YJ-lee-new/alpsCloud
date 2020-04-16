package com.alps.base.api.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alps.common.core.domain.ResultBody;

@FeignClient(value = "basePro")
public interface BPfActionService {
	
	@RequestMapping(value = "/action/loading",method = RequestMethod.POST)
	public ResultBody<?> findByMenuId(@RequestParam(value="companyId",required = true) String companyId,
			@RequestParam(value="menuId",required = true) Long menuId);
	
	@RequestMapping(value = "/",method = RequestMethod.POST)
	public ResultBody<?> findLogic(@RequestParam(value="apiId",required = true) String apiId);

}
