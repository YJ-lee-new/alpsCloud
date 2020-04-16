package com.alps.provider.pf.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.alps.common.core.domain.BaseEntity;

/**
 * pf_logic表 pf_logic
 * 
 * @author leopards
 * @date 2020-03-19
 */
public class PfLogic extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** 逻辑ID */
	private String logicId;
	/** 逻辑接口或者API */
	private String logicUrl;
	/** 接口传入参数 */
	private String urlInput;
	/** 接口传出参数 */
	private String urlOutput;
	/** 接口传输方式 */
	private String urlTransport;
	/** 逻辑群组 */
	private String logicGroup;
	/**  */
	private String logicName;
	/** 逻辑描述 */
	private String logicDesc;

	public void setLogicId(String logicId) 
	{
		this.logicId = logicId;
	}

	public String getLogicId() 
	{
		return logicId;
	}
	public void setLogicUrl(String logicUrl) 
	{
		this.logicUrl = logicUrl;
	}

	public String getLogicUrl() 
	{
		return logicUrl;
	}
	public void setUrlInput(String urlInput) 
	{
		this.urlInput = urlInput;
	}

	public String getUrlInput() 
	{
		return urlInput;
	}
	public void setUrlOutput(String urlOutput) 
	{
		this.urlOutput = urlOutput;
	}

	public String getUrlOutput() 
	{
		return urlOutput;
	}
	public void setUrlTransport(String urlTransport) 
	{
		this.urlTransport = urlTransport;
	}

	public String getUrlTransport() 
	{
		return urlTransport;
	}
	public void setLogicGroup(String logicGroup) 
	{
		this.logicGroup = logicGroup;
	}

	public String getLogicGroup() 
	{
		return logicGroup;
	}
	public void setLogicName(String logicName) 
	{
		this.logicName = logicName;
	}

	public String getLogicName() 
	{
		return logicName;
	}
	public void setLogicDesc(String logicDesc) 
	{
		this.logicDesc = logicDesc;
	}

	public String getLogicDesc() 
	{
		return logicDesc;
	}

    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("logicId", getLogicId())
            .append("logicUrl", getLogicUrl())
            .append("urlInput", getUrlInput())
            .append("urlOutput", getUrlOutput())
            .append("urlTransport", getUrlTransport())
            .append("logicGroup", getLogicGroup())
            .append("logicName", getLogicName())
            .append("logicDesc", getLogicDesc())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
