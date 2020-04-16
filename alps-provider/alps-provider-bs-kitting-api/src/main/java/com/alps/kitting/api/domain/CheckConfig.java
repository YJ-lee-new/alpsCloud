package com.alps.kitting.api.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "验证配置栏位mode")
@Data
public class CheckConfig {
	
	@ApiModelProperty(required = true,value = "线别")
	private String line;
	
	@ApiModelProperty(required = true,value = "工站")
	private String kitStation;
	
	@ApiModelProperty(required = true,value = "成品SN")
	private String serialNo;
	
	private String batchNo;
	
	@ApiModelProperty(required = true,value = "需要验证的类型的value")
	private String value;
	
	@ApiModelProperty(required = true,value = "需要验证的类型,如CHECKMAC")
	private String trantype;
	
	private String createBy ;
	
	private String mainPartNo;

}
