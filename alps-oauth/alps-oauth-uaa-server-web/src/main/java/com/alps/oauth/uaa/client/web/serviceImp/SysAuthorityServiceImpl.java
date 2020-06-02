package com.alps.oauth.uaa.client.web.serviceImp;

import com.alps.base.api.model.entity.SysAuthority;
import com.alps.base.api.model.entity.SysMenu;
import com.alps.oauth.uaa.client.web.mapper.SysAuthorityMapper;
import com.alps.oauth.uaa.client.web.service.ISysAuthorityService;
import com.alps.oauth.uaa.client.web.service.ISysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * 系统权限管理
 * 对菜单、操作、API等进行权限分配操作
 *
 * @author YJ.lee
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysAuthorityServiceImpl extends BaseServiceImpl<SysAuthorityMapper,SysAuthority > implements ISysAuthorityService {

    @Autowired
    private SysAuthorityMapper sysAuthorityMapper;
    @Autowired
    private ISysMenuService sysMenuService;

    @Value("${spring.application.name}")
    private String DEFAULT_SERVICE_ID;

    /**
     * 获取菜单权限列表
     *
     * @return
     */
    @Override
    public List<SysMenu> findAuthorityMenu(String account,String companyId) {
    	List<SysMenu> menuList = sysMenuService.selectMenuTreeByUserId( account, companyId);
        return menuList;

    }
	@Override
	public List<SysAuthority> findAuthorityApiList(String userName) {
		return    sysAuthorityMapper.findAuthorityApiList(userName);
	}
}
