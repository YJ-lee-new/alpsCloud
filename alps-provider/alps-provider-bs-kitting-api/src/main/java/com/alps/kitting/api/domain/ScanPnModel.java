package com.alps.kitting.api.domain;

import java.util.Date;

import lombok.Data;

@Data
public class ScanPnModel {

	
	
	private String partNo;
	
	private String actSN;
	
	private Date updateTime;
	
	private int finishQty;
	
	private String error;
}
