package com.alps.oauth.server.service.impl;

import com.alps.base.api.model.vo.MetaVo;
import com.alps.base.api.model.vo.RouterVo;
import com.alps.common.constant.BaseConstants;
import com.alps.common.constant.ResourceType;
import com.alps.base.api.model.entity.BaseMenu;
import com.alps.common.oauth2.config.CommonConstants;
import com.alps.common.oauth2.exception.AlpsAlertException;
import com.alps.common.oauth2.security.AlpsHelper;
import com.alps.common.utils.StringUtils;
import com.alps.oauth.server.mapper.BaseMenuMapper;
import com.alps.oauth.server.model.PageParams;
import com.alps.oauth.server.service.ActionService;
import com.alps.oauth.server.service.AuthorityService;
import com.alps.oauth.server.service.MenuService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author YJ.lee
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class BaseMenuServiceImpl extends BaseServiceImpl<BaseMenuMapper, BaseMenu> implements MenuService {
    @Autowired
    private BaseMenuMapper baseMenuMapper;
    @Autowired
    private AuthorityService authorityService;

    @Autowired
    private ActionService actionService;

    @Value("${spring.application.name}")
    private String DEFAULT_SERVICE_ID;

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    @Override
    public IPage<BaseMenu> findListPage(PageParams pageParams) {
        BaseMenu query = pageParams.mapToObject(BaseMenu.class);
        QueryWrapper<BaseMenu> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .likeRight(ObjectUtils.isNotEmpty(query.getMenuCode()), BaseMenu::getMenuCode, query.getMenuCode())
                .likeRight(ObjectUtils.isNotEmpty(query.getMenuName()), BaseMenu::getMenuName, query.getMenuName());
        return baseMenuMapper.selectPage(new Page(pageParams.getPage(), pageParams.getLimit()), queryWrapper);
    }

    /**
     * 查询列表
     *
     * @return
     */
    @Override
    public List<BaseMenu> findAllList() {
        List<BaseMenu> list = baseMenuMapper.selectList(new QueryWrapper<>());
        //根据优先级从小到大排序
        list.sort((BaseMenu h1, BaseMenu h2) -> h1.getPriority().compareTo(h2.getPriority()));
        return list;
    }

    /**
     * 根据主键获取菜单
     *
     * @param menuId
     * @return
     */
    @Override
    public BaseMenu getMenu(Long menuId) {
        return baseMenuMapper.selectById(menuId);
    }

    /**
     * 检查菜单编码是否存在
     *
     * @param menuCode
     * @return
     */
    @Override
    public Boolean isExist(String menuCode) {
        QueryWrapper<BaseMenu> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(BaseMenu::getMenuCode, menuCode);
        int count = baseMenuMapper.selectCount(queryWrapper);
        return count > 0 ? true : false;
    }

    /**
     * 添加菜单资源
     *
     * @param menu
     * @return
     */
    @Override
    public BaseMenu addMenu(BaseMenu menu) {
        if (isExist(menu.getMenuCode())) {
            throw new AlpsAlertException(String.format("%s编码已存在!", menu.getMenuCode()));
        }
        if (menu.getParentId() == null) {
            menu.setParentId(0L);
        }
        if (menu.getPriority() == null) {
            menu.setPriority(0);
        }
        if (menu.getStatus() == null) {
            menu.setStatus(1);
        }
        if (menu.getIsPersist() == null) {
            menu.setIsPersist(0);
        }
        menu.setServiceId(DEFAULT_SERVICE_ID);
        menu.setCreateTime(new Date());
        menu.setUpdateTime(menu.getCreateTime());
        baseMenuMapper.insert(menu);
        // 同步权限表里的信息
        authorityService.saveOrUpdateAuthority(menu.getMenuId(), ResourceType.menu);
        return menu;
    }

    /**
     * 修改菜单资源
     *
     * @param menu
     * @return
     */
    @Override
    public BaseMenu updateMenu(BaseMenu menu) {
        BaseMenu saved = getMenu(menu.getMenuId());
        if (saved == null) {
            throw new AlpsAlertException(String.format("%s信息不存在!", menu.getMenuId()));
        }
        if (!saved.getMenuCode().equals(menu.getMenuCode())) {
            // 和原来不一致重新检查唯一性
            if (isExist(menu.getMenuCode())) {
                throw new AlpsAlertException(String.format("%s编码已存在!", menu.getMenuCode()));
            }
        }
        if (menu.getParentId() == null) {
            menu.setParentId(0l);
        }
        if (menu.getPriority() == null) {
            menu.setPriority(0);
        }
        menu.setUpdateTime(new Date());
        baseMenuMapper.updateById(menu);
        // 同步权限表里的信息
        authorityService.saveOrUpdateAuthority(menu.getMenuId(), ResourceType.menu);
        return menu;
    }


    /**
     * 移除菜单
     *
     * @param menuId
     * @return
     */
    @Override
    public void removeMenu(Long menuId) {
        BaseMenu menu = getMenu(menuId);
        if (menu != null && menu.getIsPersist().equals(BaseConstants.ENABLED)) {
            throw new AlpsAlertException(String.format("保留数据,不允许删除!"));
        }
        // 移除菜单权限
        authorityService.removeAuthority(menuId, ResourceType.menu);
        // 移除功能按钮和相关权限
        actionService.removeByMenuId(menuId);
        // 移除菜单信息
        baseMenuMapper.deleteById(menuId);
    }

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @param companyId 租户Id
     * @return 权限列表
     */
    @Override
    public List<BaseMenu> selectMenuTreeByUserId(Long userId,String companyId)
    {
        List<BaseMenu> menus = null;
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("userId", companyId);
        map.put("companyId", companyId);
        if (CommonConstants.ROOT.equals(AlpsHelper.getUser().getUsername()))
        {
        	// menus = baseMenuMapper.selectMenuTreeAll();
        	menus = baseMenuMapper.selectMenuTree(map);
        }
        else
        {
            //menus = baseMenuMapper.selectMenuTreeByUserId(userId);
        	menus = baseMenuMapper.selectMenuTreeByCompanyId(map);
        }
//        List<BaseMenu> menus = baseMenuMapper.selectMenuTreeByUserId(userId);
        return getChildPerms(menus, 0);
    }


    /**
     * 查询系统菜单列表
     *
     * @param menu 菜单信息
     * @return 菜单列表
     */
    @Override
    public List<BaseMenu> selectMenuList(BaseMenu menu)
    {
        List<BaseMenu> menuList = baseMenuMapper.selectMenuList(menu);
        return menuList;
    }

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    @Override
    public Set<String> selectMenuPermsByUserId(Long userId)
    {
        List<String> perms = baseMenuMapper.selectMenuPermsByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (String perm : perms)
        {
            if (StringUtils.isNotEmpty(perm))
            {
                permsSet.addAll(Arrays.asList(perm.trim().split(",")));
            }
        }
        return permsSet;
    }


    /**
     * 根据角色ID查询菜单树信息
     *
     * @param roleId 角色ID
     * @return 选中菜单列表
     */
    public List<Integer> selectMenuListByRoleId(Long roleId)
    {
        return baseMenuMapper.selectMenuListByRoleId(roleId);
    }

    /**
     * 构建前端路由所需要的菜单
     *
     * @param menus 菜单列表
     * @return 路由列表
     */
    @Override
    public List<RouterVo> buildMenus(List<BaseMenu> menus)
    {
        List<RouterVo> routers = new LinkedList<RouterVo>();
        for (BaseMenu menu : menus)
        {
            RouterVo router = new RouterVo();
            router.setMenuId(menu.getMenuId());
            router.setName(menu.getMenuName());
            router.setPath(getRouterPath(menu));
            router.setComponent(StringUtils.isEmpty(menu.getComponent()) ? "Layout" : menu.getComponent());
            router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon()));
            router.setName(menu.getMenuName());
            List<BaseMenu> cMenus = menu.getChildren();
            if (!cMenus.isEmpty() && cMenus.size() > 0 && "M".equals(menu.getMenuType()))
            {
                router.setAlwaysShow(true);
                router.setRedirect("noRedirect");
                router.setChildren(buildMenus(cMenus));
            }
            routers.add(router);
        }
        return routers;
    }

    /**
     * 构建前端所需要树结构
     *
     * @param menus 菜单列表
     * @return 树结构列表
     */
    @Override
    public List<BaseMenu> buildMenuTree(List<BaseMenu> menus)
    {
        List<BaseMenu> returnList = new ArrayList<BaseMenu>();
        for (Iterator<BaseMenu> iterator = menus.iterator(); iterator.hasNext();)
        {
            BaseMenu t = (BaseMenu) iterator.next();
            // 根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (t.getParentId() == 0)
            {
                recursionFn(menus, t);
                returnList.add(t);
            }
        }
        if (returnList.isEmpty())
        {
            returnList = menus;
        }
        return returnList;
    }

    /**
     * 构建前端所需要下拉树结构
     *
     * @param menus 菜单列表
     * @return 下拉树结构列表
     */
