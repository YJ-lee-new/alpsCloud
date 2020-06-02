package com.alps.oauth.uaa.client.web.mapper;

import com.alps.base.api.model.entity.SysMenu;
import com.alps.plaform.database.mapper.SuperMapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author YJ.lee
 */
@Repository
public interface SysMenuMapper extends SuperMapper<SysMenu> {


    List<SysMenu> selectMenuList(SysMenu menu);

    List<String> selectMenuPerms();

    List<String> selectMenuPermsByUserId(Long userId);

    List<SysMenu> selectMenuTree();
    
    List<SysMenu> selectActionMenuList(Long menuId);
    
    List<SysMenu> selectMenuTreeAll();

    List<SysMenu> selectMenuTreeByUserId(Map<String, Object> map);

    List<Integer> selectMenuListByRoleId(Long roleId);

    SysMenu selectMenuById(Long menuId);
    
    SysMenu selectMenuPathById(Long menuId);

    int hasChildByMenuId(Long menuId);

    int insertMenu(SysMenu menu);

    int updateMenu(SysMenu menu);

    int deleteMenuById(Long menuId);

    SysMenu checkMenuNameUnique(@Param("menuName") String menuName, @Param("parentId") Long parentId);

	List<SysMenu> selectMenuTree(Map<String, Object> map);

	List<SysMenu> selectMenuTreeByCompanyId(Map<String, Object> map);
	

}
