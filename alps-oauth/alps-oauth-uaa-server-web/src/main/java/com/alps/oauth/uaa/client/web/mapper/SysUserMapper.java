package com.alps.oauth.uaa.client.web.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.alps.base.api.model.entity.SysUser;
import com.alps.plaform.database.mapper.SuperMapper;

/**
 * @author YJ.lee
 */
@Repository
public interface SysUserMapper extends SuperMapper<SysUser> {

    /**
     * 根据条件分页查询用户列表
     * 
     * @param sysUser 用户信息
     * @return 用户信息集合信息
     */
    public List<SysUser> selectUserList(SysUser sysUser);
    
    /**
     * 通过用户ID查询用户
     * 
     * @param userId 用户ID
     * @return 用户对象信息
     */
    public SysUser selectUserById(Long userId);
    
    /**
     * 批量删除用户信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteUserByIds(Long[] ids);
    
    public List<SysUser> selectAuthorityUserList(SysUser sysUser);
    
    public List<SysUser> selectUnAuthorityUserList(SysUser sysUser);

}