//    @Override
//    public List<TreeSelect> buildMenuTreeSelect(List<BaseMenu> menus)
//    {
//        List<BaseMenu> menuTrees = buildMenuTree(menus);
//        return menuTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
//    }

    /**
     * 根据菜单ID查询信息
     *
     * @param menuId 菜单ID
     * @return 菜单信息
     */
    @Override
    public BaseMenu selectMenuById(Long menuId)
    {
        return baseMenuMapper.selectMenuById(menuId);
    }

    /**
     * 是否存在菜单子节点
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    @Override
    public boolean hasChildByMenuId(Long menuId)
    {
        int result = baseMenuMapper.hasChildByMenuId(menuId);
        return result > 0 ? true : false;
    }

    /**
     * 查询菜单使用数量
     *
     * @param menuId 菜单ID
     * @return 结果
     */
//    @Override
//    public boolean checkMenuExistRole(Long menuId)
//    {
//        int result = roleMenuMapper.checkMenuExistRole(menuId);
//        return result > 0 ? true : false;
//    }

    /**
     * 新增保存菜单信息
     *
     * @param menu 菜单信息
     * @return 结果
     */
    @Override
    public int insertMenu(BaseMenu menu)
    {
        return baseMenuMapper.insertMenu(menu);
    }

    /**
     * 修改保存菜单信息
     *
     * @param menu 菜单信息
     * @return 结果
     */
