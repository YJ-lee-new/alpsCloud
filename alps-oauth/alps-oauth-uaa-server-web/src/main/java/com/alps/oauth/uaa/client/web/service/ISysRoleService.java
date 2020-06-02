package com.alps.oauth.uaa.client.web.service;

import com.alps.base.api.model.entity.SysRole;
import com.alps.base.api.model.entity.SysRoleUser;
import com.alps.oauth.uaa.client.web.model.PageParams;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 角色管理
 *
 * @author YJ.lee
 */
public interface ISysRoleService extends IBaseService<SysRole> {

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    IPage<SysRole> findListPage(PageParams pageParams);

    /**
     * 查询列表
     *
     * @return
     */
    List<SysRole> findAllList(SysRole role);

    /**
     * 获取角色信息
     *
     * @param roleId
     * @return
     */
    SysRole getRole(Long roleId);

    /**
     * 添加角色
     *
     * @param role 角色
     * @return
     */
    SysRole addRole(SysRole role);
    /**
     * 添加角色
     *
     * @param role 角色
     * @return
     */
    int insertRole(SysRole role);

    /**
     * 更新角色
     *
     * @param role 角色
     * @return
     */
    int updateRole(SysRole role);

    /**
     * 删除角色
     *
     * @param roleId 角色ID
     * @return
     */
    int removeRole(String roleIdstr);

    /**
     * 检测角色编码是否存在
     *
     * @param roleCode
     * @return
     */
    Boolean isExist(String roleCode);

    /**
     * 用户添加角色
     *
     * @param userId
     * @param roles
     * @return
     */
    void saveUserRoles(Long userId, String... roles);

    /**
     * 角色添加成员
     *
     * @param roleId
     * @param userIds
     */
    void saveRoleUsers(Long roleId, String... userIds);

    /**
     * 查询角色成员
     * @return
     */
    List<SysRoleUser> findRoleUsers(Long roleId);

    /**
     * 获取角色所有授权组员数量
     *
     * @param roleId
     * @return
     */
    int getCountByRole(Long roleId);

    /**
     * 获取组员角色数量
     *
     * @param userId
     * @return
     */
    int getCountByUser(Long userId);

    /**
     * 移除角色所有组员
     *
     * @param roleId
     * @return
     */
    void removeRoleUsers(Long roleId);

    /**
     * 移除组员的所有角色
     *
     * @param userId
     * @return
     */
    void removeUserRoles(Long userId);

    /**
     * 检测是否存在
     *
     * @param userId
     * @param roleId
     * @return
     */
    Boolean isExist(Long userId, Long roleId);

    /**
     * 获取用户角色列表
     *
     * @param userId
     * @return
     */
    List<SysRole> getUserRoles(Long userId);

    /**
     * 获取用户角色ID列表
     *
     * @param userId
     * @return
     */
    List<Long> getUserRoleIds(Long userId);
    
    /**
     * 批量选择授权用户角色
     * 
     * @param roleId 角色ID
     * @param userIds 需要删除的用户数据ID
     * @return 结果
     */
    public int insertRoleUsers(Long roleId, String userIds);
    
    /**
     * 取消授权用户角色
     * 
     * @param userRole 用户和角色关联信息
     * @return 结果
     */
    public int deleteAuthUser(SysRoleUser userRole);

    /**
     * 批量取消授权用户角色
     * 
     * @param roleId 角色ID
     * @param userIds 需要删除的用户数据ID
     * @return 结果
     */
    public int deleteAuthUsers(Long roleId, String userIds);
    
}
