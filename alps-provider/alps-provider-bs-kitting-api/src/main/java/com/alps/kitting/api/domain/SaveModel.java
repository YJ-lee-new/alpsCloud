package com.alps.kitting.api.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("pass/fail/swap参数mode")
@Data
public class SaveModel {
	
	@ApiModelProperty(required = true)
	private String serialNo;
	
	@ApiModelProperty(notes = "kitting1必填，其他工站不必填")
	private String batchNo;
	
	@ApiModelProperty(required = true)
	private String kitStation;
	
	@ApiModelProperty(required = true)
	private String line;
	
	private String nextevent;
	
	private String mainPartNo;
	
	private String mac;
	
	private String assetNo;
	
	private String skuNo;
	
	@ApiModelProperty(value = "Fail按钮此栏位必填,")
	private String errorType;
	
	@ApiModelProperty(value = "saveerror将3个栏位值合并传进来，必填")
	private String errorDesc;
	
	private String createBy;
	

}
