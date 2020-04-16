package com.alps.oauth.server.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alps.base.api.model.entity.BaseMenu;
import com.alps.base.api.model.vo.RouterVo;
import com.alps.common.core.domain.ResultBody;
import com.alps.base.api.model.UserAccount;
import com.alps.base.api.model.entity.BaseRole;
import com.alps.base.api.model.entity.BaseSysUser;
import com.alps.base.api.service.IBaseUserServiceClient;
import com.alps.common.core.domain.ReturnResult;
import com.alps.common.oauth2.security.AlpsHelper;
import com.alps.common.oauth2.security.AlpsUserDetails;
import com.alps.common.oauth2.utils.StringUtils;
import com.alps.oauth.server.model.PageParams;
import com.alps.oauth.server.service.MenuService;
import com.alps.oauth.server.service.RoleService;
import com.alps.oauth.server.service.UserService;
import com.alps.platform.log.annotation.LogAnnotation;
import com.alps.provider.system.domain.SysUser;
import com.baomidou.mybatisplus.core.metadata.IPage;

import com.github.pagehelper.PageInfo;
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
public class UserController implements IBaseUserServiceClient {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private MenuService menuService;



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
    @LogAnnotation(module = "base")
    public ResultBody<UserAccount> userLogin(@RequestParam(value = "username") String username) {
        UserAccount account = userService.login(username);
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
    public ResultBody getMenus(String companyId)
    {
        AlpsUserDetails loginUser=AlpsHelper.getUser();
        // 权限集合
        List<BaseMenu> menus = menuService.selectMenuTreeByUserId(loginUser.getUserId(),companyId);
        return new ResultBody().data(menuService.buildMenus(menus));
    }

    /**
     * 系统分页用户列表
     *
     * @return
     */
    @ApiOperation(value = "系统分页用户列表", notes = "系统分页用户列表")
    @GetMapping("/user")
    public ResultBody<IPage<BaseSysUser>> getUserList(@RequestParam(required = false) Map map) {
        return ResultBody.ok().data(userService.findListPage(new PageParams(map)));
    }

    /**
     * 获取所有用户列表
     *
     * @return
     */
    @ApiOperation(value = "获取所有用户列表", notes = "获取所有用户列表")
    @GetMapping("/user/all")
    public ResultBody<List<BaseRole>> getUserAllList() {
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
            @RequestBody BaseSysUser user
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
    	BaseSysUser user = new BaseSysUser();
        user.setUserId(userId);
        user.setNickName(nickName);
        user.setUserType(userType);
        user.setEmail(email);
        user.setMobile(mobile);
        user.setUserDesc(userDesc);
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
    public ResultBody<List<BaseRole>> getUserRoles(
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
    	BaseSysUser user = new BaseSysUser();
        user.setNickName(nickName);
        user.setUserName(account);
        user.setPassword(password);
        user.setAvatar(avatar);
        userService.addUserThirdParty(user, accountType);
        return ResultBody.ok();
    }



}
