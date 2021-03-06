package com.alps.base.api.service;

import com.alps.base.api.model.entity.SysApp;
import com.alps.common.core.domain.ResultBody;
import com.alps.common.oauth2.security.AlpsClientDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
public interface IPfAppServiceClient {

    /**
     * 获取应用基础信息
     *
     * @param appId 应用Id
     * @return
     */
    @GetMapping("/app/{appId}/info")
    ResultBody<SysApp> getApp(@PathVariable("appId") Long appId);

    /**
     * 获取应用开发配置信息
     * @param clientId
     * @return
     */
    @GetMapping("/app/client/{clientId}/info")
    ResultBody<AlpsClientDetails> getAppClientInfo(@PathVariable("clientId") String clientId);
}
