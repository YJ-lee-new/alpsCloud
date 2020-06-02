package com.alps.oauth.uaa.client.web.serviceImp;

import com.alps.common.constant.BaseConstants;
import com.alps.common.core.text.Convert;
import com.alps.base.api.model.entity.SysApp;
import com.alps.base.api.model.entity.BaseDeveloper;
import com.alps.common.oauth2.exception.AlpsAlertException;
import com.alps.common.oauth2.security.AlpsClientDetails;
import com.alps.common.oauth2.utils.BeanConvertUtils;
import com.alps.common.oauth2.utils.RandomValueUtils;
import com.alps.oauth.uaa.client.web.mapper.SysAppMapper;
import com.alps.oauth.uaa.client.web.model.PageParams;
import com.alps.oauth.uaa.client.web.service.ISysAppService;
import com.alps.oauth.uaa.client.web.utils.CriteriaQuery;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
@Service
@Transactional(rollbackFor = Exception.class)
public class SysAppServiceImpl extends BaseServiceImpl<SysAppMapper, SysApp> implements ISysAppService {

    @Autowired
    private SysAppMapper SysAppMapper;
    
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
    public IPage<SysApp> findListPage(PageParams pageParams) {
        SysApp query = pageParams.mapToObject(SysApp.class);
        CriteriaQuery<SysApp> cq = new CriteriaQuery(pageParams);
        cq.lambda()
                .eq(ObjectUtils.isNotEmpty(query.getDeveloperId()), SysApp::getDeveloperId, query.getDeveloperId())
                .eq(ObjectUtils.isNotEmpty(query.getAppType()), SysApp::getAppType, query.getAppType())
                .eq(ObjectUtils.isNotEmpty(pageParams.getRequestMap().get("aid")), SysApp::getAppId, pageParams.getRequestMap().get("aid"))
                .likeRight(ObjectUtils.isNotEmpty(query.getAppName()), SysApp::getAppName, query.getAppName())
                .likeRight(ObjectUtils.isNotEmpty(query.getAppNameEn()), SysApp::getAppNameEn, query.getAppNameEn());
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
    public SysApp getAppInfo(Long appId) {
        return SysAppMapper.selectById(appId);
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
    public SysApp addAppInfo(SysApp app) {
        String appKey = RandomValueUtils.randomAlphanumeric(24);
        String secretKey = RandomValueUtils.randomAlphanumeric(32);
        app.setAppKey(appKey);
        app.setSecretKey(secretKey);
        app.setCreateTime(new Date());
        app.setUpdateTime(app.getCreateTime());
        if (app.getIsPersist() == null) {
            app.setIsPersist(0);
        }
        SysAppMapper.insert(app);
        Map info = BeanConvertUtils.objectToMap(app);
        // 功能授权
        BaseClientDetails client = new BaseClientDetails();
        client.setClientId(app.getAppKey());
        client.setClientSecret(app.getSecretKey());
        client.setAdditionalInformation(info);
        client.setScope(Arrays.asList("userProfile"));
        client.setAuthorizedGrantTypes(Arrays.asList("authorization_code", "client_credentials", "implicit", "refresh_token","password"));
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
    public SysApp updateInfo(SysApp app) {
        app.setUpdateTime(new Date());
        SysAppMapper.updateById(app);
        // 修改客户端附加信息
        SysApp appInfo = getAppInfo(app.getAppId());
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
    public String restSecret(Long appId) {
        SysApp appInfo = getAppInfo(appId);
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
        SysAppMapper.updateById(appInfo);
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
    public void removeApp(Long appId) {
        SysApp appInfo = getAppInfo(appId);
        if (appInfo == null) {
            throw new AlpsAlertException(appId + "应用不存在!");
        }
        if (appInfo.getIsPersist().equals(BaseConstants.ENABLED)) {
            throw new AlpsAlertException(String.format("保留数据,不允许删除"));
        }
        SysAppMapper.deleteById(appInfo.getAppId());
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
    
    /**
     * 查询sys_app列表
     * 
     * @param SysApp sys_app信息
     * @return sys_app集合
     */
    @Override
    public List<SysApp> selectSysAppList(SysApp SysApp)
    {
    	return SysAppMapper.selectSysAppList(SysApp);
    }

    /**
     * 删除sys_app对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteSysAppByIds(String ids)
	{
		int n = 0;
		String [] appIds = ids.split(",");
		for(String appId: appIds) {
			this.removeApp(Long.parseLong(appId));
			n++;
		}
		return n;
	}
	


}
