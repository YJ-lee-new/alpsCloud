package com.alps.oauth.server.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.alps.base.api.model.entity.BaseRole;
import com.alps.base.api.model.entity.BaseRoleUser;
import com.alps.plaform.database.mapper.SuperMapper;

import java.util.List;

/**
 * @author YJ.lee
 */
@Repository
public interface BaseRoleUserMapper extends SuperMapper<BaseRoleUser> {
    /**
     * 查询系统用户角色
     *
     * @param userId
     * @return
     */
    List<BaseRole> selectRoleUserList(@Param("userId") Long userId);

    /**
     * 查询用户角色ID列表
     * @param userId
     * @return
     */
    List<Long> selectRoleUserIdList(@Param("userId") Long userId);
}
