package com.alps.oauth.uaa.client.web.service;

import java.util.List;

import com.alps.base.api.model.entity.SysCompany;

/**
 * 应用信息管理
 *
 * @author YJ.lee
 */
public interface ISysCompanyService extends IBaseService<SysCompany> {
	public List<SysCompany> selectSysAppList(SysCompany sysCompany);
}
