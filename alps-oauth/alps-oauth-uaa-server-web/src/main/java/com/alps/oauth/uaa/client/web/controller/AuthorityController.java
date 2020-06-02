package com.alps.oauth.uaa.client.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alps.base.api.model.Authority2Api;
import com.alps.base.api.model.AuthorityMenu;
import com.alps.base.api.model.entity.SysAuthority;
import com.alps.base.api.model.entity.SysMenu;
import com.alps.common.core.domain.ResultBody;
import com.alps.base.api.service.ISysAuthorityServiceClient;
import com.alps.oauth.uaa.client.web.service.ISysAuthorityService;

import java.util.List;
/**
 * @author:Yujie.lee
 * Date:2019年12月7日
 * TodoTODO
 */
@Api(tags = "系统权限管理")
@RestController
public class AuthorityController implements ISysAuthorityServiceClient {

    @Autowired
    private ISysAuthorityService authorityService;

    /**
     * 获取用户对应角色下面所有接口例表
     */
    
    @ApiOperation(value = "获取接口列表", notes = "获取接口列表")
    @GetMapping("/authority/apiList")
    public ResultBody<List<SysAuthority>> findAuthorityApiList(
            @RequestParam(value = "userName", required = true) String userName
    ) {
        List<SysAuthority> result = authorityService.findAuthorityApiList(userName);
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
    public ResultBody<List<SysMenu>> findAuthorityMenu(
    		@RequestParam(value = "userName", required = true) String userName,
   		    @RequestParam(value = "companyId", required = true) String companyId
    		) {
        List<SysMenu> result = authorityService.findAuthorityMenu(userName,companyId);
        return ResultBody.ok().data(result);
    }
}
