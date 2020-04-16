package com.alps.base.api.domain;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description = "action实体")
@Data
public class PfAction implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value="自动生成UUID",required=false)
	private String actionId;
	@ApiModelProperty(value = "code",required = true)
	private String actionCode;
	@ApiModelProperty(value = "中文描述",required = true)
	private String actionNameCn;
	@ApiModelProperty(value = "apiId",required = true)
	private String apiId;
	@ApiModelProperty(value = "描述",required = false)
	private String actionDesc;
	@ApiModelProperty(value = "创建人",required = false)
	private String createBy;
	@ApiModelProperty(value = "创建时间",required = false)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date createTime;
	

}
