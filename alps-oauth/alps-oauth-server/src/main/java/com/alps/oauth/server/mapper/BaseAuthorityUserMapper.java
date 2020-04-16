package com.alps.oauth.server.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.alps.base.api.model.AuthorityMenu;
import com.alps.base.api.model.entity.BaseAuthorityUser;
import com.alps.common.oauth2.security.AlpsAuthority;
import com.alps.plaform.database.mapper.SuperMapper;

import java.util.List;

/**
 * @author YJ.lee
 */
@Repository
public interface BaseAuthorityUserMapper extends SuperMapper<BaseAuthorityUser> {

    /**
     * 获取用户已授权权限
     *
     * @param userId
     * @return
     */
    List<AlpsAuthority> selectAuthorityByUser(@Param("userId") Long userId);

    /**
     * 获取用户已授权权限完整信息
     *
     * @param userId
     * @return
     */
    List<AuthorityMenu> selectAuthorityMenuByUser(@Param("userId") Long userId);
}
