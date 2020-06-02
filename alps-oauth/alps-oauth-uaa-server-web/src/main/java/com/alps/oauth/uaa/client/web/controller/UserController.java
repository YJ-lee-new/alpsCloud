package com.alps.oauth.uaa.client.web.controller;

import com.alps.base.api.model.entity.SysMenu;
import com.alps.common.core.domain.ResultBody;
import com.alps.base.api.model.UserAccount;
import com.alps.base.api.model.entity.SysRole;
import com.alps.base.api.model.entity.SysUser;
import com.alps.base.api.service.ISysUserServiceClient;
import com.alps.common.core.domain.ReturnResult;
import com.alps.common.oauth2.security.AlpsHelper;
import com.alps.common.oauth2.security.AlpsUserDetails;
import com.alps.common.oauth2.utils.StringUtils;
import com.alps.common.oauth2.utils.WebUtils;
import com.alps.oauth.uaa.client.web.model.PageParams;
import com.alps.oauth.uaa.client.web.service.ISysMenuService;
import com.alps.oauth.uaa.client.web.service.ISysRoleService;
import com.alps.oauth.uaa.client.web.service.ISysUserService;
import com.baomidou.mybatisplus.core.metadata.IPage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 系统用户信息
 *
 * @author YJ.lee
 */
@Api(tags = "系统用户管理")
@RestController
public class UserController implements ISysUserServiceClient {
    @Autowired
    private ISysUserService userService;
    @Autowired
    private ISysRoleService roleService;
    @Autowired
    private ISysMenuService menuService;



