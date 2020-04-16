package com.alps.oauth.server.mapper;

import org.springframework.stereotype.Repository;

import com.alps.base.api.model.entity.BaseSysUser;
import com.alps.plaform.database.mapper.SuperMapper;

/**
 * @author YJ.lee
 */
@Repository
public interface BaseAccountMapper extends SuperMapper<BaseSysUser> {
}
