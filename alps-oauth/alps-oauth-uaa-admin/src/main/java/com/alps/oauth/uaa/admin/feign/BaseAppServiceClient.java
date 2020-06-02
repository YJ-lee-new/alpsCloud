package com.alps.oauth.uaa.admin.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

import com.alps.common.constant.BaseConstants;
import com.alps.base.api.service.IPfAppServiceClient;
/**
 * @author:Yujie.lee
 * Date:2019年12月7日
 * TodoTODO
 */
@Component
@FeignClient(value = BaseConstants.OAUTH2_SERVER)
public interface BaseAppServiceClient extends IPfAppServiceClient {


}
