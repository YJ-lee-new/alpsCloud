/**
 *@date:2020年1月6日
 *@author:Yujie.Lee
 *TODO
 */
package com.alps.oauth.uaa.admin.service;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.alps.base.api.model.Authority2Api;
import com.alps.common.constant.UaaConstant;
import com.alps.common.core.domain.ResultBody;
import com.alps.oauth.uaa.admin.feign.BaseAuthorityServiceClient;

/**
 * @author:Yujie.lee
 * Date:2020年1月6日
 * TodoTODO
 */
@Component
public class Token2ApiSerivce { 
	@Autowired
	private BaseAuthorityServiceClient baseAuthorityServiceClient;
	
	@Autowired
    private RedisTemplate<String,Object> redisTemplate ;
	
    public List<Authority2Api> getToken2Api(String appId,String token,String userName,String tokenType ){
		ResultBody<List<Authority2Api>> body = baseAuthorityServiceClient.findAuthorityApiList(userName);
		List <Authority2Api> list = body.getData();
		for(Iterator<Authority2Api> it = list.iterator() ; it.hasNext();){
		  Authority2Api temp = it.next() ;
          redisTemplate.boundHashOps(UaaConstant.API_OKEN + ":" + token).put(String.valueOf(temp.getApiUrl() ), JSONObject.toJSONString(temp));
        }
		redisTemplate.expire(UaaConstant.API_OKEN + ":" + token, 60*60, TimeUnit.SECONDS);
    	return list ;
    }

}
