package com.alps.common.oauth2.constants;

import java.io.Serializable;
/**
 * @author:Yujie.lee
 * Date:2019年12月7日
 * Todo 社交第三方账号客户端
 */
public class AlpsOAuth2ClientDetails implements Serializable {
    private static final long serialVersionUID = -6103012432819993075L;
    /**
     * 客户端ID
     */
    private String clientId;
    /**
     * 客户端密钥
     */
    private String clientSecret;
    /**
     * 客户端授权范围
     */
    private String scope;
    /**
     * 获取token
     */
    private String accessTokenUri;
    /**
     * 认证地址
     */
    private String userAuthorizationUri;

    /**
     * 重定向地址
     */
    private String redirectUri;

    /**
     * 获取用户信息
     */
    private String userInfoUri;

    /**
     * 登录成功地址
     */
    private String loginSuccessUri;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getAccessTokenUri() {
        return accessTokenUri;
    }

    public void setAccessTokenUri(String accessTokenUri) {
        this.accessTokenUri = accessTokenUri;
    }

    public String getUserAuthorizationUri() {
        return userAuthorizationUri;
    }

    public void setUserAuthorizationUri(String userAuthorizationUri) {
        this.userAuthorizationUri = userAuthorizationUri;
    }

    public String getUserInfoUri() {
        return userInfoUri;
    }

    public void setUserInfoUri(String userInfoUri) {
        this.userInfoUri = userInfoUri;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public String getLoginSuccessUri() {
        return loginSuccessUri;
    }

    public void setLoginSuccessUri(String loginSuccessUri) {
        this.loginSuccessUri = loginSuccessUri;
    }
}
