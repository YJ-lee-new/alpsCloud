package com.alps.oauth.server.mapper;

import org.springframework.stereotype.Repository;

import com.alps.base.api.model.AuthorityAction;
import com.alps.base.api.model.AuthorityApi;
import com.alps.base.api.model.AuthorityMenu;
import com.alps.base.api.model.AuthorityResource;
import com.alps.base.api.model.entity.BaseAuthority;
import com.alps.common.oauth2.security.AlpsAuthority;
import com.alps.plaform.database.mapper.SuperMapper;

import java.util.List;
import java.util.Map;

/**
 * @author YJ.lee
 */
@Repository
public interface BaseAuthorityMapper extends SuperMapper<BaseAuthority> {

    /**
     * 查询所有资源授权列表
     * @return
     */
    List<AuthorityResource> selectAllAuthorityResource();

    /**
     * 查询已授权权限列表
     *
     * @param map
     * @return
     */
    List<AlpsAuthority> selectAuthorityAll(Map map);


    /**
     * 获取菜单权限
     *
     * @param map
     * @return
     */
    List<AuthorityMenu> selectAuthorityMenu(Map map);

    /**
     * 获取操作权限
     *
     * @param map
     * @return
     */
    List<AuthorityAction> selectAuthorityAction(Map map);

    /**
     * 获取API权限
     *
     * @param map
     * @return
     */
    List<AuthorityApi> selectAuthorityApi(Map map);


}
