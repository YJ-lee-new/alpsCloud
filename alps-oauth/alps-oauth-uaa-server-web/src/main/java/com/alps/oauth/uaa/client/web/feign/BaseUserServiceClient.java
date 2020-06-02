package com.alps.oauth.uaa.client.web.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

import com.alps.base.api.service.ISysUserServiceClient;
import com.alps.base.api.model.AlpsConstants;
/**
 * @author:Yujie.lee
 * Date:2019年12月7日
 * TodoTODO
 */
@Component
@FeignClient(value = AlpsConstants.OAUTH2_SERVER)
public interface BaseUserServiceClient extends ISysUserServiceClient {
}
