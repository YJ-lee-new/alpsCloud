package com.alps.base.api.domain;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="function实体对象")
@Data
public class SysFunctionMenu implements Serializable{
	/**
	 *@author  wendy
	 *@date 2019.12.28
	 *@description sys_function_menu表 
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value="自动生成UUID",required=false)
	@TableId(type=IdType.UUID)
	private String id;
	@ApiModelProperty(value="function名称",required=true)
	private String functionName;
	@ApiModelProperty(value="menuId",required=true)
	private String menuId;
	@ApiModelProperty(value="0：逻辑单元，1：逻辑流程",required=true)
	private String logicType;
	@ApiModelProperty(value="逻辑单元id或者逻辑流程id",required=true)
	private String functionLogicId;
	@ApiModelProperty(value="登录者",required=false)
	private String createBy;
	@ApiModelProperty(value="创建时间",required=false)
	private String createTime;
	
	
	

}
