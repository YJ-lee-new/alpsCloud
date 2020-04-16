package com.alps.base.api.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="逻辑流程实体信息")
@Data
public class SysLogicflow implements Serializable{

	/**
	 *@author  wendy
	 *@date 2019.12.28
	 *@description sys_logicflow表 
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@TableId(type=IdType.UUID)
	@ApiModelProperty(value="自动生成UUID",required=false)
	private String logicFlowId;
	@ApiModelProperty(value="logicFlow名称",required=true)
	private String logicFlowName;
	@ApiModelProperty(value="logicFlow的URL",required=true)
	private String logicFlowUrl;
	@ApiModelProperty(value="logicFlow描述",required=false)
	private String logicFlowDesc;
	@ApiModelProperty(value="logicgroup,来源逻辑单元的logicgroup",required=true)
	private String logicGroup;
	@ApiModelProperty(value="自动生成UUID",required=false)
	private String createBy;
	private Date createTime;

}
