package com.alps.oauth.uaa.client.web.serviceImp;

import com.alps.common.constant.BaseConstants;
import com.alps.common.constant.UserConstants;
import com.alps.common.core.text.Convert;
import com.alps.base.api.model.entity.SysApi;
import com.alps.base.api.model.entity.SysAuthority;
import com.alps.base.api.model.entity.SysMenu;
import com.alps.base.api.model.entity.SysRole;
import com.alps.base.api.model.entity.SysRoleUser;
import com.alps.base.api.model.entity.SysUser;
import com.alps.common.oauth2.config.CommonConstants;
import com.alps.common.oauth2.exception.AlpsAlertException;
import com.alps.common.oauth2.utils.StringUtils;
import com.alps.oauth.uaa.client.web.mapper.SysApiMapper;
import com.alps.oauth.uaa.client.web.mapper.SysAuthorityMapper;
import com.alps.oauth.uaa.client.web.mapper.SysMenuMapper;
import com.alps.oauth.uaa.client.web.mapper.SysRoleMapper;
import com.alps.oauth.uaa.client.web.mapper.SysRoleUserMapper;
import com.alps.oauth.uaa.client.web.model.PageParams;
import com.alps.oauth.uaa.client.web.service.ISysRoleService;
import com.alps.oauth.uaa.client.web.service.ISysUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author YJ.lee
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysRoleServiceImpl extends BaseServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysAuthorityMapper sysAuthorityMapper;
    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;
    @Autowired
    private SysMenuMapper sysMenuMapper;
    @Autowired
    private SysApiMapper sysApiMapper;
    @Autowired
    private ISysUserService userService;

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    @Override
    public IPage<SysRole> findListPage(PageParams pageParams) {
        SysRole query = pageParams.mapToObject(SysRole.class);
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .likeRight(ObjectUtils.isNotEmpty(query.getRoleCode()),SysRole::getRoleCode, query.getRoleCode())
                .likeRight(ObjectUtils.isNotEmpty(query.getRoleName()),SysRole::getRoleName, query.getRoleName());
        queryWrapper.orderByDesc("create_time");
        return sysRoleMapper.selectPage(pageParams,queryWrapper);
    }

    /**
     * 查询列表
     *
     * @return
     */
    @Override
    public List<SysRole> findAllList(SysRole role) {
        List<SysRole> list = sysRoleMapper.selectRoleList(role);
        return list;
    }

    /**
     * 获取角色信息
     *
     * @param roleId
     * @return
     */
    @Override
    public SysRole getRole(Long roleId) {
        return sysRoleMapper.selectById(roleId);
    }

    /**
     * 添加角色
     *
     * @param role 角色
     * @return
     */
    @Override
    public SysRole addRole(SysRole role) {
        if (isExist(role.getRoleCode())) {
            throw new AlpsAlertException(String.format("%s编码已存在!", role.getRoleCode()));
        }
        if (role.getStatus() == null) {
            role.setStatus(BaseConstants.ENABLED);
        }
        if (role.getDelFlag() == null) {
            role.setDelFlag(BaseConstants.DISABLED);
        }
        role.setCreateTime(new Date());
        role.setUpdateTime(role.getCreateTime());
        sysRoleMapper.insert(role);
        return role;
    }
    
    /**
     * 添加角色
     *
     * @param role 角色
     * @return
     */
    @Override
    public int insertRole(SysRole role) {
    	if (isExist(role.getRoleCode())) {
    		throw new AlpsAlertException(String.format("%s编码已存在!", role.getRoleCode()));
    	}
    	if (role.getStatus() == null) {
    		role.setStatus(BaseConstants.ENABLED);
    	}
    	if (role.getDelFlag() == null) {
    		role.setDelFlag(BaseConstants.DISABLED);
    	}
    	role.setCreateTime(new Date());
    	role.setUpdateTime(role.getCreateTime());
    	sysRoleMapper.insert(role);
    	return insertRoleGateway(role);
    }
    
    public int insertRoleGateway(SysRole role) {
    	int rows = 1;
    	//增加之前统一选清空之前的权限.
    	sysAuthorityMapper.deleteByRoleId(role.getRoleId());
        List<SysAuthority> list = new ArrayList<SysAuthority>();
        String  authorityTpye  = role.getAuthorityTpye();
        SysMenu sysmenu = null;
        SysApi sysapi = null;
        for (Long menuId : role.getMenuIds())
        {
        	SysAuthority sa = new SysAuthority();
        	sa.setCompanyId(role.getCompanyId());
        	sa.setRoleId(role.getRoleId());
        	if(UserConstants.RBAC_AUTHORITY.contains(authorityTpye)) {
        		sysmenu = sysMenuMapper.selectMenuPathById(menuId);
        		if(sysmenu != null && UserConstants.SYS_ACTION.contains(sysmenu.getMenuType())){
        			sa.setMenuId(menuId);
        			sa.setApiId(sysmenu.getApiId());
        			sa.setPath(sysmenu.getUrl());
        			list.add(sa);
        		}
        		
        	}else if(UserConstants.API_AUTHORITY.contains(authorityTpye)) {
        		sysapi = sysApiMapper.selectById(menuId);
        		if(sysapi != null) {
        			sa.setApiId(sysapi.getApiId());
        			sa.setPath(sysapi.getPath());
        			list.add(sa);
        		}
        	}
        }
        if (list.size() > 0)
        {
            rows = sysAuthorityMapper.batchRoleGateway(list);
        }
        return rows;
    }
    

    /**
     * 更新角色
     *
     * @param role 角色
     * @return
     */
    @Override
    public int updateRole(SysRole role) {
        role.setUpdateTime(new Date());
        sysRoleMapper.updateById(role);
        return insertRoleGateway(role);
    }

    
    /**
     * 删除角色
     *
     * @param roleId 角色ID
     * @return
     */
    @Override
    public int removeRole(String roleIdstr) {
    	Long[] roleIds = Convert.toLongArray(roleIdstr);
        for (Long roleId : roleIds)
        {
            SysRole role = getRole(roleId);
            if (role != null && role.getDelFlag().equals(BaseConstants.ENABLED)) {
                throw new AlpsAlertException(String.format("保留数据,不允许删除"));
            }
            int count = getCountByRole(roleId);
        	
            if (count > 0) {
                throw new AlpsAlertException("该角色下存在授权人员,不允许删除!");
            }
        }
        return sysRoleMapper.deleteRoleByIds(roleIds);
    }

    /**
     * 检测角色编码是否存在
     *
     * @param roleCode
     * @return
     */
    @Override
    public Boolean isExist(String roleCode) {
        if (StringUtils.isBlank(roleCode)) {
            throw new AlpsAlertException("roleCode不能为空!");
        }
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(SysRole::getRoleCode, roleCode);
        queryWrapper.lambda().eq(SysRole::getDelFlag, "0");
        return sysRoleMapper.selectCount(queryWrapper) > 0;
    }

    /**
     * 用户添加角色
     *
     * @param userId
     * @param roles
     * @return
     */
    @Override
    public void saveUserRoles(Long userId, String... roles) {
        if (userId == null || roles == null) {
            return;
        }
        SysUser user = userService.selectUserById(userId);
        if (user == null) {
            return;
        }
        if (CommonConstants.ROOT.equals(user.getUserName())) {
            throw new AlpsAlertException("默认用户无需分配!");
        }
        // 先清空,在添加
        removeUserRoles(userId);
        if (roles.length > 0) {
            for (String roleId : roles) {
                SysRoleUser roleUser = new SysRoleUser();
                roleUser.setUserId(userId);
                roleUser.setRoleId(Long.parseLong(roleId));
                sysRoleUserMapper.insert(roleUser);
            }
            // 批量保存
        }
    }

    /**
     * 角色添加成员
     *
     * @param roleId
     * @param userIds
     */
    @Override
    public void saveRoleUsers(Long roleId, String... userIds) {
        if (roleId == null || userIds == null) {
            return;
        }
        // 先清空,在添加
        removeRoleUsers(roleId);
        if (userIds.length > 0) {
            for (String userId : userIds) {
                SysRoleUser roleUser = new SysRoleUser();
                roleUser.setUserId(Long.parseLong(userId));
                roleUser.setRoleId(roleId);
                sysRoleUserMapper.insert(roleUser);
            }
            // 批量保存
        }
    }

    /**
     * 查询角色成员
     *
     * @return
     */
    @Override
    public List<SysRoleUser> findRoleUsers(Long roleId) {
        QueryWrapper<SysRoleUser> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(SysRoleUser::getRoleId, roleId);
        return sysRoleUserMapper.selectList(queryWrapper);
    }

    /**
     * 获取角色所有授权组员数量
     *
     * @param roleId
     * @return
     */
    @Override
    public int getCountByRole(Long roleId) {
        QueryWrapper<SysRoleUser> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(SysRoleUser::getRoleId, roleId);
        int result = sysRoleUserMapper.selectCount(queryWrapper);
        return result;
    }

    /**
     * 获取组员角色数量
     *
     * @param userId
     * @return
     */
    @Override
    public int getCountByUser(Long userId) {
        QueryWrapper<SysRoleUser> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(SysRoleUser::getUserId, userId);
        int result = sysRoleUserMapper.selectCount(queryWrapper);
        return result;
    }

    /**
     * 移除角色所有组员
     *
     * @param roleId
     * @return
     */
    @Override
    public void removeRoleUsers(Long roleId) {
        QueryWrapper<SysRoleUser> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(SysRoleUser::getRoleId, roleId);
        sysRoleUserMapper.delete(queryWrapper);
    }

    /**
     * 移除组员的所有角色
     *
     * @param userId
     * @return
     */
    @Override
    public void removeUserRoles(Long userId) {
        QueryWrapper<SysRoleUser> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(SysRoleUser::getUserId, userId);
        sysRoleUserMapper.delete(queryWrapper);
    }

    /**
     * 检测是否存在
     *
     * @param userId
     * @param roleId
     * @return
     */
    @Override
    public Boolean isExist(Long userId, Long roleId) {
        QueryWrapper<SysRoleUser> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(SysRoleUser::getRoleId, roleId);
        queryWrapper.lambda().eq(SysRoleUser::getUserId, userId);
        sysRoleUserMapper.delete(queryWrapper);
        int result = sysRoleUserMapper.selectCount(queryWrapper);
        return result > 0;
    }


    /**
     * 获取组员角色
     *
     * @param userId
     * @return
     */
    @Override
    public List<SysRole> getUserRoles(Long userId) {
    	
    	List<SysRole> userRoles = sysRoleUserMapper.selectRoleUserList(userId);
        List<SysRole> roles = sysRoleMapper.selectRoleList(new SysRole());
        for (SysRole role : roles)
        {
            for (SysRole userRole : userRoles)
            {
                if (role.getRoleId().longValue() == userRole.getRoleId().longValue())
                {
                	role.setSelectflag(true);
                    break;
                }
            }
        }
        return roles;
        
    }

    /**
     * 获取用户角色ID列表
     *
     * @param userId
     * @return
     */
    @Override
    public List<Long> getUserRoleIds(Long userId) {
        return sysRoleUserMapper.selectRoleUserIdList(userId);
    }
    public int insertRoleUsers(Long roleId, String userIds)
    {
        Long[] users = Convert.toLongArray(userIds);
        // 新增用户与角色管理
        List<SysRoleUser> list = new ArrayList<SysRoleUser>();
        for (Long userId : users)
        {
        	SysRoleUser ur = new SysRoleUser();
            ur.setUserId(userId);
            ur.setRoleId(roleId);
            list.add(ur);
        }
        return sysRoleUserMapper.batchUserRole(list);
    }
    
    
    
    /**
     * 取消授权用户角色
     * 
     * @param userRole 用户和角色关联信息
     * @return 结果
     */
    @Override
    public int deleteAuthUser(SysRoleUser userRole) {
    	return sysRoleUserMapper.deleteUserRoleInfo(userRole);
    }

    /**
     * 批量取消授权用户角色
     * 
     * @param roleId 角色ID
     * @param userIds 需要删除的用户数据ID
     * @return 结果
     */
    @Override
    public int deleteAuthUsers(Long roleId, String userIds) {
    	return sysRoleUserMapper.deleteUserRoleInfos(roleId, Convert.toLongArray(userIds));
    }

}
