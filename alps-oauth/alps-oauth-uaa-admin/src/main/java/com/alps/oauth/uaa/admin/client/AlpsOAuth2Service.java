package com.alps.oauth.uaa.admin.client;

import com.alibaba.fastjson.JSONObject;
/**
 * @author:Yujie.lee
 * Date:2019年12月7日
 * Todo 第三方登录接口
 */
public interface AlpsOAuth2Service {
    /**
     * 根据code获得Token
     *
     * @param code code
     * @return token
     */
    String getAccessToken(String code);

    /**
     * 根据Token获得AlpsId
     *
     * @param accessToken Token
     * @return alpsId
     */
    String getAlpsId(String accessToken);

    /**
     * 拼接授权URL
     *
     * @return URL
     */
    String getAuthorizationUrl();

    /**
     * 根据Token和AlpsId获得用户信息
     *
     * @param accessToken Token
     * @param alpsId      alpsId
     * @return 第三方应用给的用户信息
     */
    JSONObject getUserInfo(String accessToken, String alpsId);

    /**
     * 刷新Token
     *
     * @param code code
     * @return 新的token
     */
    String refreshToken(String code);

    /**
     * 获取登录成功地址
     * @return
     */
    String getLoginSuccessUrl();

    /**
     * 获取客户端配置信息
     * @return
     */
    AlpsOAuth2ClientDetails getClientDetails();
}
