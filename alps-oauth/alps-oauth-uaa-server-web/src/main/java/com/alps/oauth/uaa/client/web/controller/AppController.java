package com.alps.oauth.uaa.client.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.alps.base.api.model.entity.SysApp;
import com.alps.base.api.service.IPfAppServiceClient;
import com.alps.oauth.uaa.client.web.service.ISysAppService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import com.alps.common.core.domain.ResultBody;
import com.alps.common.oauth2.security.AlpsClientDetails;

/**
 * sys_app 信息操作处理
 * 
 * @author leopards
 * @date 2020-03-19
 */
@Api(tags = "系统应用管理")
@RestController
public class AppController  implements IPfAppServiceClient {
	
	@Autowired
	private ISysAppService appService;
    /**
     * 获取应用详情
     *
     * @param appId
     * @return
     */
    @ApiOperation(value = "获取应用详情", notes = "获取应用详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appId", value = "应用ID", defaultValue = "1", required = true, paramType = "path"),
    })
    @GetMapping("/app/{appId}/info")
    @Override
    public ResultBody<SysApp> getApp( @PathVariable("appId") Long appId) {
        SysApp appInfo = appService.getAppInfo(appId);
        return ResultBody.ok().data(appInfo);
    }

    /**
               * 获取应用开发配置信息
     *
     * @param clientId
     * @return
     */
    @ApiOperation(value = "获取应用开发配置信息", notes = "获取应用开发配置信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "clientId", value = "应用ID", defaultValue = "1", required = true, paramType = "path"),
    })
    @GetMapping("/app/client/{clientId}/info")
    @Override
    public ResultBody<AlpsClientDetails> getAppClientInfo(@PathVariable("clientId") String clientId) {
        AlpsClientDetails clientInfo = appService.getAppClientInfo(clientId);
        return ResultBody.ok().data(clientInfo);
    }
}

