package com.alps.oauth.uaa.client.web.controller;

import com.alps.common.annotation.Log;
import com.alps.common.core.domain.AjaxResult;
import com.alps.common.core.domain.ResultBody;
import com.alps.common.core.domain.Ztree;
import com.alps.common.enums.BusinessType;
import com.alps.base.api.model.entity.SysAction;
import com.alps.base.api.model.entity.SysApp;
import com.alps.base.api.model.entity.SysMenu;
import com.alps.common.oauth2.constants.AlpsRestTemplate;
import com.alps.oauth.uaa.client.web.model.PageParams;
import com.alps.oauth.uaa.client.web.service.ISysAppService;
import com.alps.oauth.uaa.client.web.service.ISysMenuService;
import com.alps.oauth.uaa.client.web.utils.WebUserHelper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author YJ.lee
 */
@Api(tags = "系统菜单资源管理")
@Controller
public class SysMenuController extends BaseController {
	private String prefix = "system/menu";
    @Autowired
    private ISysMenuService sysMenuService;
    
    @Autowired
    private ISysAppService SysAppService;

    @Autowired
    private AlpsRestTemplate alpsRestTemplate;
    
    
    @GetMapping("/system/menu")
    public String menu()
    {
        return prefix + "/menu";
    }
    
    @GetMapping("/system/menu/list")
    @ResponseBody
    public List<SysMenu> list(SysMenu menu)
    {
        List<SysMenu> menuList = sysMenuService.findAllList();
        return menuList;
    }
    
    /**
     * 删除菜单
     */
    @Log(title = "菜单管理", businessType = BusinessType.DELETE)
    @GetMapping("/system/menu/remove/{menuId}")
    @ResponseBody
    public AjaxResult remove(@PathVariable("menuId") Long menuId)
    {
        if (sysMenuService.hasChildByMenuId(menuId))
        {
            return AjaxResult.warn("存在子菜单,不允许删除");
        }
        return toAjax(sysMenuService.deleteMenuById(menuId));
    }

    /**
     * 新增
     */
    @GetMapping("/system/menu/add/{parentId}")
    public String add(@PathVariable("parentId") Long parentId, ModelMap mmap)
    {
        SysMenu menu = null;
        if (0L != parentId)
        {
            menu = sysMenuService.selectMenuById(parentId);
        }
        else
        {
            menu = new SysMenu();
            menu.setMenuId(0L);
            menu.setMenuName("主目录");
        }
    	SysApp SysApp = new SysApp();
		mmap.put("appList", SysAppService.selectSysAppList(SysApp));
        mmap.put("menu", menu);
        return prefix + "/add";
    }

    /**
     * 新增保存菜单
     */
    @Log(title = "菜单管理", businessType = BusinessType.INSERT)
    @PostMapping("/system/menu/add")
    @ResponseBody
    public AjaxResult addSave(SysMenu menu)
    {
        menu.setCreateBy(WebUserHelper.getUser().getUserId().toString());
        return toAjax(sysMenuService.insertMenu(menu));
    }
 
    /**
     * 选择菜单树
     */
    @GetMapping("/system/menu/selectMenuTree/{menuId}")
    public String selectMenuTree(@PathVariable("menuId") Long menuId, ModelMap mmap)
    {
        mmap.put("menu", sysMenuService.selectMenuById(menuId));
        return prefix + "/tree";
    }
    
    
    /**
     * 加载所有菜单列表树不包括事件
     */
    @GetMapping("/system/menu/menuTreeData")
    @ResponseBody
    public List<Ztree> menuTreeData()
    {
        List<Ztree> ztrees = sysMenuService.menuTreeData();
        return ztrees;
    }
    
    /**
     * 加载所有菜单列表树包括事件
     */
    @GetMapping("/system/menu/menuTreeDataAll")
    @ResponseBody
    public List<Ztree> menuTreeDataAll()
    {
        List<Ztree> ztrees = sysMenuService.menuTreeDataAll();
        return ztrees;
    }
    
    /**
     * 加载所有菜单列表树包括事件
     */
    @GetMapping("/system/menu/menuTreeDataAllByRoleId")
    @ResponseBody
    public List<Ztree> menuTreeDataAllByRoleId(Long roleId)
    {
    	List<Ztree> ztrees = sysMenuService.menuTreeDataAllByRole(roleId)  ;
    	return ztrees;
    }
    
    /**
     * 加载所有api列表树
     */
    @GetMapping("/system/menu/apiTreeData")
    @ResponseBody
    public List<Ztree> apiTreeData()
    {
    	List<Ztree> ztrees = sysMenuService.getApiTreeData();
    	return ztrees;
    }
    /**
     * 加载所有api列表树
     */
    @GetMapping("/system/menu/apiTreeDataByRoleId")
    @ResponseBody
    public List<Ztree> apiTreeDataByRoleId(Long roleId)
    {
    	List<Ztree> ztrees = sysMenuService.getApiTreeDataByRole(roleId);
    	return ztrees;
    }
    
