package com.alps.oauth.uaa.client.web.service;

import com.alps.base.api.model.entity.SysMenu;
import com.alps.base.api.model.vo.RouterVo;
import com.alps.common.core.domain.Ztree;
import com.alps.oauth.uaa.client.web.model.PageParams;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;
import java.util.Set;

/**
 * 菜单资源管理
 * @author YJ.lee
 */
public interface ISysMenuService extends IBaseService<SysMenu> {
    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    IPage<SysMenu> findListPage(PageParams pageParams);

    /**
     * 查询列表
     * @return
     */
    List<SysMenu> findAllList();
    
    /**
     * 查询列表
     * @return
     */
    List<SysMenu> findActionListByMenuId(Long menuId);

    /**
     * 根据主键获取菜单
     *
     * @param menuId
     * @return
     */
    SysMenu getMenu(Long menuId);

    /**
     * 检查菜单编码是否存在
     *
     * @param menuCode
     * @return
     */
    Boolean isExist(Long menuCode);


    /**
     * 查询所有菜单信息
     * 
     * @return 菜单列表
     */
    public List<Ztree> menuTreeData();
    
    /**
     * 查询所有菜单信息
     * 
     * @return 菜单列表
     */
    public List<Ztree> menuTreeDataAll();
    /**
     * 查询所有菜单信息
     * 
     * @return 菜单列表
     */
    public List<Ztree> menuTreeDataAllByRole(Long roleId);
    
    /**
     * 查询所有api信息
     * 
     * @return api列表
     */
    public List<Ztree> getApiTreeData();
    /**
     * 查询所有api信息
     * 
     * @return api列表
     */
    public List<Ztree> getApiTreeDataByRole(Long roleId);


    /**
     * 添加菜单资源
     *
     * @param menu
     * @return
     */
    SysMenu addMenu(SysMenu menu);

    /**
     * 修改菜单资源
     *
     * @param menu
     * @return
     */
    int updateMenu(SysMenu menu);

    /**
     * 移除菜单
     *
     * @param menuId
     * @return
     */
    void removeMenu(Long menuId);

    /**
     * 获取用户权限
     *
     * @param userId
     * @return
     */
    List<SysMenu> selectMenuTreeByUserId(String account,String companyId);


    /**
     * 查询系统菜单列表
     *
     * @param menu 菜单信息
     * @return 菜单列表
     */
    public List<SysMenu> selectMenuList(SysMenu menu);

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    public Set<String> selectMenuPermsByUserId(Long userId);

    /**
     * 根据角色ID查询菜单树信息
     *
     * @param roleId 角色ID
     * @return 选中菜单列表
     */
    public List<Integer> selectMenuListByRoleId(Long roleId);

    /**
     * 构建前端路由所需要的菜单
     *
     * @param menus 菜单列表
     * @return 路由列表
     */
    public List<RouterVo> buildMenus(List<SysMenu> menus);

    /**
     * 构建前端所需要树结构
     *
     * @param menus 菜单列表
     * @return 树结构列表
     */
    public List<SysMenu> buildMenuTree(List<SysMenu> menus);

    /**
     * 构建前端所需要下拉树结构
     *
     * @param menus 菜单列表
     * @return 下拉树结构列表
     */
//    public List<TreeSelect> buildMenuTreeSelect(List<BaseMenu> menus);

    /**
     * 根据菜单ID查询信息
     *
     * @param menuId 菜单ID
     * @return 菜单信息
     */
    public SysMenu selectMenuById(Long menuId);

    /**
     * 是否存在菜单子节点
     *
     * @param menuId 菜单ID
     * @return 结果 true 存在 false 不存在
     */
    public boolean hasChildByMenuId(Long menuId);

    /**
     * 查询菜单是否存在角色
     *
     * @param menuId 菜单ID
     * @return 结果 true 存在 false 不存在
     */
//    public boolean checkMenuExistRole(Long menuId);

    /**
     * 新增保存菜单信息
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public int insertMenu(SysMenu menu);


    /**
     * 删除菜单管理信息
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    public int deleteMenuById(Long menuId);

    /**
     * 校验菜单名称是否唯一
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public String checkMenuNameUnique(SysMenu menu);

}
