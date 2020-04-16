package com.alps.provider.pf.domain;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.alps.common.core.domain.BaseEntity;

/**
 * pf_api表 pf_api
 * 
 * @author leopards
 * @date 2020-03-19
 */
public class PfApi extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** 逻辑流ID/API ID */
	private String apiId;
	/** 对外提供服务的appkey */
	private String appId;
	/** 具体逻辑流程/步骤 */
	private String apiName;
	/** 对外提供服务的URL */
	private String apiUrl;
	/** 逻辑流描述/API描述 */
	private String apiDesc;
	/** API种类 */
	private String apiCategory;
	/** 是否有效 */
	private Integer status;
	/** 方法名 */
	private String methodName;
	
	private String apiShow;

	/**
	 * @return the apiShow
	 */
	public String getApiShow() {
		return apiShow;
	}

	/**
	 * @param apiShow the apiShow to set
	 */
	public void setApiShow(String apiShow) {
		this.apiShow = apiShow;
	}

	public void setApiId(String apiId) 
	{
		this.apiId = apiId;
	}

	public String getApiId() 
	{
		return apiId;
	}
	public void setAppId(String appId) 
	{
		this.appId = appId;
	}

	public String getAppId() 
	{
		return appId;
	}
	public void setApiName(String apiName) 
	{
		this.apiName = apiName;
	}

	public String getApiName() 
	{
		return apiName;
	}
	public void setApiUrl(String apiUrl) 
	{
		this.apiUrl = apiUrl;
	}

	public String getApiUrl() 
	{
		return apiUrl;
	}
	public void setApiDesc(String apiDesc) 
	{
		this.apiDesc = apiDesc;
	}

	public String getApiDesc() 
	{
		return apiDesc;
	}
	public void setApiCategory(String apiCategory) 
	{
		this.apiCategory = apiCategory;
	}

	public String getApiCategory() 
	{
		return apiCategory;
	}
	public void setStatus(Integer status) 
	{
		this.status = status;
	}

	public Integer getStatus() 
	{
		return status;
	}
	public void setMethodName(String methodName) 
	{
		this.methodName = methodName;
	}

	public String getMethodName() 
	{
		return methodName;
	}

    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("apiId", getApiId())
            .append("appId", getAppId())
            .append("apiName", getApiName())
            .append("apiUrl", getApiUrl())
            .append("apiDesc", getApiDesc())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("apiCategory", getApiCategory())
            .append("status", getStatus())
            .append("methodName", getMethodName())
            .toString();
    }
}
