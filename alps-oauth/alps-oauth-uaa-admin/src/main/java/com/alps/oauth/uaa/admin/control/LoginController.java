package com.alps.oauth.uaa.admin.control;

import com.alps.base.api.model.entity.SysMenu;
import com.alps.base.api.model.entity.SysUser;
import com.alps.common.core.domain.ResultBody;
import com.alps.common.oauth2.security.AlpsHelper;
import com.alps.common.oauth2.security.AlpsUserDetails;
import com.alps.oauth.uaa.admin.client.AlpsOAuth2ClientDetails;
import com.alps.oauth.uaa.admin.client.AlpsOAuth2ClientProperties;

import com.alps.oauth.uaa.admin.feign.BaseUserServiceClient;
import com.alps.oauth.uaa.admin.serivceImp.ClientDetailsServiceImpl;
import com.alps.oauth.uaa.admin.service.AppRedisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerEndpointsConfiguration;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author:Yujie.lee
 * Date:2019年12月7日
 * TodoTODO
 */
@Api(tags = "用户认证中心")
@RestController
public class LoginController {
    @Autowired
    private AppRedisService appRedisService;
    @Autowired
    private TokenStore tokenStore;
    @Autowired
    AuthorizationServerEndpointsConfiguration endpoints;
    @Autowired
    private BaseUserServiceClient userServiceClient;
    /**
     * 获取用户基础信息
     *
     * @return
     */
    @ApiOperation(value = "获取当前登录用户信息", notes = "获取当前登录用户信息")
    @GetMapping("/current/user")
    public ResultBody getUserProfile() {
        return ResultBody.ok().data(AlpsHelper.getUser());
    }


    /**
     * 获取当前登录用户信息-SSO单点登录
     *
     * @param principal
     * @return
     */
    @ApiOperation(value = "获取当前登录用户信息-SSO单点登录", notes = "获取当前登录用户信息-SSO单点登录")
    @GetMapping("/current/user/sso")
    public Principal principal(Principal principal) {
        return principal;
    }

    /**
     * 获取用户访问令牌
     * 基于oauth2密码模式登录
     *
//     * @param username
//     * @param password
     * @return access_token
     */
    @ApiOperation(value = "登录获取用户访问令牌", notes = "基于oauth2密码模式登录,无需签名,返回access_token")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "username", required = true, value = "登录名", paramType = "form"),
//            @ApiImplicitParam(name = "password", required = true, value = "登录密码", paramType = "form")
//    })
    @PostMapping("/client/token")
    public ResultBody<OAuth2AccessToken> getLoginToken(@RequestBody SysUser user) throws Exception {
        OAuth2AccessToken result = appRedisService.getToken(user.getUserName(), user.getPassword(), null,null);
       return ResultBody.ok().data(result);
    }

    /**
     * 获取用户 角色 权限  信息
     *
     * @return 用户信息
     */
    @GetMapping("/user/getInfo")
    public ResultBody getInfo()
    {
        return userServiceClient.getInfo();
    }


    /**
     * 获取用户 角色 权限  信息
     *
     * @return 用户信息
     */
    @PostMapping("/user/getMenus")
    public ResultBody getMenus(String userName, String companyId)
    {
        return userServiceClient.getMenus(userName,companyId);
    }

    /**
     * 退出移除令牌
     *
     * @param token
     */
    @ApiOperation(value = "退出并移除令牌", notes = "退出并移除令牌,令牌将失效")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", required = true, value = "访问令牌", paramType = "form")
    })
    @PostMapping("/logout/token")
    public ResultBody removeToken(@RequestParam String token) {
        tokenStore.removeAccessToken(tokenStore.readAccessToken(token));
        return ResultBody.ok();
    }

}