    /**
     * 打开API选择页面
     */
    @GetMapping("/system/menu/selectApiTree")
    public String selectApiTree()
    {
    	return prefix + "/apiTree";
    }
    
    

    /**
     * 修改菜单
     */
    @GetMapping("/system/menu/edit/{menuId}")
    public String edit(@PathVariable("menuId") Long menuId, ModelMap mmap)
    {
    	SysApp SysApp = new SysApp();
        mmap.put("menu", sysMenuService.selectMenuById(menuId));
        mmap.put("appList", SysAppService.selectSysAppList(SysApp));
        return prefix + "/edit";
    }

    /**
     * 修改保存菜单
     */
    @Log(title = "菜单管理", businessType = BusinessType.UPDATE)
    @PostMapping("/system/menu/edit")
    @ResponseBody
    public AjaxResult editSave(SysMenu menu)
    {
        menu.setUpdateBy(WebUserHelper.getUser().getUserId().toString());
        return toAjax(sysMenuService.updateMenu(menu));
    }

    /**
     * 选择菜单图标
     */
    @GetMapping("/system/menu/icon")
    public String icon()
    {
        return prefix + "/icon";
    }
    
    

    /**
     * 获取分页菜单资源列表
     *
     * @return
     */
    @ApiOperation(value = "获取分页菜单资源列表", notes = "获取分页菜单资源列表")
    @GetMapping("/menu")
    public ResultBody<IPage<SysMenu>> getMenuListPage(@RequestParam(required = false) Map map) {
        return ResultBody.ok().data(sysMenuService.findListPage(new PageParams(map)));
    }

    /**
     * 菜单所有资源列表
     *
     * @return
     */
    @ApiOperation(value = "菜单所有资源列表", notes = "菜单所有资源列表")
    @GetMapping("/menu/all")
    public ResultBody<List<SysMenu>> getMenuAllList() {
        return ResultBody.ok().data(sysMenuService.findAllList());
    }


