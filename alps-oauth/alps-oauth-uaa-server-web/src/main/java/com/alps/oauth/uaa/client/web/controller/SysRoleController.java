package com.alps.oauth.uaa.client.web.controller;

import com.alps.common.annotation.Log;
import com.alps.common.constant.UserConstants;
import com.alps.common.core.domain.AjaxResult;
import com.alps.common.core.domain.ResultBody;
import com.alps.common.core.page.TableDataInfo;
import com.alps.common.enums.BusinessType;
import com.alps.base.api.model.entity.SysRole;
import com.alps.base.api.model.entity.SysRoleUser;
import com.alps.base.api.model.entity.SysUser;
import com.alps.common.oauth2.utils.StringUtils;
import com.alps.oauth.uaa.client.web.model.PageParams;
import com.alps.oauth.uaa.client.web.service.ISysCompanyService;
import com.alps.oauth.uaa.client.web.service.ISysRoleService;
import com.alps.oauth.uaa.client.web.service.ISysUserService;
import com.alps.oauth.uaa.client.web.utils.WebUserHelper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
* <p>Title: SysRoleController</p>  

* <p>RBAC 角色控制: </p>  

* @author YJ.lee  

* @date 2020年5月20日
 */
@Api(tags = "系统角色管理")
@Controller
public class SysRoleController extends BaseController{
	private String prefix = "system/role";
    @Autowired
    private ISysRoleService roleService;
    @Autowired
    private ISysCompanyService sysCompanyService;
    
    @Autowired
    private ISysUserService userService;

    
    @GetMapping("/system/role")
    public String role()
    {
        return prefix + "/role";
    }
    
    @PostMapping("/system/role/list")
    @ResponseBody
    public TableDataInfo list(SysRole role)
    {
        List<SysRole> list = roleService.findAllList(role);
        return getDataTable(list);
    }
    
    /**
     * 新增角色
     */
    @GetMapping("/system/role/add")
    public String add(ModelMap mmap)
    {
    	mmap.put("companyList", sysCompanyService.list()) ;
        return prefix + "/add";
    }

    /**
     * 新增保存角色
     */
    @Log(title = "角色管理", businessType = BusinessType.INSERT)
    @PostMapping("/system/role/add")
    @ResponseBody
    public AjaxResult addSave(SysRole role)
    {
        role.setCreateBy(WebUserHelper.getUser().getUserId().toString());
        return toAjax(roleService.insertRole(role) );

    }

    /**
     * 修改角色
     */
    @GetMapping("/system/role/edit/{roleId}")
    public String edit(@PathVariable("roleId") Long roleId, ModelMap mmap)
    {
        mmap.put("role", roleService.getRole(roleId));
        mmap.put("companyList", sysCompanyService.list()) ;
        return prefix + "/edit";
    }
    

    /**
     * 修改保存角色
     */
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PostMapping("/system/role/edit")
    @ResponseBody
    public AjaxResult editSave(SysRole role)
    {
        role.setUpdateBy(WebUserHelper.getUser().getUserId().toString());
        return toAjax(roleService.updateRole(role));
    } 
    
    @PostMapping("/system/role/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        try
        {
            return toAjax(roleService.removeRole(ids));
        }
        catch (Exception e)
        {
            return error(e.getMessage());
        }
    }
    
    
    /**
     * 分配用户
     */
    @GetMapping("/system/role/authUser/{roleId}")
    public String authUser(@PathVariable("roleId") Long roleId, ModelMap mmap)
    {
        mmap.put("role", roleService.getById(roleId) );
        return prefix + "/authUser";
    }
    

    /**
     * 选择用户
     */
    @GetMapping("/system/role/authUser/selectUser/{roleId}")
    public String selectUser(@PathVariable("roleId") Long roleId, ModelMap mmap)
    {
        mmap.put("role", roleService.getById(roleId));
        return prefix + "/selectUser";
    }
    
    /**
     * 批量选择用户授权
     */
    @PostMapping("/system/role/authUser/selectAll")
    @ResponseBody
    public AjaxResult selectAuthUserAll(Long roleId, String userIds)
    {
        return toAjax(roleService.insertRoleUsers(roleId, userIds));
    }
    
    /**
     * 取消授权
     */
    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @PostMapping("/system/role/authUser/cancel")
    @ResponseBody
    public AjaxResult cancelAuthUser(SysRoleUser sysUser)
    {
        return toAjax(roleService.deleteAuthUser(sysUser));
    }

    /**
     * 批量取消授权
     */
    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @PostMapping("/system/role/authUser/cancelAll")
    @ResponseBody
    public AjaxResult cancelAuthUserAll(Long roleId, String userIds)
    {
        return toAjax(roleService.deleteAuthUsers(roleId, userIds));
    }
    
    
    /**
     * 查询已分配用户角色列表
     */
    @PostMapping("/system/role/authUser/allocatedList")
    @ResponseBody
    public TableDataInfo allocatedList(SysUser user)
    {
        startPage();
        List<SysUser> list = userService.selectAuthorityUserList(user);
        return getDataTable(list);
    }
    
    /**
     * 查询已分配用户角色列表
     */
    @PostMapping("/system/role/authUser/unallocatedList")
    @ResponseBody
    public TableDataInfo unallocatedList(SysUser user)
    {
    	startPage();
    	List<SysUser> list = userService.selectUnAuthorityUserList(user);
    	return getDataTable(list);
    }
    
