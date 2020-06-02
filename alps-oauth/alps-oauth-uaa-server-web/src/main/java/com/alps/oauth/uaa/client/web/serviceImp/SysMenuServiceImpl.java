package com.alps.oauth.uaa.client.web.serviceImp;

import com.alps.base.api.model.vo.MetaVo;
import com.alps.base.api.model.vo.RouterVo;
import com.alps.common.core.domain.Ztree;
import com.alps.base.api.model.entity.SysApi;
import com.alps.base.api.model.entity.SysApp;
import com.alps.base.api.model.entity.SysAuthority;
import com.alps.base.api.model.entity.SysMenu;
import com.alps.common.oauth2.config.CommonConstants;
import com.alps.common.oauth2.exception.AlpsAlertException;
import com.alps.common.utils.StringUtils;
import com.alps.oauth.uaa.client.web.mapper.SysApiMapper;
import com.alps.oauth.uaa.client.web.mapper.SysAppMapper;
import com.alps.oauth.uaa.client.web.mapper.SysAuthorityMapper;
import com.alps.oauth.uaa.client.web.mapper.SysMenuMapper;
import com.alps.oauth.uaa.client.web.model.PageParams;
import com.alps.oauth.uaa.client.web.service.ISysMenuService;
import com.alps.oauth.uaa.client.web.utils.WebUserHelper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author YJ.lee
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysMenuServiceImpl extends BaseServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {
    @Autowired
    private SysMenuMapper sysMenuMapper;
    
    @Autowired
    private SysAppMapper sysAppMapper;
    
    @Autowired
    private SysApiMapper sysApiMapper;
    
    @Autowired
    private SysAuthorityMapper sysAuthorityMapper;
    

    @Value("${spring.application.name}")
    private String DEFAULT_SERVICE_ID;

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    @Override
    public IPage<SysMenu> findListPage(PageParams pageParams) {
        SysMenu query = pageParams.mapToObject(SysMenu.class);
        QueryWrapper<SysMenu> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .likeRight(ObjectUtils.isNotEmpty(query.getMenuId()), SysMenu::getMenuId, query.getMenuId())
                .likeRight(ObjectUtils.isNotEmpty(query.getMenuName()), SysMenu::getMenuName, query.getMenuName());
        return sysMenuMapper.selectPage(new Page(pageParams.getPage(), pageParams.getLimit()), queryWrapper);
    }

    /**
     * 查询列表
     *
     * @return
     */
    @Override
    public List<SysMenu> findAllList() {
        List<SysMenu> list = sysMenuMapper.selectList(new QueryWrapper<>());
        //根据优先级从小到大排序
        list.sort((SysMenu h1, SysMenu h2) -> h1.getOrderNum().compareTo(h2.getOrderNum()));
        return list;
    }

    /**
     * 根据主键获取菜单
     *
     * @param menuId
     * @return
     */
    @Override
    public SysMenu getMenu(Long menuId) {
        return sysMenuMapper.selectById(menuId);
    }

    /**
     * 检查菜单编码是否存在
     *
     * @param menuCode
     * @return
     */
    @Override
    public Boolean isExist(Long menuId) {
        QueryWrapper<SysMenu> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(SysMenu::getMenuId, menuId);
        int count = sysMenuMapper.selectCount(queryWrapper);
        return count > 0 ? true : false;
    }

    /**
     * 添加菜单资源
     *
     * @param menu
     * @return
     */
    @Override
    public SysMenu addMenu(SysMenu menu) {
        if (isExist(menu.getMenuId())) {
            throw new AlpsAlertException(String.format("%s编码已存在!", menu.getMenuId()));
        }
        if (menu.getParentId() == null) {
            menu.setParentId(0L);
        }
        if (menu.getOrderNum() == null) {
            menu.setOrderNum(0);
        }
        if (menu.getStatus() == null) {
            menu.setStatus(1);
        }
        menu.setServiceId(DEFAULT_SERVICE_ID);
        menu.setCreateTime(new Date());
        menu.setUpdateTime(menu.getCreateTime());
        sysMenuMapper.insert(menu);
        return menu;
    }

    /**
     * 修改菜单资源
     *
     * @param menu
     * @return
     */
    @Override
    public int updateMenu(SysMenu menu) {
        SysMenu saved = getMenu(menu.getMenuId());
        if (saved == null) {
            throw new AlpsAlertException(String.format("%s信息不存在!", menu.getMenuId()));
        }
        if (!saved.getMenuId().equals(menu.getMenuId())) {
            // 和原来不一致重新检查唯一性
            if (isExist(menu.getMenuId())) {
                throw new AlpsAlertException(String.format("%s编码已存在!", menu.getMenuId()));
            }
        }
        if (menu.getParentId() == null) {
            menu.setParentId(0l);
        }
        if (menu.getOrderNum() == null) {
            menu.setOrderNum(0);
        }
        menu.setUpdateTime(new Date());
        return sysMenuMapper.updateById(menu);
        
    }


    /**
     * 移除菜单
     *
     * @param menuId
     * @return
     */
    @Override
    public void removeMenu(Long menuId) {
        // 移除菜单信息
        sysMenuMapper.deleteById(menuId);
    }

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @param companyId 租户Id
     * @return 权限列表
     */
    @Override
    public List<SysMenu> selectMenuTreeByUserId(String account,String companyId)
    {
        List<SysMenu> menus = null;
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("account", account);
        map.put("companyId", companyId);
        if (CommonConstants.ROOT.equals(WebUserHelper.getUser().getUsername()))
        {
        	map.put("companyId", null);
        }
        //menus = sysMenuMapper.selectMenuTree(map);
        menus = sysMenuMapper.selectMenuTreeByUserId(map);
        return getChildPerms(menus, 0);
    }


    /**
     * 查询系统菜单列表
     *
     * @param menu 菜单信息
     * @return 菜单列表
     */
    @Override
    public List<SysMenu> selectMenuList(SysMenu menu)
    {
        List<SysMenu> menuList = sysMenuMapper.selectMenuList(menu);
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
        List<String> perms = sysMenuMapper.selectMenuPermsByUserId(userId);
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
        return sysMenuMapper.selectMenuListByRoleId(roleId);
    }

    /**
     * 构建前端路由所需要的菜单
     *
     * @param menus 菜单列表
     * @return 路由列表
     */
    @Override
    public List<RouterVo> buildMenus(List<SysMenu> menus)
    {
        List<RouterVo> routers = new LinkedList<RouterVo>();
        for (SysMenu menu : menus)
        {
            RouterVo router = new RouterVo();
            router.setMenuId(menu.getMenuId());
            router.setName(menu.getMenuName());
            router.setPath(getRouterPath(menu));
            router.setComponent(StringUtils.isEmpty(menu.getComponent()) ? "Layout" : menu.getComponent());
            router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon()));
            router.setName(menu.getMenuName());
            List<SysMenu> cMenus = menu.getChildren();
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
    public List<SysMenu> buildMenuTree(List<SysMenu> menus)
    {
        List<SysMenu> returnList = new ArrayList<SysMenu>();
        for (Iterator<SysMenu> iterator = menus.iterator(); iterator.hasNext();)
        {
            SysMenu t = (SysMenu) iterator.next();
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
    public SysMenu selectMenuById(Long menuId)
    {
    	 SysMenu sysMenu = sysMenuMapper.selectMenuById(menuId);
         if(sysMenu != null && "A".equals(sysMenu.getMenuType()) && sysMenu.getApiId() != null) {
        	 SysApi sysapi = sysApiMapper.selectById(sysMenu.getApiId());
        	 SysApp sysapp = sysAppMapper.selectById(sysapi.getServiceId());
        	 sysMenu.setApiName(sysapp.getAppName()+":-->"+ sysapi.getApiName()+":-->" + sysapi.getApiCode());
         }
         return sysMenu;
    }

    /**
     * 查询所有菜单不含事件
     * 
     * @return 菜单列表
     */
    @Override
    public List<Ztree> menuTreeData()
    {
        List<SysMenu> menuList = sysMenuMapper.selectMenuTree() ;
        List<Ztree> ztrees = initZtree(menuList);
        return ztrees;
    }
    /**
     * 查询所有菜单含事件
     * 
     * @return 菜单列表
     */
    @Override
    public List<Ztree> menuTreeDataAll()
    {
    	List<SysMenu> menuList = sysMenuMapper.selectMenuTreeAll() ;
    	List<Ztree> ztrees = initZtree(menuList);;
    	return ztrees;
    }
    /**
     * 查询所有菜单含事件
     * 
     * @return 菜单列表
     */
    @Override
    public List<Ztree> menuTreeDataAllByRole(Long roleId)
    {
    	List<SysMenu> menuList = sysMenuMapper.selectMenuTreeAll() ;
    	List<SysAuthority> tuthorityList = sysAuthorityMapper.selectSysAutoListByRoleId(roleId);
    	List<Ztree> ztrees = initSelectTree(menuList,tuthorityList);
    	return ztrees;
    }
    
    /**
     * 对象转菜单树
     * 
     * @param menuList 菜单列表
     * @return 树结构列表
     */
    public List<Ztree> initZtree(List<SysMenu> menuList)
    {
        return initZtree(menuList, null, false);
    }

    /**
     * 对象转菜单树
     * 
     * @param menuList 菜单列表
     * @param roleMenuList 角色已存在菜单列表
     * @param permsFlag 是否需要显示权限标识
     * @return 树结构列表
     */
    public List<Ztree> initZtree(List<SysMenu> menuList, List<String> roleMenuList, boolean permsFlag)
    {
        List<Ztree> ztrees = new ArrayList<Ztree>();
        for (SysMenu menu : menuList)
        {
            Ztree ztree = new Ztree();
            ztree.setId(menu.getMenuId());
            ztree.setpId(menu.getParentId());
            ztree.setName(transMenuName(menu, roleMenuList));
            ztree.setTitle(menu.getMenuName());
            if(menu != null && "A".equals(menu.getMenuType()) && menu.getApiId() != null) {
           	 SysApi sysapi = sysApiMapper.selectById(menu.getApiId());
           	 ztree.setName(transMenuName(menu, roleMenuList) +"-->"+ sysapi.getApiName()+"(" + sysapi.getApiCode()+")");
            }
            ztrees.add(ztree);
        }
        return ztrees;
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
        int result = sysMenuMapper.hasChildByMenuId(menuId);
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
    public int insertMenu(SysMenu menu)
    {
        return sysMenuMapper.insertMenu(menu);
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
//        return sysMenuMapper.updateMenu(menu);
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
        return sysMenuMapper.deleteMenuById(menuId);
    }

    /**
     * 校验菜单名称是否唯一
     *
     * @param menu 菜单信息
     * @return 结果
     */
    @Override
    public String checkMenuNameUnique(SysMenu menu)
    {
        Long menuId = StringUtils.isNull(menu.getMenuId()) ? -1L : menu.getMenuId();
        SysMenu info = sysMenuMapper.checkMenuNameUnique(menu.getMenuName(), menu.getParentId());
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
    public String getRouterPath(SysMenu menu)
    {
        String routerPath = menu.getUrl();
        // 非外链并且是一级目录
        if (0 == menu.getParentId() && "1".equals(menu.getIsFrame()))
        {
            routerPath = "/" + menu.getUrl();
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
    public List<SysMenu> getChildPerms(List<SysMenu> list, int parentId)
    {
        List<SysMenu> returnList = new ArrayList<SysMenu>();
        for (Iterator<SysMenu> iterator = list.iterator(); iterator.hasNext();)
        {
            SysMenu t = (SysMenu) iterator.next();
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
    private void recursionFn(List<SysMenu> list, SysMenu t)
    {
        // 得到子节点列表
        List<SysMenu> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysMenu tChild : childList)
        {
            if (hasChild(list, tChild))
            {
                // 判断是否有子节点
                Iterator<SysMenu> it = childList.iterator();
                while (it.hasNext())
                {
                    SysMenu n = (SysMenu) it.next();
                    recursionFn(list, n);
                }
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysMenu> getChildList(List<SysMenu> list, SysMenu t)
    {
        List<SysMenu> tlist = new ArrayList<SysMenu>();
        Iterator<SysMenu> it = list.iterator();
        while (it.hasNext())
        {
            SysMenu n = (SysMenu) it.next();
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
    private boolean hasChild(List<SysMenu> list, SysMenu t)
    {
        return getChildList(list, t).size() > 0 ? true : false;
    }
    
    public String transMenuName(SysMenu menu, List<String> roleMenuList)
    {
        StringBuffer sb = new StringBuffer();
        sb.append(menu.getMenuName());
        return sb.toString();
    }
	
	@Override
	public List<Ztree> getApiTreeData() {
		List<Ztree> ztrees = new ArrayList<Ztree>();
		SysApp sysApp = new SysApp();
		//查找出所有app
		List<SysApp> applist = sysAppMapper.selectSysAppList(sysApp);
		for(SysApp sysapp : applist) {
			/**
			 * 找出所有APP下面的API List
			 */
			Long appId = sysapp.getAppId();
			 QueryWrapper<SysApi> queryWrapper = new QueryWrapper();
		        queryWrapper.lambda()
		                .eq(SysApi::getServiceId , appId);
			List<SysApi> apilist = sysApiMapper.selectList(queryWrapper) ;
			Ztree ztree = new Ztree();
			ztree.setId(sysapp.getAppId() );
            ztree.setpId(0L);
            ztree.setName(sysapp.getAppName());
            ztree.setTitle(sysapp.getAppDesc());
            ztrees.add(ztree);
			for( SysApi sysapi: apilist) {
				Ztree ztree2 = new Ztree();
				ztree2.setId(sysapi.getApiId());
				ztree2.setpId(sysapi.getServiceId());
				ztree2.setName(sysapi.getApiName()+":-->" + sysapi.getApiCode());
				ztree2.setTitle(sysapi.getApiDesc());
	            ztrees.add(ztree2);
			}
		}
		
		return ztrees;
	}
	
	@Override
	public List<Ztree> getApiTreeDataByRole(Long roleId) {
		List<Ztree> ztrees = new ArrayList<Ztree>();
		SysApp sysApp = new SysApp();
		//查找出所有app
		List<SysApp> applist = sysAppMapper.selectSysAppList(sysApp);
		List<SysAuthority> tuthorityList = sysAuthorityMapper.selectSysAutoListByRoleId(roleId);
		for(SysApp sysapp : applist) {
			/**
			 * 找出所有APP下面的API List
			 */
			Long appId = sysapp.getAppId();
			QueryWrapper<SysApi> queryWrapper = new QueryWrapper();
			queryWrapper.lambda()
			.eq(SysApi::getServiceId , appId);
			List<SysApi> apilist = sysApiMapper.selectList(queryWrapper) ;
			Ztree ztree = new Ztree();
			ztree.setId(sysapp.getAppId() );
			ztree.setpId(0L);
			ztree.setName(sysapp.getAppName());
			ztree.setTitle(sysapp.getAppDesc());
			ztrees.add(ztree);
			for( SysApi sysapi: apilist) {
				Ztree ztree2 = new Ztree();
				ztree2.setId(sysapi.getApiId());
				ztree2.setpId(sysapi.getServiceId());
				ztree2.setName(sysapi.getApiName()+":-->" + sysapi.getApiCode());
				ztree2.setTitle(sysapi.getApiDesc());
				for(SysAuthority sa : tuthorityList) {
					if(sysapi.getApiId().equals(sa.getApiId())) {
						ztree2.setChecked(true);
					}
				}
				ztrees.add(ztree2);
			}
		}
		
		return ztrees;
	}
	
	public List<Ztree>  initSelectTree(List<SysMenu> allList,List<SysAuthority> selectList ){
		  List<Ztree> ztrees = new ArrayList<Ztree>();
	        for (SysMenu menu : allList)
	        {
	            Ztree ztree = new Ztree();
	            ztree.setId(menu.getMenuId());
	            ztree.setpId(menu.getParentId());
	            ztree.setName(menu.getMenuName());
	            ztree.setTitle(menu.getMenuName());
	            if(menu != null && "A".equals(menu.getMenuType()) && menu.getApiId() != null) {
	           	 SysApi sysapi = sysApiMapper.selectById(menu.getApiId());
	           	 ztree.setName(menu.getMenuName() +"-->"+ sysapi.getApiName()+"(" + sysapi.getApiCode()+")");
	            }
	            for(SysAuthority sa : selectList) {
	            	if(menu.getMenuId().equals(sa.getMenuId())) {
	            		ztree.setChecked(true);
	            	}
	            }
	            ztrees.add(ztree);
	        }
	        return ztrees;
		
		
	}

	@Override
	public List<SysMenu> findActionListByMenuId(Long menuId) {
		return  sysMenuMapper.selectActionMenuList(menuId);
	}
}
