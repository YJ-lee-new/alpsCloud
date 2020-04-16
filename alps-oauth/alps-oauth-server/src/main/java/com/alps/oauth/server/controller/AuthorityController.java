package com.alps.oauth.server.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alps.base.api.model.Authority2Api;
import com.alps.base.api.model.AuthorityApi;
import com.alps.base.api.model.AuthorityMenu;
import com.alps.base.api.model.AuthorityResource;
import com.alps.common.core.domain.ResultBody;
import com.alps.base.api.model.entity.BaseAuthorityAction;
import com.alps.base.api.model.entity.BaseSysUser;
import com.alps.base.api.service.IBaseAuthorityServiceClient;
import com.alps.common.oauth2.config.CommonConstants;
import com.alps.common.oauth2.security.AlpsAuthority;
import com.alps.common.oauth2.utils.StringUtils;
import com.alps.oauth.server.service.AuthorityService;
import com.alps.oauth.server.service.UserService;
import com.alps.common.oauth2.constants.AlpsRestTemplate;

import java.util.Date;
import java.util.List;
/**
 * @author:Yujie.lee
 * Date:2019年12月7日
 * TodoTODO
 */
@Api(tags = "系统权限管理")
@RestController
public class AuthorityController implements IBaseAuthorityServiceClient {

    @Autowired
    private AuthorityService authorityService;
    @Autowired
    private UserService userService;
    @Autowired
    private AlpsRestTemplate alpsRestTemplate;

    /**
     * 获取所有访问权限列表
     *
     * @return
     */
    @ApiOperation(value = "获取所有访问权限列表", notes = "获取所有访问权限列表")
    @GetMapping("/authority/access")
    @Override
    public ResultBody<List<AuthorityResource>> findAuthorityResource() {
        List<AuthorityResource> result = authorityService.findAuthorityResource();
        return ResultBody.ok().data(result);
    }

    /**
     * 获取权限列表
     *
     * @return
     */
    @ApiOperation(value = "获取接口权限列表", notes = "获取接口权限列表")
    @GetMapping("/authority/api")
    public ResultBody<List<AuthorityApi>> findAuthorityApi(
            @RequestParam(value = "serviceId", required = false) String serviceId
    ) {
        List<AuthorityApi> result = authorityService.findAuthorityApi(serviceId);
        return ResultBody.ok().data(result);
    }
    
    /**
     * 获取用户对应角色下面所有接口例表
     */
    
    @ApiOperation(value = "获取接口列表", notes = "获取接口列表")
    @GetMapping("/authority/apiList")
    public ResultBody<List<Authority2Api>> findAuthorityApiList(
            @RequestParam(value = "userName", required = false) String userName
    ) {
        List<Authority2Api> result = authorityService.findAuthorityApiList(userName);
        return ResultBody.ok().data(result);
    }


    /**
     * 获取菜单权限列表
     *
     * @return
     */
    @ApiOperation(value = "获取菜单权限列表", notes = "获取菜单权限列表")
    @GetMapping("/authority/menu")
    @Override
    public ResultBody<List<AuthorityMenu>> findAuthorityMenu() {
        List<AuthorityMenu> result = authorityService.findAuthorityMenu(1);
        return ResultBody.ok().data(result);
    }

