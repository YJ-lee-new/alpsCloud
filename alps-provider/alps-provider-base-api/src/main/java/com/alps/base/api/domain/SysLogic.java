package com.alps.base.api.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description="逻辑单元信息")
@Data
public class SysLogic implements Serializable{
	/**
	 *@author  wendy
	 *@date 2019.12.28
	 *@description sys_logic表 
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value="自动生成UUID")
	@TableId(type=IdType.UUID)
	private String logicId;
	@ApiModelProperty(value="logic名称",required=true)
	private String logicName;
	@ApiModelProperty(value="logic接口",required=true)
	private String logicUrl;
	@ApiModelProperty(value="input参数",required=true)
	private String urlInput;
	@ApiModelProperty(value="output参数",required=true)
	private String urlOutput;
	@ApiModelProperty(value="urlTransport",required=false)
	private String urlTransport;
	@ApiModelProperty(value="logic群组",required=true)
	private String logicGroup;
	@ApiModelProperty(value="logic描述",required=false)
	private String logicDesc;
	@ApiModelProperty(value="操作账号",required=false)
	private String createBy;
	@ApiModelProperty(value="当前时间",required=false)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date createTime;

}
