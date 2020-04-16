package com.alps.pack.api.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alps.common.core.domain.ResultBody;

@FeignClient(value="bsPack")
public interface PackServiceApi {
	
	//扫描WO
	@RequestMapping(value="/pack/scanWO",method=RequestMethod.POST)
	public ResultBody<?> scanWO();
	
	//检查SSN清单
	@RequestMapping(value="/pack/checkSSN",method=RequestMethod.POST)
	public ResultBody<?> checkSSN();
	
	
	//保存SSN清单
	@RequestMapping(value="/pack/saveSSN",method=RequestMethod.POST)
	public ResultBody<?> saveSSN();
	
	//需要打印调用此接口
	@RequestMapping(value="/pack/print",method=RequestMethod.POST)
	public ResultBody<?> boxPrint();

}
