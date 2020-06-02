package com.alps.oauth.uaa.client.web.mapper;

import org.springframework.stereotype.Repository;

import com.alps.base.api.model.entity.SysRole;
import com.alps.plaform.database.mapper.SuperMapper;

import java.util.List;

/**
 * @author YJ.lee
 */
@Repository
public interface SysRoleMapper extends SuperMapper<SysRole> {

    List<SysRole> selectRoleList(SysRole role);
    
    /**
     * 批量角色用户信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteRoleByIds(Long[] ids);
}