    /**
     * 校验角色名称
     */
    @PostMapping("/system/role/checkRoleCodeUnique")
    @ResponseBody
    public String checkRoleCodeUnique(SysRole role)
    {
    	return  roleService.isExist(role.getRoleCode()) ? UserConstants.ROLE_NAME_NOT_UNIQUE :UserConstants.ROLE_NAME_UNIQUE;
    }

    
    
    /**
     * 获取分页角色列表
     *
     * @return
     */
    @ApiOperation(value = "获取分页角色列表", notes = "获取分页角色列表")
    @GetMapping("/role")
    public ResultBody<IPage<SysRole>> getRoleListPage(@RequestParam(required = false) Map map) {
        return ResultBody.ok().data(roleService.findListPage(new PageParams(map)));
    }

    /**
     * 获取所有角色列表
     *
     * @return
     */
    @ApiOperation(value = "获取所有角色列表", notes = "获取所有角色列表")
    @GetMapping("/role/all")
    public ResultBody<List<SysRole>> getRoleAllList() {
    	SysRole role  = new SysRole();
        return ResultBody.ok().data(roleService.findAllList(role));
    }

    /**
     * 获取角色详情
     *
     * @param roleId
     * @return
     */
    @ApiOperation(value = "获取角色详情", notes = "获取角色详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", defaultValue = "", required = true, paramType = "path")
    })
    @GetMapping("/role/{roleId}/info")
    public ResultBody<SysRole> getRole(@PathVariable(value = "roleId") Long roleId) {
        SysRole result = roleService.getRole(roleId);
        return ResultBody.ok().data(result);
    }

    /**
     * 添加角色
     *
     * @param roleCode 角色编码
     * @param roleName 角色显示名称
     * @param roleDesc 描述
     * @param status   启用禁用
     * @return
     */
    @ApiOperation(value = "添加角色", notes = "添加角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleCode", value = "角色编码", defaultValue = "", required = true, paramType = "form"),
            @ApiImplicitParam(name = "roleName", value = "角色显示名称", defaultValue = "", required = true, paramType = "form"),
            @ApiImplicitParam(name = "roleDesc", value = "描述", defaultValue = "", required = false, paramType = "form"),
            @ApiImplicitParam(name = "status", required = true, defaultValue = "1", allowableValues = "0,1", value = "是否启用", paramType = "form")
    })
    @PostMapping("/role/add")
    public ResultBody<Long> addRole(
            @RequestParam(value = "roleCode") String roleCode,
            @RequestParam(value = "roleName") String roleName,
            @RequestParam(value = "roleDesc", required = false) String roleDesc,
            @RequestParam(value = "status", defaultValue = "1", required = false) Integer status
    ) {
        SysRole role = new SysRole();
        role.setRoleCode(roleCode);
        role.setRoleName(roleName);
        role.setStatus(status);
        role.setRoleDesc(roleDesc);
        Long roleId = null;
        SysRole result = roleService.addRole(role);
        if (result != null) {
            roleId = result.getRoleId();
        }
        return ResultBody.ok().data(roleId);
    }

    /**
     * 编辑角色
     *
     * @param roleId   角色ID
     * @param roleCode 角色编码
     * @param roleName 角色显示名称
     * @param roleDesc 描述
     * @param status   启用禁用
     * @return
     */
    @ApiOperation(value = "编辑角色", notes = "编辑角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", defaultValue = "", required = true, paramType = "form"),
            @ApiImplicitParam(name = "roleCode", value = "角色编码", defaultValue = "", required = true, paramType = "form"),
            @ApiImplicitParam(name = "roleName", value = "角色显示名称", defaultValue = "", required = true, paramType = "form"),
            @ApiImplicitParam(name = "roleDesc", value = "描述", defaultValue = "", required = false, paramType = "form"),
            @ApiImplicitParam(name = "status", required = true, defaultValue = "1", allowableValues = "0,1", value = "是否启用", paramType = "form")
    })
    @PostMapping("/role/update")
    public ResultBody updateRole(
            @RequestParam(value = "roleId") Long roleId,
            @RequestParam(value = "roleCode") String roleCode,
            @RequestParam(value = "roleName") String roleName,
            @RequestParam(value = "roleDesc", required = false) String roleDesc,
            @RequestParam(value = "status", defaultValue = "1", required = false) Integer status
    ) {
        SysRole role = new SysRole();
        role.setRoleId(roleId);
        role.setRoleCode(roleCode);
        role.setRoleName(roleName);
        role.setStatus(status);
        role.setRoleDesc(roleDesc);
        int n = roleService.updateRole(role);
        if(n>0) {
        	return ResultBody.ok();
        }else {
        	 return ResultBody.failed();
        }
       
    }

    /**
     * 角色添加成员
     * @param roleId
     * @param userIds
     * @return
     */
    @ApiOperation(value = "角色添加成员", notes = "角色添加成员")
    @PostMapping("/role/users/add")
    public ResultBody addUserRoles(
            @RequestParam(value = "roleId") Long roleId,
            @RequestParam(value = "userIds", required = false) String userIds
    ) {
        roleService.saveRoleUsers(roleId, StringUtils.isNotBlank(userIds) ? userIds.split(",") : new String[]{});
        return ResultBody.ok();
    }

    /**
     * 查询角色成员
     *
     * @param roleId
     * @return
     */
    @ApiOperation(value = "查询角色成员", notes = "查询角色成员")
    @GetMapping("/role/users")
    public ResultBody<List<SysRoleUser>> getRoleUsers(
            @RequestParam(value = "roleId") Long roleId
    ) {
        return ResultBody.ok().data(roleService.findRoleUsers(roleId));
    }

}