    /**
     * 获取功能权限列表
     *
     * @param actionId
     * @return
     */
    @ApiOperation(value = "获取功能权限列表", notes = "获取功能权限列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "actionId", required = true, value = "功能按钮ID", paramType = "form")
    })
    @GetMapping("/authority/action")
    public ResultBody<List<BaseAuthorityAction>> findAuthorityAction(
            @RequestParam(value = "actionId") Long actionId
    ) {
        List<BaseAuthorityAction> list = authorityService.findAuthorityAction(actionId);
        return ResultBody.ok().data(list);
    }


    /**
     * 获取角色已分配权限
     *
     * @param roleId 角色ID
     * @return
     */
    @ApiOperation(value = "获取角色已分配权限", notes = "获取角色已分配权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", defaultValue = "", required = true, paramType = "form")
    })
    @GetMapping("/authority/role")
    public ResultBody<List<AlpsAuthority>> findAuthorityRole(Long roleId) {
        List<AlpsAuthority> result = authorityService.findAuthorityByRole(roleId);
        return ResultBody.ok().data(result);
    }


    /**
     * 获取用户已分配权限
     *
     * @param userId 用户ID
     * @return
     */
    @ApiOperation(value = "获取用户已分配权限", notes = "获取用户已分配权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", defaultValue = "", required = true, paramType = "form")
    })
    @GetMapping("/authority/user")
    public ResultBody<List<AlpsAuthority>> findAuthorityUser(
            @RequestParam(value = "userId") Long userId
    ) {
        BaseSysUser user = userService.getUserById(userId);
        List<AlpsAuthority> result = authorityService.findAuthorityByUser(userId, CommonConstants.ROOT.equals(user.getUserName()));
        return ResultBody.ok().data(result);
    }


    /**
     * 获取应用已分配接口权限
     *
     * @param appId 角色ID
     * @return
     */
    @ApiOperation(value = "获取应用已分配接口权限", notes = "获取应用已分配接口权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appId", value = "应用Id", defaultValue = "", required = true, paramType = "form")
    })
    @GetMapping("/authority/app")
    public ResultBody<List<AlpsAuthority>> findAuthorityApp(
            @RequestParam(value = "appId") String appId
    ) {
        List<AlpsAuthority> result = authorityService.findAuthorityByApp(appId);
        return ResultBody.ok().data(result);
    }

    /**
     * 分配角色权限
     *
     * @param roleId       角色ID
     * @param expireTime   授权过期时间
     * @param authorityIds 权限ID.多个以,隔开
     * @return
     */
    @ApiOperation(value = "分配角色权限", notes = "分配角色权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", defaultValue = "", required = true, paramType = "form"),
            @ApiImplicitParam(name = "expireTime", value = "过期时间.选填", defaultValue = "", required = false, paramType = "form"),
            @ApiImplicitParam(name = "authorityIds", value = "权限ID.多个以,隔开.选填", defaultValue = "", required = false, paramType = "form")
    })
    @PostMapping("/authority/role/grant")
    public ResultBody grantAuthorityRole(
            @RequestParam(value = "roleId") Long roleId,
            @RequestParam(value = "expireTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date expireTime,
            @RequestParam(value = "authorityIds", required = false) String authorityIds
    ) {
        authorityService.addAuthorityRole(roleId, expireTime, StringUtils.isNotBlank(authorityIds) ? authorityIds.split(",") : new String[]{});
        alpsRestTemplate.refreshGateway();
        return ResultBody.ok();
    }


    /**
     * 分配用户权限
     *
     * @param userId       用户ID
     * @param expireTime   授权过期时间
     * @param authorityIds 权限ID.多个以,隔开
     * @return
     */
    @ApiOperation(value = "分配用户权限", notes = "分配用户权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", defaultValue = "", required = true, paramType = "form"),
            @ApiImplicitParam(name = "expireTime", value = "过期时间.选填", defaultValue = "", required = false, paramType = "form"),
            @ApiImplicitParam(name = "authorityIds", value = "权限ID.多个以,隔开.选填", defaultValue = "", required = false, paramType = "form")
    })
    @PostMapping("/authority/user/grant")
    public ResultBody grantAuthorityUser(
            @RequestParam(value = "userId") Long userId,
            @RequestParam(value = "expireTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date expireTime,
            @RequestParam(value = "authorityIds", required = false) String authorityIds
    ) {
        authorityService.addAuthorityUser(userId, expireTime, StringUtils.isNotBlank(authorityIds) ? authorityIds.split(",") : new String[]{});
        alpsRestTemplate.refreshGateway();
        return ResultBody.ok();
    }


    /**
     * 分配应用权限
     *
     * @param appId        应用Id
     * @param expireTime   授权过期时间
     * @param authorityIds 权限ID.多个以,隔开
     * @return
     */
    @ApiOperation(value = "分配应用权限", notes = "分配应用权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appId", value = "应用Id", defaultValue = "", required = true, paramType = "form"),
            @ApiImplicitParam(name = "expireTime", value = "过期时间.选填", defaultValue = "", required = false, paramType = "form"),
            @ApiImplicitParam(name = "authorityIds", value = "权限ID.多个以,隔开.选填", defaultValue = "", required = false, paramType = "form")
    })
    @PostMapping("/authority/app/grant")
    public ResultBody grantAuthorityApp(
            @RequestParam(value = "appId") String appId,
            @RequestParam(value = "expireTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date expireTime,
            @RequestParam(value = "authorityIds", required = false) String authorityIds
    ) {
        authorityService.addAuthorityApp(appId, expireTime, StringUtils.isNotBlank(authorityIds) ? authorityIds.split(",") : new String[]{});
        alpsRestTemplate.refreshGateway();
        return ResultBody.ok();
    }

    /**
     * 功能按钮绑定API
     *
     * @param actionId
     * @param authorityIds
     * @return
     */
    @ApiOperation(value = "功能按钮授权", notes = "功能按钮授权")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "actionId", required = true, value = "功能按钮ID", paramType = "form"),
            @ApiImplicitParam(name = "authorityIds", required = false, value = "全新ID:多个用,号隔开", paramType = "form"),
    })
    @PostMapping("/authority/action/grant")
    public ResultBody grantAuthorityAction(
            @RequestParam(value = "actionId") Long actionId,
            @RequestParam(value = "authorityIds", required = false) String authorityIds
    ) {
        authorityService.addAuthorityAction(actionId, StringUtils.isNotBlank(authorityIds) ? authorityIds.split(",") : new String[]{});
        alpsRestTemplate.refreshGateway();
        return ResultBody.ok();
    }


}
