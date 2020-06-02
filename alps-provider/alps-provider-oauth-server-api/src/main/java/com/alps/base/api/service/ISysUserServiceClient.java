package com.alps.base.api.service;

import com.alps.common.core.domain.ResultBody;
import com.alps.base.api.model.UserAccount;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
public interface ISysUserServiceClient {

    /**
     * 系统用户登录
     *
     * @param username
     * @return
     */
    @PostMapping("/user/login")
    ResultBody<UserAccount> userLogin(
    		@RequestParam(value = "username") String username
    		);


    /**
     * 注册第三方系统登录账号
     *
     * @param account
     * @param password
     * @param accountType
     * @return
     */
    @PostMapping("/user/register/thirdParty")
    ResultBody addUserThirdParty(
            @RequestParam(value = "account") String account,
            @RequestParam(value = "password") String password,
            @RequestParam(value = "accountType") String accountType,
            @RequestParam(value = "nickName") String nickName,
            @RequestParam(value = "avatar") String avatar
    );

    /**
     * 获取用户 角色 权限  信息
     *
     * @return 用户信息
     */
    @PostMapping("/user/getMenus")
    public ResultBody getMenus(@RequestParam(value="userName",required = true) String userName,
    		                   @RequestParam(value="companyId",required = true) String companyId);

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("/user/getInfo")
    public ResultBody getInfo();



}
