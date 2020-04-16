package com.alps.kitting.api.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "扫描SN参数mode")
public class ScanSN {
	
	@ApiModelProperty(required = true,value = "线别")
	private String line;
	
	@ApiModelProperty(required = true,value = "工站")
	private String kitStation;
	
	@ApiModelProperty(required = true,value = "成品SN")
	private String serialNo;
	
	private String batchNo;
	
	private String mainPartNo;
	
	@ApiModelProperty(value = "需要验证的类型")
	private String trantype;
	
	private String createBy ;
	

}
