package com.alps.oauth.server.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.alps.base.api.model.AuthorityMenu;
import com.alps.base.api.model.entity.BaseAuthorityRole;
import com.alps.common.oauth2.security.AlpsAuthority;
import com.alps.plaform.database.mapper.SuperMapper;

import java.util.List;

/**
 * @author YJ.lee
 */
@Repository
public interface BaseAuthorityRoleMapper extends SuperMapper<BaseAuthorityRole> {

    /**
     * 获取角色已授权权限
     *
     * @param roleId
     * @return
     */
    List<AlpsAuthority> selectAuthorityByRole(@Param("roleId") Long roleId);

    /**
     * 获取角色菜单权限
     *
     * @param roleId
     * @return
     */
    List<AuthorityMenu> selectAuthorityMenuByRole(@Param("roleId") Long roleId);
}
