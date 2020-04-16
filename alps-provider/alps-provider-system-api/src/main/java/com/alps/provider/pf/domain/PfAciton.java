package com.alps.provider.pf.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.alps.common.core.domain.BaseEntity;

/**
 * action表 pf_aciton
 * 
 * @author leopards
 * @date 2020-03-19
 */
public class PfAciton extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** id */
	private String actionId;
	/** 事件/action名称 */
	private String actionCode;
	/** 事件的名字，支持国际化 */
	private String actionNameEn;
	/**  */
	private String actionNameCn;
	/**  */
	private String actionNameTw;
	/**  */
	private String actionDesc;
	/**  */
	private String apiId;
	/** 是否有效 */
	private Integer status;

	public void setActionId(String actionId) 
	{
		this.actionId = actionId;
	}

	public String getActionId() 
	{
		return actionId;
	}
	public void setActionCode(String actionCode) 
	{
		this.actionCode = actionCode;
	}

	public String getActionCode() 
	{
		return actionCode;
	}
	public void setActionNameEn(String actionNameEn) 
	{
		this.actionNameEn = actionNameEn;
	}

	public String getActionNameEn() 
	{
		return actionNameEn;
	}
	public void setActionNameCn(String actionNameCn) 
	{
		this.actionNameCn = actionNameCn;
	}

	public String getActionNameCn() 
	{
		return actionNameCn;
	}
	public void setActionNameTw(String actionNameTw) 
	{
		this.actionNameTw = actionNameTw;
	}

	public String getActionNameTw() 
	{
		return actionNameTw;
	}
	public void setActionDesc(String actionDesc) 
	{
		this.actionDesc = actionDesc;
	}

	public String getActionDesc() 
	{
		return actionDesc;
	}
	public void setApiId(String apiId) 
	{
		this.apiId = apiId;
	}

	public String getApiId() 
	{
		return apiId;
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
            .append("actionId", getActionId())
            .append("actionCode", getActionCode())
            .append("actionNameEn", getActionNameEn())
            .append("actionNameCn", getActionNameCn())
            .append("actionNameTw", getActionNameTw())
            .append("actionDesc", getActionDesc())
            .append("apiId", getApiId())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