    /**
     * 获取登录账号信息
     *
     * @param username 登录名
     * @return
     */
    @ApiOperation(value = "获取账号登录信息", notes = "仅限系统内部调用")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", required = true, value = "登录名", paramType = "path"),
    })
    @PostMapping("/user/login")
    @Override
    public ResultBody<UserAccount> userLogin(
    		@RequestParam(value = "username") String username
    		) {
	    Map<String, String> parameterMap = WebUtils.getParameterMap(WebUtils.getHttpServletRequest());
        String loginType = parameterMap.get("login_type");
        String domain = parameterMap.get("domain");
        UserAccount account = userService.login(username,loginType,domain);
        return ResultBody.ok().data(account);
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @Override
    @GetMapping("/user/getInfo")
    public ResultBody getInfo()
    {
        return userService.getInfo();
    }


    /**
     * 获取用户 角色 权限  信息
     *
     * @return 用户信息
     */
    @Override
    @PostMapping("/user/getMenus")
    public ResultBody getMenus(String userName, String companyId)
    {
        // 权限集合
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(userName,companyId);
        return new ResultBody().data(menus);
    }

    /**
     * 系统分页用户列表
     *
     * @return
     */
    @ApiOperation(value = "系统分页用户列表", notes = "系统分页用户列表")
    @GetMapping("/user")
    public ResultBody<IPage<SysUser>> getUserList(@RequestParam(required = false) Map map) {
        return ResultBody.ok().data(userService.findListPage(new PageParams(map)));
    }

    /**
     * 获取所有用户列表
     *
     * @return
     */
    @ApiOperation(value = "获取所有用户列表", notes = "获取所有用户列表")
    @GetMapping("/user/all")
    public ResultBody<List<SysRole>> getUserAllList() {
        return ResultBody.ok().data(userService.findAllList());
    }


    /**
     * 添加系统用户
     *
//     * @param userName
//     * @param password
//     * @param nickName
//     * @param status
//     * @param userType
//     * @param email
//     * @param mobile
//     * @param userDesc
//     * @param avatar
     * @return
     */
    @ApiOperation(value = "添加系统用户", notes = "添加系统用户")
    @PostMapping("/user/add")
    public ReturnResult<?> addUser(
//    public ResultBody<Long> addUser(
//            @RequestParam(value = "userName") String userName,
//            @RequestParam(value = "password") String password,
//            @RequestParam(value = "nickName") String nickName,
//            @RequestParam(value = "status") Integer status,
//            @RequestParam(value = "userType") String userType,
//            @RequestParam(value = "email", required = false) String email,
//            @RequestParam(value = "mobile", required = false) String mobile,
//            @RequestParam(value = "userDesc", required = false) String userDesc,
//            @RequestParam(value = "avatar", required = false) String avatar
            @RequestBody SysUser user
    ) {
//    	BaseSysUser user = new BaseSysUser();
//        user.setUserName(userName);
//        user.setPassword(password);
//        user.setNickName(nickName);
//        user.setUserType(userType);
//        user.setEmail(email);
//        user.setMobile(mobile);
//        user.setUserDesc(userDesc);
//        user.setAvatar(avatar);
//        user.setStatus(status);
        userService.addUser(user);
//        return ResultBody.ok();
        return ReturnResult.ok("操作成功");
    }

    /**
     * 更新系统用户
     *
     * @param userId
     * @param nickName
     * @param status
     * @param userType
     * @param email
     * @param mobile
     * @param userDesc
     * @param avatar
     * @return
     */
    @ApiOperation(value = "更新系统用户", notes = "更新系统用户")
    @PostMapping("/user/update")
    public ResultBody updateUser(
            @RequestParam(value = "userId") Long userId,
            @RequestParam(value = "nickName") String nickName,
            @RequestParam(value = "status") Integer status,
            @RequestParam(value = "userType") String userType,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "mobile", required = false) String mobile,
            @RequestParam(value = "userDesc", required = false) String userDesc,
            @RequestParam(value = "avatar", required = false) String avatar
    ) {
    	SysUser user = new SysUser();
        user.setUserId(userId);
        user.setUserName(nickName);
        user.setUserType(userType);
        user.setEmail(email);
        user.setMobile(mobile);
        user.setAvatar(avatar);
        user.setStatus(status);
        userService.updateUser(user);
        return ResultBody.ok();
    }


    /**
     * 修改用户密码
     *
     * @param userId
     * @param password
     * @return
     */
    @ApiOperation(value = "修改用户密码", notes = "修改用户密码")
    @PostMapping("/user/update/password")
    public ResultBody updatePassword(
            @RequestParam(value = "userId") Long userId,
            @RequestParam(value = "password") String password
    ) {
        userService.updatePassword(userId, password);
        return ResultBody.ok();
    }

    /**
     * 用户分配角色
     *
     * @param userId
     * @param roleIds
     * @return
     */
    @ApiOperation(value = "用户分配角色", notes = "用户分配角色")
    @PostMapping("/user/roles/add")
    public ResultBody addUserRoles(
            @RequestParam(value = "userId") Long userId,
            @RequestParam(value = "roleIds", required = false) String roleIds
    ) {
        roleService.saveUserRoles(userId, StringUtils.isNotBlank(roleIds) ? roleIds.split(",") : new String[]{});
        return ResultBody.ok();
    }

    /**
     * 获取用户角色
     *
     * @param userId
     * @return
     */
    @ApiOperation(value = "获取用户已分配角色", notes = "获取用户已分配角色")
    @GetMapping("/user/roles")
    public ResultBody<List<SysRole>> getUserRoles(
            @RequestParam(value = "userId") Long userId
    ) {
        return ResultBody.ok().data(roleService.getUserRoles(userId));
    }


    /**
     * 注册第三方系统登录账号
     *
     * @param account
     * @param password
     * @param accountType
     * @return
     */
    @ApiOperation(value = "注册第三方系统登录账号", notes = "仅限系统内部调用")
    @PostMapping("/user/add/thirdParty")
    @Override
    public ResultBody addUserThirdParty(
            @RequestParam(value = "account") String account,
            @RequestParam(value = "password") String password,
            @RequestParam(value = "accountType") String accountType,
            @RequestParam(value = "nickName") String nickName,
            @RequestParam(value = "avatar") String avatar
    ) {
    	SysUser user = new SysUser();
        user.setUserName(nickName);
        user.setUserName(account);
        user.setPassword(password);
        user.setAvatar(avatar);
        userService.addUserThirdParty(user, accountType);
        return ResultBody.ok();
    }



}