//    @Override
//    public int updateMenu(BaseMenu menu)
//    {
//        return baseMenuMapper.updateMenu(menu);
//    }

    /**
     * 删除菜单管理信息
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    @Override
    public int deleteMenuById(Long menuId)
    {
        return baseMenuMapper.deleteMenuById(menuId);
    }

    /**
     * 校验菜单名称是否唯一
     *
     * @param menu 菜单信息
     * @return 结果
     */
    @Override
    public String checkMenuNameUnique(BaseMenu menu)
    {
        Long menuId = StringUtils.isNull(menu.getMenuId()) ? -1L : menu.getMenuId();
        BaseMenu info = baseMenuMapper.checkMenuNameUnique(menu.getMenuName(), menu.getParentId());
        if (StringUtils.isNotNull(info) && info.getMenuId().longValue() != menuId.longValue())
        {
//            return UserConstants.NOT_UNIQUE;
        }
//        return UserConstants.UNIQUE;
        return null;
    }

    /**
     * 获取路由地址
     *
     * @param menu 菜单信息
     * @return 路由地址
     */
    public String getRouterPath(BaseMenu menu)
    {
        String routerPath = menu.getPath();
        // 非外链并且是一级目录
        if (0 == menu.getParentId() && "1".equals(menu.getIsFrame()))
        {
            routerPath = "/" + menu.getPath();
        }
        return routerPath;
    }

    /**
     * 根据父节点的ID获取所有子节点
     *
     * @param list 分类表
     * @param parentId 传入的父节点ID
     * @return String
     */
    public List<BaseMenu> getChildPerms(List<BaseMenu> list, int parentId)
    {
        List<BaseMenu> returnList = new ArrayList<BaseMenu>();
        for (Iterator<BaseMenu> iterator = list.iterator(); iterator.hasNext();)
        {
            BaseMenu t = (BaseMenu) iterator.next();
            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (t.getParentId() == parentId)
            {
                recursionFn(list, t);
                returnList.add(t);
            }
        }
        return returnList;
    }

    /**
     * 递归列表
     *
     * @param list
     * @param t
     */
    private void recursionFn(List<BaseMenu> list, BaseMenu t)
    {
        // 得到子节点列表
        List<BaseMenu> childList = getChildList(list, t);
        t.setChildren(childList);
        for (BaseMenu tChild : childList)
        {
            if (hasChild(list, tChild))
            {
                // 判断是否有子节点
                Iterator<BaseMenu> it = childList.iterator();
                while (it.hasNext())
                {
                    BaseMenu n = (BaseMenu) it.next();
                    recursionFn(list, n);
                }
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<BaseMenu> getChildList(List<BaseMenu> list, BaseMenu t)
    {
        List<BaseMenu> tlist = new ArrayList<BaseMenu>();
        Iterator<BaseMenu> it = list.iterator();
        while (it.hasNext())
        {
            BaseMenu n = (BaseMenu) it.next();
            if (n.getParentId().longValue() == t.getMenuId().longValue())
            {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<BaseMenu> list, BaseMenu t)
    {
        return getChildList(list, t).size() > 0 ? true : false;
    }
}
