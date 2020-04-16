package com.alps.common.oauth2.security;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author YJ.LEE
 * @date 2018/4/16
 * 自定义JwtAccessToken转换器
 */
public class AlpsJwtAccessTokenEnhancer extends JwtAccessTokenConverter {


    /**
     * 生成token
     * @param accessToken
     * @param authentication
     * @return
     */
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        DefaultOAuth2AccessToken defaultOAuth2AccessToken = new DefaultOAuth2AccessToken(accessToken);
        if(authentication.getPrincipal()!=null && authentication.getPrincipal() instanceof AlpsUserDetails){
            // 设置额外用户信息
            AlpsUserDetails baseUser = ((AlpsUserDetails) authentication.getPrincipal());
            final Map<String, Object> additionalInfo = new HashMap<>(8);
            additionalInfo.put(AlpsSecurityConstants.OPEN_ID, baseUser.getUserId());
            additionalInfo.put(AlpsSecurityConstants.DOMAIN, baseUser.getDomain());
            defaultOAuth2AccessToken.setAdditionalInformation(additionalInfo);
        }

        return super.enhance(defaultOAuth2AccessToken, authentication);
    }

    /**
     * 解析token
     * @param value
     * @param map
     * @return
     */
    @Override
    public OAuth2AccessToken extractAccessToken(String value, Map<String, ?> map){
        OAuth2AccessToken oauth2AccessToken = super.extractAccessToken(value, map);
        return oauth2AccessToken;
    }
}
