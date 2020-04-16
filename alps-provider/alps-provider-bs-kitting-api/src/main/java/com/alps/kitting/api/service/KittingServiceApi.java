package com.alps.kitting.api.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alps.common.core.domain.ResultBody;
import com.alps.kitting.api.domain.CheckConfig;
import com.alps.kitting.api.domain.SaveModel;
import com.alps.kitting.api.domain.ScanPN;
import com.alps.kitting.api.domain.ScanSN;

@FeignClient(value="bsPro")
public interface KittingServiceApi {
	
	@RequestMapping(value = "/kit/getKitStation",method = RequestMethod.GET)
	public ResultBody<?> dicKitStation();
	
	@RequestMapping(value = "/kit/getLine",method = RequestMethod.GET)
	public ResultBody<?> dicLine();
	
	@RequestMapping(value = "/kit/getKitData",method = RequestMethod.GET)
	public ResultBody<?> getKitData(
			@RequestParam(value="batchNo",required = true) String batchNo,
			@RequestParam(value="kitStation",required = true) String kitStation,
			@RequestParam(value="line",required = true) String line,
			@RequestParam(value="createBy") String createBy
			);
	
	@RequestMapping(value = "/kit/scanMpart",method = RequestMethod.GET)
	public ResultBody<?> scanMpart(
			@RequestParam(value="kitStation",required = true) String kitStation,
			@RequestParam(value="line",required = true) String line,
			@RequestParam(value="batchNo",required = true) String batchNo,
			@RequestParam(value="mainPartNo",required = true) String mainPartNo);
	
	@RequestMapping(value = "/kit/scanSN",method = RequestMethod.POST)
	public ResultBody<?> scanSN(@RequestBody ScanSN ssp);
	
	@RequestMapping(value = "/kit/scanPartno",method = RequestMethod.POST)
	public ResultBody<?> scanPartno(@RequestBody ScanPN scanPN);
	
	@RequestMapping(value = "/kit/kitpass",method = RequestMethod.POST)
	public ResultBody<?> kitpass(@RequestBody SaveModel saveModel);
	
	@RequestMapping(value = "/kit/checkError",method = RequestMethod.POST)
	public ResultBody<?> checkError(@RequestBody CheckConfig checkConfig);
	
	@RequestMapping(value = "/kit/kitfail",method = RequestMethod.POST)
	public ResultBody<?> kitFail(@RequestBody SaveModel saveModel);
	
	@RequestMapping(value = "/kit/saveError",method = RequestMethod.POST)
	public ResultBody<?> saveError(@RequestBody SaveModel saveModel);
	
	@RequestMapping(value = "/kit/kitswap",method = RequestMethod.POST)
	public ResultBody<?> kitswap(@RequestBody SaveModel saveModel);
	
	
	
}
