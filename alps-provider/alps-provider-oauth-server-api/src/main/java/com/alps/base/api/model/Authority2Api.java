package com.alps.base.api.model;

import com.alps.base.api.model.entity.BaseApi;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Objects;
/**
 * @author:Yujie.lee
 * Date:2019年12月7日
 * Todo API权限例表
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Authority2Api implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    //companyId
	private String companyId;
	/**
	 * @return the companyId
	 */
	public String getCompanyId() {
		return companyId;
	}

	/**
	 * @param companyId the companyId to set
	 */
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	//UserName
	private String userName;
	
	//role_id
	private Long roleId;
	
	//menu_id
	private Long menuId;
	
	//api_url
	private String apiUrl;
    
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the roleId
	 */
	public Long getRoleId() {
		return roleId;
	}

	/**
	 * @param roleId the roleId to set
	 */
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	/**
	 * @return the menuId
	 */
	public Long getMenuId() {
		return menuId;
	}

	/**
	 * @param menuId the menuId to set
	 */
	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}

	/**
	 * @return the apiUrl
	 */
	public String getApiUrl() {
		return apiUrl;
	}

	/**
	 * @param apiUrl the apiUrl to set
	 */
	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}

}
