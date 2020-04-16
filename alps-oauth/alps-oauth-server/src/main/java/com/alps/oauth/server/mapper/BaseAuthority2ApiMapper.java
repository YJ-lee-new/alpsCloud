package com.alps.oauth.server.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.alps.base.api.model.Authority2Api;
import com.alps.base.api.model.entity.BaseAuthorityRole;
import com.alps.plaform.database.mapper.SuperMapper;

import java.util.List;

/**
 * @author YJ.lee
 */
@Repository
public interface BaseAuthority2ApiMapper extends SuperMapper<BaseAuthorityRole> {

    /**
     * 获取角色已授权权限
     *
     * @param roleId
     * @return
     */
    List<Authority2Api> selectApi2List(@Param("userName") String userName);

}
