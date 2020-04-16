package com.alps.provider.pf.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.alps.common.core.domain.BaseEntity;

/**
 * pf_app表 pf_app
 * 
 * @author leopards
 * @date 2020-03-19
 */
public class PfApp extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** 客户端ID */
	private String appId;
	/** API访问key */
	private String appKey;
	/** API访问密钥 */
	private String secretKey;
	/** app名称 */
	private String appName;
	/** app英文名称 */
	private String appNameEn;
	/** 应用图标 */
	private String appIcon;
	/** app类型:server-服务应用 app-手机应用 pc-PC网页应用 wap-手机网页应用 */
	private String appType;
	/** app描述 */
	private String appDesc;
	/** 移动应用操作系统:ios-苹果 android-安卓 */
	private String appOs;
	/** 官网地址 */
	private String website;
	/** 开发者ID:默认为0 */
	private Long developerId;
	/** 状态:0-无效 1-有效 */
	private Integer status;
	/** 保留数据0-否 1-是 不允许删除 */
	private Integer isPersist;

	public void setAppId(String appId) 
	{
		this.appId = appId;
	}

	public String getAppId() 
	{
		return appId;
	}
	public void setAppKey(String appKey) 
	{
		this.appKey = appKey;
	}

	public String getAppKey() 
	{
		return appKey;
	}
	public void setSecretKey(String secretKey) 
	{
		this.secretKey = secretKey;
	}

	public String getSecretKey() 
	{
		return secretKey;
	}
	public void setAppName(String appName) 
	{
		this.appName = appName;
	}

	public String getAppName() 
	{
		return appName;
	}
	public void setAppNameEn(String appNameEn) 
	{
		this.appNameEn = appNameEn;
	}

	public String getAppNameEn() 
	{
		return appNameEn;
	}
	public void setAppIcon(String appIcon) 
	{
		this.appIcon = appIcon;
	}

	public String getAppIcon() 
	{
		return appIcon;
	}
	public void setAppType(String appType) 
	{
		this.appType = appType;
	}

	public String getAppType() 
	{
		return appType;
	}
	public void setAppDesc(String appDesc) 
	{
		this.appDesc = appDesc;
	}

	public String getAppDesc() 
	{
		return appDesc;
	}
	public void setAppOs(String appOs) 
	{
		this.appOs = appOs;
	}

	public String getAppOs() 
	{
		return appOs;
	}
	public void setWebsite(String website) 
	{
		this.website = website;
	}

	public String getWebsite() 
	{
		return website;
	}
	public void setDeveloperId(Long developerId) 
	{
		this.developerId = developerId;
	}

	public Long getDeveloperId() 
	{
		return developerId;
	}
	public void setStatus(Integer status) 
	{
		this.status = status;
	}

	public Integer getStatus() 
	{
		return status;
	}
	public void setIsPersist(Integer isPersist) 
	{
		this.isPersist = isPersist;
	}

	public Integer getIsPersist() 
	{
		return isPersist;
	}

    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("appId", getAppId())
            .append("appKey", getAppKey())
            .append("secretKey", getSecretKey())
            .append("appName", getAppName())
            .append("appNameEn", getAppNameEn())
            .append("appIcon", getAppIcon())
            .append("appType", getAppType())
            .append("appDesc", getAppDesc())
            .append("appOs", getAppOs())
            .append("website", getWebsite())
            .append("developerId", getDeveloperId())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("status", getStatus())
            .append("isPersist", getIsPersist())
            .toString();
    }
}
