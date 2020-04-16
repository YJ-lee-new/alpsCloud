package com.alps.provider.pf.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.alps.common.core.domain.BaseEntity;

/**
 * pf_api_logic表 pf_api_logic
 * 
 * @author leopards
 * @date 2020-03-19
 */
public class PfApiLogic extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/**  */
	private String id;
	/**  */
	private String apiId;
	/**  */
	private String logicId;
	/**  */
	private Integer order;
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
	public void setApiId(String apiId) 
	{
		this.apiId = apiId;
	}

	public String getApiId() 
	{
		return apiId;
	}
	public void setLogicId(String logicId) 
	{
		this.logicId = logicId;
	}

	public String getLogicId() 
	{
		return logicId;
	}
	public void setOrder(Integer order) 
	{
		this.order = order;
	}

	public Integer getOrder() 
	{
		return order;
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
            .append("apiId", getApiId())
            .append("logicId", getLogicId())
            .append("order", getOrder())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .toString();
    }
}
