package com.alps.oauth.server.mapper;

import com.alps.base.api.model.entity.BaseAuthorityApp;
import com.alps.common.oauth2.security.AlpsAuthority;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author YJ.lee
 */
@Repository
public interface BaseAuthorityAppMapper extends BaseMapper<BaseAuthorityApp> {

    /**
     * 获取应用已授权权限
     *
     * @param appId
     * @return
     */
    List<AlpsAuthority> selectAuthorityByApp(@Param("appId") String appId);
}
