package com.alps.kitting.api.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "扫描下阶料mode")
@Data
public class ScanPN implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(required = true)
	private String serialNo;
	
	@ApiModelProperty(notes = "kitting1必填，其他工站不必填")
	private String batchNo;
	
	@ApiModelProperty(required = true)
	private String kitStation;
	
	@ApiModelProperty(required = true)
	private String line;
	
	@ApiModelProperty(notes = "扫描的是料号必填")
	private String partNo;
	
	private String mainPartNo;
	
	private String needConfirm;
	
	@ApiModelProperty(notes = "包含参数needConfirm和数据字典CSN等")
	private List<ScanMode> scanValue;
	
	private String createBy;
	
	@ApiModelProperty(hidden = true)
	private Map<String,Object> map;
	
	public ScanPN() {
		
	}

@Data
public static class ScanMode {
			
	private  String name;
		
	private  String value;
	
	public ScanMode() {
		
	}
}

}
