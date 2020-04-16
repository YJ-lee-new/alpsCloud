package com.alps.oauth.server.controller;

import com.alps.common.core.domain.ResultBody;
import com.alps.oauth.server.model.PageParams;
import com.alps.oauth.server.service.DeveloperService;
import com.alps.base.api.model.UserAccount;
import com.alps.base.api.model.entity.BaseDeveloper;
import com.alps.base.api.model.entity.BaseRole;
import com.alps.base.api.service.IBaseDeveloperServiceClient;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
@Api(tags = "系统用户管理")
@RestController
public class DeveloperController implements IBaseDeveloperServiceClient {
    @Autowired
    private DeveloperService developerService;



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
    @PostMapping("/developer/login")
    @Override
    public ResultBody<UserAccount> developerLogin(@RequestParam(value = "username") String username) {
        UserAccount account = developerService.login(username);
        return ResultBody.ok().data(account);
    }

    /**
     * 系统分页用户列表
     *
     * @return
     */
    @ApiOperation(value = "系统分页用户列表", notes = "系统分页用户列表")
    @GetMapping("/developer")
    public ResultBody<IPage<BaseDeveloper>> getUserList(@RequestParam(required = false) Map map) {
        return ResultBody.ok().data(developerService.findListPage(new PageParams(map)));
    }

    /**
     * 获取所有用户列表
     *
     * @return
     */
    @ApiOperation(value = "获取所有用户列表", notes = "获取所有用户列表")
    @GetMapping("/developer/all")
    public ResultBody<List<BaseRole>> getUserAllList() {
        return ResultBody.ok().data(developerService.findAllList());
    }

    /**
     * 添加系统用户
     *
     * @param userName
     * @param password
     * @param nickName
     * @param status
     * @param userType
     * @param email
     * @param mobile
     * @param userDesc
     * @param avatar
     * @return
     */
    @ApiOperation(value = "添加系统用户", notes = "添加系统用户")
    @PostMapping("/developer/add")
    public ResultBody<Long> addUser(
            @RequestParam(value = "userName") String userName,
            @RequestParam(value = "password") String password,
            @RequestParam(value = "nickName") String nickName,
            @RequestParam(value = "status") Integer status,
            @RequestParam(value = "userType") String userType,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "mobile", required = false) String mobile,
            @RequestParam(value = "userDesc", required = false) String userDesc,
            @RequestParam(value = "avatar", required = false) String avatar
    ) {
        BaseDeveloper developer = new BaseDeveloper();
        developer.setUserName(userName);
        developer.setPassword(password);
        developer.setNickName(nickName);
        developer.setUserType(userType);
        developer.setEmail(email);
        developer.setMobile(mobile);
        developer.setUserDesc(userDesc);
        developer.setAvatar(avatar);
        developer.setStatus(status);
        developerService.addUser(developer);
        return ResultBody.ok();
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
    @PostMapping("/developer/update")
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
        BaseDeveloper developer = new BaseDeveloper();
        developer.setUserId(userId);
        developer.setNickName(nickName);
        developer.setUserType(userType);
        developer.setEmail(email);
        developer.setMobile(mobile);
        developer.setUserDesc(userDesc);
        developer.setAvatar(avatar);
        developer.setStatus(status);
        developerService.updateUser(developer);
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
    @PostMapping("/developer/update/password")
    public ResultBody updatePassword(
            @RequestParam(value = "userId") Long userId,
            @RequestParam(value = "password") String password
    ) {
        developerService.updatePassword(userId, password);
        return ResultBody.ok();
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
    @PostMapping("/developer/add/thirdParty")
    @Override
    public ResultBody addDeveloperThirdParty(
            @RequestParam(value = "account") String account,
            @RequestParam(value = "password") String password,
            @RequestParam(value = "accountType") String accountType,
            @RequestParam(value = "nickName") String nickName,
            @RequestParam(value = "avatar") String avatar
    ) {
        BaseDeveloper developer = new BaseDeveloper();
        developer.setNickName(nickName);
        developer.setUserName(account);
        developer.setPassword(password);
        developer.setAvatar(avatar);
        developerService.addUserThirdParty(developer, accountType);
        return ResultBody.ok();
    }

}
