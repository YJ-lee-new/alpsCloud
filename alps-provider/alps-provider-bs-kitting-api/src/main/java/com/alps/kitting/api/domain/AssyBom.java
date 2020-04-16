package com.alps.kitting.api.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("pro_s_assy_bom")
public class AssyBom {
	
private String bomId;
	
	private String skuNo;
	
	private String skuRev;
	
	private String partNo;
	
	private String partRev;
	
	private String station;
	
	private String scanPriority;
	
	private String scanMode;
	
	private String replaceGroup;
	
	private int finishQty;
	
	private int qty;
	
	private String createBy;
	
	private Date createTime;
	
	private String updateBy;
	
	private Date updateTime;
	

}
