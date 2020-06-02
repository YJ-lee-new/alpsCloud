package com.alps.base.api.model.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.alps.common.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
/**
 * 
* <p>Title: PfActionRef</p>  

* <p>Description: </p>  

* @author YJ.lee  

* @date 2020年5月20日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@TableName("sys_authority")
public class SysAuthority extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/**  */
    @TableId( value = "id", type = IdType.AUTO)
	private Long id;
	/**  */
	private String companyId;
	/** 租户下的角色 */
	private Long roleId;
	/** 菜单 */
	private Long menuId;
	/** 事件功能ID */
	private Long apiId;
	/** API对应的path */
	private String  path;
	/**  */
	private Integer status;
}
