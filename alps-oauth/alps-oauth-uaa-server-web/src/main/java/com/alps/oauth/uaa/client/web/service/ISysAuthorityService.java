package com.alps.oauth.uaa.client.web.service;

import com.alps.base.api.model.entity.SysAuthority;
import com.alps.base.api.model.entity.SysMenu;

import java.util.List;

/**
 * 系统权限管理
 *
 * @author YJ.lee
 */
public interface ISysAuthorityService extends IBaseService<SysAuthority> {

    /**
     * 获取菜单权限列表
     *
     * @param status
     * @return
     */
	  public List<SysMenu> findAuthorityMenu(String account,String companyId);
    
    
    /**
     * 获取API权限列表
     *
     * @param serviceId
     * @return
     */
    List<SysAuthority> findAuthorityApiList(String userName);


}
