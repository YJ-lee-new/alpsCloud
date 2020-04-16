package com.alps.oauth.server.service.impl;

import com.alps.common.constant.BaseConstants;
import com.alps.base.api.model.entity.BaseApp;
import com.alps.base.api.model.entity.BaseDeveloper;
import com.alps.common.oauth2.exception.AlpsAlertException;
import com.alps.common.oauth2.security.AlpsClientDetails;
import com.alps.common.oauth2.utils.BeanConvertUtils;
import com.alps.common.oauth2.utils.RandomValueUtils;
import com.alps.oauth.server.extra.CriteriaQuery;
import com.alps.oauth.server.mapper.BaseAppMapper;
import com.alps.oauth.server.model.PageParams;
import com.alps.oauth.server.service.AppService;
import com.alps.oauth.server.service.AuthorityService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class BaseAppServiceImpl extends BaseServiceImpl<BaseAppMapper, BaseApp> implements AppService {

    @Autowired
    private BaseAppMapper baseAppMapper;
    @Autowired
    private AuthorityService authorityService;
    
    @Autowired
    private  JdbcClientDetailsService jdbcClientDetailsService;
    /**
     * token有效期，默认12小时
     */
    public static final int ACCESS_TOKEN_VALIDITY_SECONDS = 60 * 60 * 12;
    /**
     * token有效期，默认7天
     */
    public static final int REFRESH_TOKEN_VALIDITY_SECONDS = 60 * 60 * 24 * 7;

    /**
     * 查询应用列表
     *
     * @param pageParams
     * @return
     */
    @Override
    public IPage<BaseApp> findListPage(PageParams pageParams) {
        BaseApp query = pageParams.mapToObject(BaseApp.class);
        CriteriaQuery<BaseApp> cq = new CriteriaQuery(pageParams);
        cq.lambda()
                .eq(ObjectUtils.isNotEmpty(query.getDeveloperId()), BaseApp::getDeveloperId, query.getDeveloperId())
                .eq(ObjectUtils.isNotEmpty(query.getAppType()), BaseApp::getAppType, query.getAppType())
                .eq(ObjectUtils.isNotEmpty(pageParams.getRequestMap().get("aid")), BaseApp::getAppId, pageParams.getRequestMap().get("aid"))
                .likeRight(ObjectUtils.isNotEmpty(query.getAppName()), BaseApp::getAppName, query.getAppName())
                .likeRight(ObjectUtils.isNotEmpty(query.getAppNameEn()), BaseApp::getAppNameEn, query.getAppNameEn());
        cq.select("app.*,developer.user_name");
        //关联BaseDeveloper表
        cq.createAlias(BaseDeveloper.class);
        cq.orderByDesc("create_time");
        return pageList(cq);
    }

    /**
     * 获取app详情
     *
     * @param appId
     * @return 并将结果放入缓存
     */
    @Cacheable(value = "apps", key = "#appId")
    @Override
    public BaseApp getAppInfo(String appId) {
        return baseAppMapper.selectById(appId);
    }

    /**
     * 获取app和应用信息
     *
     * @return
     */
    @Override
    @Cacheable(value = "apps", key = "'client:'+#clientId")
    public AlpsClientDetails getAppClientInfo(String clientId) {
    	System.out.println("======>> 开始查询" );
        BaseClientDetails baseClientDetails = null;
        try {
            baseClientDetails = (BaseClientDetails) jdbcClientDetailsService.loadClientByClientId(clientId);
        } catch (Exception e) {
        	System.out.println("======>>"  +e);
        	throw  new AlpsAlertException(400,"所请求的应用不存在!");
        }
        String appId = baseClientDetails.getAdditionalInformation().get("appId").toString();
        AlpsClientDetails alpsClient = new AlpsClientDetails();
        BeanUtils.copyProperties(baseClientDetails, alpsClient);
        alpsClient.setAuthorities(authorityService.findAuthorityByApp(appId));
        return alpsClient;
    }

    /**
     * 更新应用开发新型
     *
     * @param client
     */
    @CacheEvict(value = {"apps"}, key = "'client:'+#client.clientId")
    @Override
    public void updateAppClientInfo(AlpsClientDetails client) {
        jdbcClientDetailsService.updateClientDetails(client);
    }

    /**
     * 添加应用
     *
     * @param app
     * @return 应用信息
     */
    @CachePut(value = "apps", key = "#app.appId")
    @Override
    public BaseApp addAppInfo(BaseApp app) {
        String appId = String.valueOf(System.currentTimeMillis());
        String apiKey = RandomValueUtils.randomAlphanumeric(24);
        String secretKey = RandomValueUtils.randomAlphanumeric(32);
        app.setAppId(appId);
        app.setApiKey(apiKey);
        app.setSecretKey(secretKey);
        app.setCreateTime(new Date());
        app.setUpdateTime(app.getCreateTime());
        if (app.getIsPersist() == null) {
            app.setIsPersist(0);
        }
        baseAppMapper.insert(app);
        Map info = BeanConvertUtils.objectToMap(app);
        // 功能授权
        BaseClientDetails client = new BaseClientDetails();
        client.setClientId(app.getAppKey());
        client.setClientSecret(app.getSecretKey());
        client.setAdditionalInformation(info);
        client.setAuthorizedGrantTypes(Arrays.asList("authorization_code", "client_credentials", "implicit", "refresh_token"));
        client.setAccessTokenValiditySeconds(ACCESS_TOKEN_VALIDITY_SECONDS);
        client.setRefreshTokenValiditySeconds(REFRESH_TOKEN_VALIDITY_SECONDS);
        jdbcClientDetailsService.addClientDetails(client);
        return app;
    }

    /**
     * 修改应用
     *
     * @param app 应用
     * @return 应用信息
     */
    @Caching(evict = {
            @CacheEvict(value = {"apps"}, key = "#app.appId"),
            @CacheEvict(value = {"apps"}, key = "'client:'+#app.appId")
    })
    @Override
    public BaseApp updateInfo(BaseApp app) {
        app.setUpdateTime(new Date());
        baseAppMapper.updateById(app);
        // 修改客户端附加信息
        BaseApp appInfo = getAppInfo(app.getAppId());
        Map info = BeanConvertUtils.objectToMap(appInfo);
        BaseClientDetails client = (BaseClientDetails) jdbcClientDetailsService.loadClientByClientId(appInfo.getAppKey());
        client.setAdditionalInformation(info);
        jdbcClientDetailsService.updateClientDetails(client);
        return app;
    }

    /**
     * 重置秘钥
     *
     * @param appId
     * @return
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = {"apps"}, key = "#appId"),
            @CacheEvict(value = {"apps"}, key = "'client:'+#appId")
    })
    public String restSecret(String appId) {
        BaseApp appInfo = getAppInfo(appId);
        if (appInfo == null) {
            throw new AlpsAlertException(appId + "应用不存在!");
        }
        if (appInfo.getIsPersist().equals(BaseConstants.ENABLED)) {
            throw new AlpsAlertException(String.format("保留数据,不允许修改"));
        }
        // 生成新的密钥
        String secretKey = RandomValueUtils.randomAlphanumeric(32);
        appInfo.setSecretKey(secretKey);
        appInfo.setUpdateTime(new Date());
        baseAppMapper.updateById(appInfo);
        jdbcClientDetailsService.updateClientSecret(appInfo.getAppKey(), secretKey);
        return secretKey;
    }

    /**
     * 删除应用
     *
     * @param appId
     * @return
     */
    @Caching(evict = {
            @CacheEvict(value = {"apps"}, key = "#appId"),
            @CacheEvict(value = {"apps"}, key = "'client:'+#appId")
    })
    @Override
    public void removeApp(String appId) {
        BaseApp appInfo = getAppInfo(appId);
        if (appInfo == null) {
            throw new AlpsAlertException(appId + "应用不存在!");
        }
        if (appInfo.getIsPersist().equals(BaseConstants.ENABLED)) {
            throw new AlpsAlertException(String.format("保留数据,不允许删除"));
        }
        // 移除应用权限
        authorityService.removeAuthorityApp(appId);
        baseAppMapper.deleteById(appInfo.getAppId());
        jdbcClientDetailsService.removeClientDetails(appInfo.getAppKey());
    }

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String apiKey = String.valueOf(RandomValueUtils.randomAlphanumeric(24));
        String secretKey = String.valueOf(RandomValueUtils.randomAlphanumeric(32));
        System.out.println("apiKey=" + apiKey);
        System.out.println("secretKey=" + secretKey);
        System.out.println("encodeSecretKey=" + encoder.encode(secretKey));
    }

}
