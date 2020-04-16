package com.alps.base.api.service;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alps.base.api.domain.SysFunctionMenu;
import com.alps.common.core.domain.ResultBody;

@FeignClient(value="basePro")
public interface BSysFunctionService {
	
	@RequestMapping(value="/function/all",method=RequestMethod.GET)
	public ResultBody<?> findAll(@RequestParam(value="menuId",required=false) Integer menuId,
			@RequestParam(value="functionName",required=false) String functionName,
			@RequestParam(value="pageNo",required=true) int pageNo,
			@RequestParam(value="pageSize",required=true) int pageSize);
	
	@RequestMapping(value="/function/add",method=RequestMethod.POST)
	public ResultBody<?> addFunction(@RequestBody SysFunctionMenu sysfunction);
	
	@RequestMapping(value="/function/edit",method=RequestMethod.POST)
	public ResultBody<?> editFunction(@RequestBody SysFunctionMenu sysfunction);
	
	@RequestMapping(value="/function/delete",method=RequestMethod.POST)
	public ResultBody<?> deleteFunction(@RequestParam(value="ids",required=true) String ids);
	
	@RequestMapping(value="/function/dicData",method=RequestMethod.GET)
	public ResultBody<?> dicData();
	
	
	
	
}