    /**
     * 获取菜单下所有操作
     *
     * @param menuId
     * @return
     */
    @ApiOperation(value = "获取菜单下所有操作", notes = "获取菜单下所有操作")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "menuId", value = "menuId", paramType = "form"),
    })
    @GetMapping("/menu/action")
    public ResultBody<List<SysAction>> getMenuAction(Long menuId) {
        return ResultBody.ok().data(sysMenuService.findActionListByMenuId(menuId));
    }

    /**
     * 获取菜单资源详情
     *
     * @param menuId
     * @return 应用信息
     */
    @ApiOperation(value = "获取菜单资源详情", notes = "获取菜单资源详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "menuId", required = true, value = "menuId"),
    })
    @GetMapping("/menu/{menuId}/info")
    public ResultBody<SysMenu> getMenu(@PathVariable("menuId") Long menuId) {
        return ResultBody.ok().data(sysMenuService.getMenu(menuId));
    }

    /**
     * 添加菜单资源
     *
     * @param menuCode 菜单编码
     * @param menuName 菜单名称
     * @param icon     图标
     * @param scheme   请求前缀
     * @param path     请求路径
     * @param target   打开方式
     * @param status   是否启用
     * @param parentId 父节点ID
     * @param priority 优先级越小越靠前
     * @param menuDesc 描述
     * @return
     */
    @ApiOperation(value = "添加菜单资源", notes = "添加菜单资源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "menuCode", required = true, value = "菜单编码", paramType = "form"),
            @ApiImplicitParam(name = "menuName", required = true, value = "菜单名称", paramType = "form"),
            @ApiImplicitParam(name = "icon", required = false, value = "图标", paramType = "form"),
            @ApiImplicitParam(name = "scheme", required = false, value = "请求协议", allowableValues = "/,http://,https://", paramType = "form"),
            @ApiImplicitParam(name = "path", required = false, value = "请求路径", paramType = "form"),
            @ApiImplicitParam(name = "target", required = false, value = "请求路径", allowableValues = "_self,_blank", paramType = "form"),
            @ApiImplicitParam(name = "parentId", required = false, defaultValue = "0", value = "父节点ID", paramType = "form"),
            @ApiImplicitParam(name = "status", required = true, defaultValue = "1", allowableValues = "0,1", value = "是否启用", paramType = "form"),
            @ApiImplicitParam(name = "priority", required = false, value = "优先级越小越靠前", paramType = "form"),
            @ApiImplicitParam(name = "menuDesc", required = false, value = "描述", paramType = "form"),
    })
    @PostMapping("/menu/add")
    public ResultBody<Long> addMenu(
            @RequestParam(value = "menuCode") long menuCode,
            @RequestParam(value = "menuName") String menuName,
            @RequestParam(value = "icon", required = false) String icon,
            @RequestParam(value = "scheme", required = false, defaultValue = "/") String scheme,
            @RequestParam(value = "path", required = false, defaultValue = "") String path,
            @RequestParam(value = "target", required = false, defaultValue = "_self") String target,
            @RequestParam(value = "status", defaultValue = "1") Integer status,
            @RequestParam(value = "parentId", required = false, defaultValue = "0") Long parentId,
            @RequestParam(value = "orderNum", required = false, defaultValue = "0") Integer orderNum
    ) {
        SysMenu menu = new SysMenu();
        menu.setMenuId(menuCode);
        menu.setMenuName(menuName);
        menu.setIcon(icon);
        menu.setUrl(path);
        menu.setScheme(scheme);
        menu.setTarget(target);
        menu.setStatus(status);
        menu.setParentId(parentId);
        menu.setOrderNum(orderNum);
        Long menuId = null;
        SysMenu result = sysMenuService.addMenu(menu);
        if (result != null) {
            menuId = result.getMenuId();
        }
        return ResultBody.ok().data(menuId);
    }

    /**
     * 编辑菜单资源
     *
     * @param menuCode 菜单编码
     * @param menuName 菜单名称
     * @param icon     图标
     * @param scheme   请求前缀
     * @param path     请求路径
     * @param target   打开方式
     * @param status   是否启用
     * @param parentId 父节点ID
     * @param priority 优先级越小越靠前
     * @param menuDesc 描述
     * @return
     */
    @ApiOperation(value = "编辑菜单资源", notes = "编辑菜单资源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "menuId", required = true, value = "菜单ID", paramType = "form"),
            @ApiImplicitParam(name = "menuCode", required = true, value = "菜单编码", paramType = "form"),
            @ApiImplicitParam(name = "menuName", required = true, value = "菜单名称", paramType = "form"),
            @ApiImplicitParam(name = "icon", required = false, value = "图标", paramType = "form"),
            @ApiImplicitParam(name = "scheme", required = false, value = "请求协议", allowableValues = "/,http://,https://", paramType = "form"),
            @ApiImplicitParam(name = "path", required = false, value = "请求路径", paramType = "form"),
            @ApiImplicitParam(name = "target", required = false, value = "请求路径", allowableValues = "_self,_blank", paramType = "form"),
            @ApiImplicitParam(name = "parentId", required = false, defaultValue = "0", value = "父节点ID", paramType = "form"),
            @ApiImplicitParam(name = "status", required = true, defaultValue = "1", allowableValues = "0,1", value = "是否启用", paramType = "form"),
            @ApiImplicitParam(name = "priority", required = false, value = "优先级越小越靠前", paramType = "form"),
            @ApiImplicitParam(name = "menuDesc", required = false, value = "描述", paramType = "form"),
    })
    @PostMapping("/menu/update")
    public ResultBody updateMenu(
            @RequestParam("menuId") Long menuId,
            @RequestParam(value = "menuName") String menuName,
            @RequestParam(value = "icon", required = false) String icon,
            @RequestParam(value = "scheme", required = false, defaultValue = "/") String scheme,
            @RequestParam(value = "path", required = false, defaultValue = "") String path,
            @RequestParam(value = "target", required = false, defaultValue = "_self") String target,
            @RequestParam(value = "status", defaultValue = "1") Integer status,
            @RequestParam(value = "parentId", required = false, defaultValue = "0") Long parentId
    ) {
        SysMenu menu = new SysMenu();
        menu.setMenuId(menuId);
        menu.setMenuName(menuName);
        menu.setIcon(icon);
        menu.setUrl(path);
        menu.setScheme(scheme);
        menu.setTarget(target);
        menu.setStatus(status);
        menu.setParentId(parentId);
        sysMenuService.updateMenu(menu);
        alpsRestTemplate.refreshGateway();
        return ResultBody.ok();
    }

    /**
     * 移除菜单资源
     *
     * @param menuId
     * @return
     */
    @ApiOperation(value = "移除菜单资源", notes = "移除菜单资源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "menuId", required = true, value = "menuId", paramType = "form"),
    })
    @PostMapping("/menu/remove")
    public ResultBody<Boolean> removeMenu(
            @RequestParam("menuId") Long menuId
    ) {
        sysMenuService.removeMenu(menuId);
        alpsRestTemplate.refreshGateway();
        return ResultBody.ok();
    }
}
