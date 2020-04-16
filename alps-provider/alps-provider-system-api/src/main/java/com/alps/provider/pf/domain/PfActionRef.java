package com.alps.provider.pf.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.alps.common.core.domain.BaseEntity;

/**
 * pf_action_ref表 pf_action_ref
 * 
 * @author leopards
 * @date 2020-03-19
 */
public class PfActionRef extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/**  */
	private String id;
	/**  */
	private String companyId;
	/** 租户下的角色 */
	private Long roleId;
	/** 菜单 */
	private Long menuId;
	/** 事件功能ID */
	private String actionId;
	/**  */
	private Integer status;

	public void setId(String id) 
	{
		this.id = id;
	}

	public String getId() 
	{
		return id;
	}
	public void setCompanyId(String companyId) 
	{
		this.companyId = companyId;
	}

	public String getCompanyId() 
	{
		return companyId;
	}
	public void setRoleId(Long roleId) 
	{
		this.roleId = roleId;
	}

	public Long getRoleId() 
	{
		return roleId;
	}
	public void setMenuId(Long menuId) 
	{
		this.menuId = menuId;
	}

	public Long getMenuId() 
	{
		return menuId;
	}
	public void setActionId(String actionId) 
	{
		this.actionId = actionId;
	}

	public String getActionId() 
	{
		return actionId;
	}
	public void setStatus(Integer status) 
	{
		this.status = status;
	}

	public Integer getStatus() 
	{
		return status;
	}

    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("companyId", getCompanyId())
            .append("roleId", getRoleId())
            .append("menuId", getMenuId())
            .append("actionId", getActionId())
            .append("status", getStatus())
            .toString();
    }
}
