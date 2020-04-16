package com.alps.oauth.server.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alps.base.api.model.AuthorityMenu;
import com.alps.base.api.model.entity.BaseSysUser;
import com.alps.common.core.domain.ResultBody;
import com.alps.common.oauth2.config.CommonConstants;
import com.alps.common.oauth2.security.AlpsHelper;
import com.alps.common.oauth2.security.AlpsUserDetails;
import com.alps.oauth.server.service.AuthorityService;
import com.alps.oauth.server.service.UserService;

import java.util.List;
/**
 * @author:Yujie.lee
 * Date:2019年12月7日
 * TodoTODO
 */
@Api(tags = "当前登陆用户")
@RestController
public class CurrentUserController {

    @Autowired
    private UserService userService;
    @Autowired
    private AuthorityService authorityService;
    @Autowired
    private RedisTokenStore redisTokenStore;
    /**
     * 修改当前登录用户密码
     *
     * @return
     */
    @ApiOperation(value = "修改当前登录用户密码", notes = "修改当前登录用户密码")
    @GetMapping("/current/user/rest/password")
    public ResultBody restPassword(@RequestParam(value = "password") String password) {
        userService.updatePassword(AlpsHelper.getUser().getUserId(), password);
        return ResultBody.ok();
    }

    /**
     * 修改当前登录用户基本信息
     *
     * @param nickName
     * @param userDesc
     * @param avatar
     * @return
     */
    @ApiOperation(value = "修改当前登录用户基本信息", notes = "修改当前登录用户基本信息")
    @PostMapping("/current/user/update")
    public ResultBody updateUserInfo(
            @RequestParam(value = "nickName") String nickName,
            @RequestParam(value = "userDesc", required = false) String userDesc,
            @RequestParam(value = "avatar", required = false) String avatar
    ) {
        AlpsUserDetails alpsUserDetails = AlpsHelper.getUser();
        BaseSysUser user = new BaseSysUser();
        user.setUserId(alpsUserDetails.getUserId());
        user.setNickName(nickName);
        user.setUserDesc(userDesc);
        user.setAvatar(avatar);
        userService.updateUser(user);
        alpsUserDetails.setNickName(nickName);
        alpsUserDetails.setAvatar(avatar);
        AlpsHelper.updateAlpsUser(redisTokenStore,alpsUserDetails);
        return ResultBody.ok();
    }

    /**
     * 获取登陆用户已分配权限
     *
     * @return
     */
    @ApiOperation(value = "获取当前登录用户已分配菜单权限", notes = "获取当前登录用户已分配菜单权限")
    @GetMapping("/current/user/menu")
    public ResultBody<List<AuthorityMenu>> findAuthorityMenu() {
        List<AuthorityMenu> result = authorityService.findAuthorityMenuByUser(AlpsHelper.getUser().getUserId(), CommonConstants.ROOT.equals(AlpsHelper.getUser().getUsername()));
        return ResultBody.ok().data(result);
    }
}
