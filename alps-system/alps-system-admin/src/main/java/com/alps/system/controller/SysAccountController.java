package com.alps.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alps.common.core.controller.BaseController;
import com.alps.common.core.domain.ResultBody;
import com.alps.provider.system.api.ISysAccountClient;
import com.alps.provider.system.domain.SysUser;
import com.alps.system.service.ISysUserService;

/**
 * 用户信息
 * 
 * @author : yujie.lee
 */
@RestController
public class SysAccountController implements ISysAccountClient
{
    @Autowired
    private ISysUserService userService;  
    
    /*
     *  	对外提供账号查询接口
     * @see com.alps.provider.system.api.ISysAccountClient#getAccount(java.lang.String)
     */
	@Override
	@PostMapping("/user/login")
	public SysUser getAccount(String username,String accountType,String domain) {
		SysUser sysUser = userService.selectAccountByUsername(username,accountType,domain);
		return sysUser;
	}
}