package com.alps.oauth.server.service;

import com.alps.base.api.model.Authority2Api;
import com.alps.base.api.model.AuthorityApi;
import com.alps.base.api.model.AuthorityMenu;
import com.alps.base.api.model.AuthorityResource;
import com.alps.base.api.model.entity.BaseAuthority;
import com.alps.base.api.model.entity.BaseAuthorityAction;
import com.alps.common.constant.ResourceType;
import com.alps.common.oauth2.security.AlpsAuthority;
import com.alps.oauth.server.extra.IBaseService;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 系统权限管理
 *
 * @author YJ.lee
 */
public interface AuthorityService extends IBaseService<BaseAuthority> {

    /**
     * 获取访问权限列表
     *
     * @return
     */
    List<AuthorityResource> findAuthorityResource();

    /**
     * 获取菜单权限列表
     *
     * @param status
     * @return
     */
    List<AuthorityMenu> findAuthorityMenu(Integer status);


    /**
     * 获取API权限列表
     *
     * @param serviceId
     * @return
     */
    List<AuthorityApi> findAuthorityApi(String serviceId);
    
    
    /**
     * 获取API权限列表
     *
     * @param serviceId
     * @return
     */
    List<Authority2Api> findAuthorityApiList(String userName);


    /**
     * 查询功能按钮权限列表
     *
     * @param actionId
     * @return
     */
    List<BaseAuthorityAction> findAuthorityAction(Long actionId);


    /**
     * 保存或修改权限
     *
     * @param resourceId
     * @param resourceType
     * @return 权限Id
     */
    BaseAuthority saveOrUpdateAuthority(Long resourceId, ResourceType resourceType);


    /**
     * 获取权限
     *
     * @param resourceId
     * @param resourceType
     * @return
     */
    BaseAuthority getAuthority(Long resourceId, ResourceType resourceType);

    /**
     * 移除权限
     *
     * @param resourceId
     * @param resourceType
     * @return
     */
    void removeAuthority(Long resourceId, ResourceType resourceType);

    /**
     * 移除应用权限
     *
     * @param appId
     */
    void removeAuthorityApp(String appId);


    /**
     * 移除功能按钮权限
     * @param actionId
     */
    void  removeAuthorityAction(Long actionId);

    /**
     * 是否已被授权
     *
     * @param resourceId
     * @param resourceType
     * @return
     */
    Boolean isGranted(Long resourceId, ResourceType resourceType);

    /**
     * 角色授权
     *
     * @param
     * @param roleId       角色ID
     * @param expireTime   过期时间,null表示长期,不限制
     * @param authorityIds 权限集合
     * @return 权限标识
     */
    void addAuthorityRole(Long roleId, Date expireTime, String... authorityIds);


    /**
     * 用户授权
     *
     * @param
     * @param userId       用户ID
     * @param expireTime   过期时间,null表示长期,不限制
     * @param authorityIds 权限集合
     * @return 权限标识
     */
    void addAuthorityUser(Long userId, Date expireTime, String... authorityIds);


    /**
     * 应用授权
     *
     * @param
     * @param appId        应用ID
     * @param expireTime   过期时间,null表示长期,不限制
     * @param authorityIds 权限集合
     * @return
     */
    void addAuthorityApp(String appId, Date expireTime, String... authorityIds);

    /**
     * 应用授权-添加单个权限
     *
     * @param appId
     * @param expireTime
     * @param authorityId
     */
    void addAuthorityApp(String appId, Date expireTime, String authorityId);

    /**
     * 添加功能按钮权限
     *
     * @param actionId
     * @param authorityIds
     * @return
     */
    void addAuthorityAction(Long actionId, String... authorityIds);

    /**
     * 获取应用已授权权限
     *
     * @param appId
     * @return
     */
    List<AlpsAuthority> findAuthorityByApp(String appId);

    /**
     * 获取角色已授权权限
     *
     * @param roleId
     * @return
     */
    List<AlpsAuthority> findAuthorityByRole(Long roleId);

    /**
     * 获取用户已授权权限
     *
     * @param userId
     * @param root
     * @return
     */
    List<AlpsAuthority> findAuthorityByUser(Long userId, Boolean root);

    /**
     * 获取开放对象权限
     *
     * @param type = null 查询全部  type = 1 获取菜单和操作 type = 2 获取API
     * @return
     */
    List<AlpsAuthority> findAuthorityByType(String type);

    /**
     * 获取用户已授权权限详情
     *
     * @param userId
     * @param root
     * @return
     */
    List<AuthorityMenu> findAuthorityMenuByUser(Long userId, Boolean root);

    /**
     * 检测全是是否被多个角色授权
     *
     * @param authorityId
     * @param roleIds
     * @return
     */
    Boolean isGrantedByRoleIds(String authorityId, Long... roleIds);


    /**
     * 清理无效权限
     * @param serviceId
     * @param codes
     */
    void clearInvalidApi(String serviceId,Collection<String> codes);
}
