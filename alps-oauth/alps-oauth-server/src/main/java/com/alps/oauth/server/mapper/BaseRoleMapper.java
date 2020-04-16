package com.alps.oauth.server.mapper;

import org.springframework.stereotype.Repository;

import com.alps.base.api.model.entity.BaseRole;
import com.alps.plaform.database.mapper.SuperMapper;

import java.util.List;
import java.util.Map;

/**
 * @author YJ.lee
 */
@Repository
public interface BaseRoleMapper extends SuperMapper<BaseRole> {

    List<BaseRole> selectRoleList(Map params);
}
