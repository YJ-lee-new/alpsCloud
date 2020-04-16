package com.alps.kitting.api.domain;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel(description = "错误mode")
@Data
public class ErrorModel {
	
	private String errorCode;
	
	private String descs;
	
	private String L10Descs;

}
