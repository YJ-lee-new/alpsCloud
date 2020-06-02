package com.alps.oauth.uaa.client.web.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alps.base.api.model.entity.SysDept;
import com.alps.base.api.model.entity.SysRole;
import com.alps.base.api.model.entity.SysUser;
import com.alps.common.annotation.Log;
import com.alps.common.core.domain.AjaxResult;
import com.alps.common.core.page.TableDataInfo;
import com.alps.common.enums.BusinessType;
import com.alps.common.utils.StringUtils;
import com.alps.oauth.uaa.client.web.service.ISysDeptService;
import com.alps.oauth.uaa.client.web.service.ISysRoleService;
import com.alps.oauth.uaa.client.web.service.ISysUserService;
import com.alps.oauth.uaa.client.web.utils.WebUserHelper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 

* <p>Title: SysUserController</p>  

* <p>Description: </p>  

* @author YJ.lee  

* @date 2020年4月25日
 */
@Api(tags = "系统用户管理")
@Controller
@RequestMapping("/system/user")
public class SysUserController extends BaseController 
{
    private String prefix = "system/user";
    
    @Autowired
    private ISysUserService userService;
    
    @Autowired
    private ISysRoleService roleService;
    
    @Autowired
    private ISysDeptService sysDeptService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping()
    public String user()
    {
        return prefix + "/user";
    }

    @ApiOperation(value = "系统分页用户列表", notes = "系统分页用户列表")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysUser sysUser)
    {
        startPage();
        List<SysUser> list = userService.findUserList(sysUser);
        return getDataTable(list);
    }

    /**
     * 新增用户
     */
    @GetMapping("/add")
    public String add(ModelMap mmap)
    {
    	SysDept dept  = new SysDept();
    	SysRole role  = new SysRole();
        mmap.put("roles", roleService.findAllList(role));
        mmap.put("dept", sysDeptService.selectDeptTree(dept));
        return prefix + "/add";
    }

    
    /**
     * 新增保存用户
     */
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysUser user)
    {
    	 if ("@superAdmin.com" == user.getDomain())
         {
             return error("不允许建立超级管理员用户");
         }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreateBy(WebUserHelper.getUser().getUserId().toString() );
        return toAjax(userService.addUser(user));
    }

    /**
     * Check unique
     *   
     * @param account
     * @return
     */
    @PostMapping("/checkLoginNameUnique")
    @ResponseBody
    public String checkLoginNameUnique(String account)
    {
        return userService.getUserByUsername(account) > 0 ? "1" :"0";
    }
    /**
     * Check unique
     *   
     * @param account
     * @return
     */
    @PostMapping("/checkEmailUnique")
    @ResponseBody
    public String checkEmailUnique(String email)
    {
    	return userService.getUserByEmail(email) > 0 ? "1" :"0";
    }
    /**
     * Check unique
     *   
     * @param account
     * @return
     */
    @PostMapping("/checkMobileUnique")
    @ResponseBody
    public String checkMobileUnique(String mobile)
    {
    	return userService.getUserByMobile(mobile) > 0 ? "1" :"0";
    }
    /**
     * 修改用户
     */
    @GetMapping("/edit/{userId}")
    public String edit(@PathVariable("userId") Long userId, ModelMap mmap)
    {
    	SysDept dept  = new SysDept();
        mmap.put("user", userService.selectUserById(userId));
        mmap.put("roles", roleService.getUserRoles(userId));
        mmap.put("dept", sysDeptService.selectDeptTree(dept));
        return prefix + "/edit";
    }

    /**
     * 修改保存用户
     */
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysUser user)
    {
        if ("@superAdmin.com" == user.getDomain())
        {
            return error("不允许修改超级管理员用户");
        }
        user.setUpdateBy(WebUserHelper.getUser().getUserId().toString());
        return toAjax(userService.updateUser(user));
    }
    
    @Log(title = "用户管理", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        try
        {
            return toAjax(userService.deleteUserByIds(ids) );
        }
        catch (Exception e)
        {
            return error(e.getMessage());
        }
    }
    
    

    @Log(title = "重置密码", businessType = BusinessType.UPDATE)
    @GetMapping("/resetPwd/{userId}")
    public String resetPwd(@PathVariable("userId") Long userId, ModelMap mmap)
    {
        mmap.put("user", userService.getById(userId));
        return prefix + "/resetPwd";
    }

    @Log(title = "重置密码", businessType = BusinessType.UPDATE)
    @PostMapping("/resetPwd")
    @ResponseBody
    public AjaxResult resetPwdSave(SysUser user)
    {
        return toAjax(userService.updatePassword(user.getUserId(),user.getPassword()));
    }
}