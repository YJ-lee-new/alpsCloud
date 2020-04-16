package com.alps.zuul.service;

import org.springframework.stereotype.Component;

import com.alps.common.core.domain.ReturnResult;
import com.alps.provider.system.domain.SysUser;

/**
 * @author:Yujie.lee
 * Date:2019年11月2日
 * TodoTODO
 */
@Component
public class HystrixSysUserHandler implements SysUserService{


	@SuppressWarnings("unchecked")
	@Override
    public ReturnResult<SysUser> login(String account, String password) {
        return (ReturnResult<SysUser>) ReturnResult.build(400,"系统异常");
    }

}
