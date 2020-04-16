package com.alps.zuul.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alps.common.core.domain.ReturnResult;
import com.alps.provider.system.domain.SysUser;;
//@FeignClient(value = "alpsWeb",fallback = HystrixWebUserHandler.class)
/**
 * @author:Yujie.lee
 * Date:2019年11月2日
 * TodoTODO
 */
@FeignClient(value = "sysPro",fallback = HystrixSysUserHandler.class)
public interface SysUserService {
	
    @RequestMapping(value="/user/login",method = RequestMethod.POST)
    ReturnResult<SysUser> login(@RequestParam(value = "account") String account, @RequestParam(value = "password") String password);

}
