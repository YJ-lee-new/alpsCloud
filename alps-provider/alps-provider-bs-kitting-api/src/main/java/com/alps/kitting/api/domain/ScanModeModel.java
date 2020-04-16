package com.alps.kitting.api.domain;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel(description = "ScanMode")
@Data
public class ScanModeModel {
	
	private String name;
	
	private String value;

}
