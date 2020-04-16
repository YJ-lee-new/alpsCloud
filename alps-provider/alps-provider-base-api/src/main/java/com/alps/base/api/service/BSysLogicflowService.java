package com.alps.base.api.service;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alps.base.api.domain.SysLogicflow;
import com.alps.common.core.domain.ResultBody;

public interface BSysLogicflowService {
	
	@RequestMapping(value="/logicflow/all",method=RequestMethod.GET)
	public ResultBody<?> findAll(@RequestParam(value="logicFlowName",required=false) String logicFlowName,
			@RequestParam(value="pageNo",required=true) int pageNo,
			@RequestParam(value="pageSize",required=true) int pageSize);
	
	@RequestMapping(value="/logicflow/add",method=RequestMethod.POST)
	public ResultBody<?> addLogicflow(@RequestBody SysLogicflow SysLogicflow);
	
	@RequestMapping(value="/logicflow/edit",method=RequestMethod.POST)
	public ResultBody<?> editLogicflow(@RequestBody SysLogicflow SysLogicflow);
	
	@RequestMapping(value="/logicflow/delete",method=RequestMethod.POST)
	public ResultBody<?> deleteLogicflow(@RequestParam(value="ids",required=true) String ids);
	
	@RequestMapping(value="/logicflow/dicData",method=RequestMethod.GET)
	public ResultBody<?> dicData();
	
	

}
