package com.alps.oauth.server.mapper;

import com.alps.base.api.model.entity.BaseMenu;
import com.alps.plaform.database.mapper.SuperMapper;

import com.alps.provider.system.domain.SysMenu;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author YJ.lee
 */
@Repository
public interface BaseMenuMapper extends SuperMapper<BaseMenu> {


    List<BaseMenu> selectMenuList(BaseMenu menu);

    List<String> selectMenuPerms();

    List<String> selectMenuPermsByUserId(Long userId);

    List<BaseMenu> selectMenuTreeAll();

    List<BaseMenu> selectMenuTreeByUserId(Long userId);

    List<Integer> selectMenuListByRoleId(Long roleId);

    BaseMenu selectMenuById(Long menuId);

    int hasChildByMenuId(Long menuId);

    int insertMenu(BaseMenu menu);

    int updateMenu(BaseMenu menu);

    int deleteMenuById(Long menuId);

    BaseMenu checkMenuNameUnique(@Param("menuName") String menuName, @Param("parentId") Long parentId);

	List<BaseMenu> selectMenuTree(Map<String, Object> map);

	List<BaseMenu> selectMenuTreeByCompanyId(Map<String, Object> map);

}
