package com.alps.kitting.api.domain;

import java.util.Map;

import lombok.Data;

@Data
public class WOMaster {
	private String batchNo;
	
	private String workOrder;
	
	private String skuNo;

	private String currentStatus;
	
	private String UPCCode;
	
	private String JANCode;
	
	private int totalQty;
	
	private int finishQty;
	
	private Map<String,Object> params;
	
	private String createBy;
	
	private String createTime;
	
	private String error;
	
	private String finishStation;

}
