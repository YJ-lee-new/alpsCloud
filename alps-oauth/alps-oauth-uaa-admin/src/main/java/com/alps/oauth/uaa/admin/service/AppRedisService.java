/**
 *@date:2020年1月6日
 *@author:Yujie.Lee
 *TODO
 */
package com.alps.oauth.uaa.admin.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerEndpointsConfiguration;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Component;

import com.alps.common.oauth2.security.AlpsHelper;
import com.alps.oauth.uaa.admin.client.AlpsOAuth2ClientDetails;
import com.alps.oauth.uaa.admin.client.AlpsOAuth2ClientProperties;
import com.alps.oauth.uaa.admin.serivceImp.ClientDetailsServiceImpl;

/**
 * @author:Yujie.lee
 * Date:2020年1月6日
 * TodoTODO
 */
@Component
public class AppRedisService {
    @Autowired
    private ClientDetailsServiceImpl clientDetailsServiceImpl;
    @Autowired
    private AlpsOAuth2ClientProperties clientProperties;
    @Autowired
    AuthorizationServerEndpointsConfiguration endpoints;
    
    @Autowired
    Token2ApiSerivce  token2ApiSerivce;
	 /**
     * 生成 oauth2 token
     *
     * @param userName
     * @param password
     * @param type
     * @return
     */
    public OAuth2AccessToken getToken(String userName, String password, String type,String appKey) throws Exception {
    	AlpsOAuth2ClientDetails clientDetails = getClientDetails(appKey);
    	String appId = clientDetails.getClientId();
        // 使用oauth2密码模式登录.
        Map<String, String> postParameters = new HashMap<>();
        postParameters.put("username", userName);
        postParameters.put("password", password);
        postParameters.put("client_id", appId);
        postParameters.put("client_secret", clientDetails.getClientSecret());
        postParameters.put("grant_type", "password");
        // 添加参数区分,第三方登录
        postParameters.put("login_type", type);
        OAuth2AccessToken result = AlpsHelper.createAccessToken(endpoints,postParameters);
        //拿到合法Token之后，将该Token具有的所有接口权限刷入redis，以便在gateway进行拦截
        token2ApiSerivce.getToken2Api(appId, result.getValue() , userName, result.getTokenType());
        return  result;
    }
    /**
     * Client信息处理
     */
    public AlpsOAuth2ClientDetails getClientDetails(String appKey) {
    	AlpsOAuth2ClientDetails clientDetails = new AlpsOAuth2ClientDetails();
    	if(appKey == null || "".equals(appKey)) {
    		clientDetails = clientProperties.getOauth2().get("admin");
    	}else {
            ClientDetails details = clientDetailsServiceImpl.loadClientByClientId(appKey);
	   		 if (details != null && details.getClientId()!=null && details.getAdditionalInformation() != null) {
	   			 clientDetails.setClientId(details.getClientId());
	   			 clientDetails.setClientSecret(details.getAdditionalInformation().getOrDefault("secretKey", "0").toString() );
	   		 }
	   		 else {
	   			 throw new ClientRegistrationException("应用服务不存在!");
	   		 }
    	}
    	return clientDetails;
    }

}
