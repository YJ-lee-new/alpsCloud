package com.alps.base.api.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alps.base.api.domain.SysLogic;
import com.alps.common.core.domain.ResultBody;

@FeignClient(value="basePro")
public interface BSysLogicService {
	
	@RequestMapping(value="/logic/all",method=RequestMethod.GET)
	public ResultBody<?> findAll(@RequestParam(value="logicName",required=false) String logicName,
			@RequestParam(value="pageNo",required=true) int pageNo,
			@RequestParam(value="pageSize",required=true) int pageSize);
	
	@RequestMapping(value="/logic/add",method=RequestMethod.POST)
	public ResultBody<?> addLogic(@RequestBody SysLogic sysLogic);
	
	@RequestMapping(value="/logic/edit",method=RequestMethod.POST)
	public ResultBody<?> editLogic(@RequestBody SysLogic sysLogic);
	
	@RequestMapping(value="/logic/delete",method=RequestMethod.POST)
	public ResultBody<?> deleteLogic(@RequestParam(value="ids",required=true) String ids);
	
	@RequestMapping(value="/logic/dicData",method=RequestMethod.GET)
	public ResultBody<?> dicData();
	
	@RequestMapping(value="/logic/checkNameUnquine",method=RequestMethod.POST)
	public ResultBody<?> checkNameUnquine(@RequestParam(value="logicName",required=false) String logicName);

}
