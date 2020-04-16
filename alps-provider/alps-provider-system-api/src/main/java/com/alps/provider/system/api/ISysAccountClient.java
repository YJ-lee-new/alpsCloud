package com.alps.provider.system.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alps.common.core.domain.ResultBody;
import com.alps.provider.system.domain.SysUser;

/**
 * 参数配置 信息操作处理
 * 
 * @author : yujie.lee
 */
public interface ISysAccountClient
{

    /**
                *        查询账号列表
     */
	@PostMapping("/user/login")
	SysUser getAccount(
			@RequestParam(value = "username") String username,
			@RequestParam(value = "loginType") String accountType,
			@RequestParam(value = "domain") String domain);
	
	
//    /**
//     * 注册第三方系统登录账号
//     *
//     * @param account
//     * @param password
//     * @param accountType
//     * @return
//     */
//    @PostMapping("/user/register/thirdParty")
//    ResultBody addUserThirdParty(
//            @RequestParam(value = "account") String account,
//            @RequestParam(value = "password") String password,
//            @RequestParam(value = "accountType") String accountType,
//            @RequestParam(value = "nickName") String nickName,
//            @RequestParam(value = "avatar") String avatar
//    );

	
}
