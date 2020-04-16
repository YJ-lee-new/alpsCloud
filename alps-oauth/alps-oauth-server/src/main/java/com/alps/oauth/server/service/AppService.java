package com.alps.oauth.server.service;

import com.alps.base.api.model.entity.BaseApp;
import com.alps.common.oauth2.security.AlpsClientDetails;
import com.alps.oauth.server.extra.IBaseService;
import com.alps.oauth.server.model.PageParams;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * 应用信息管理
 *
 * @author YJ.lee
 */
public interface AppService extends IBaseService<BaseApp> {
    /**
     * 查询应用列表
     *
     * @param pageParams
     * @return
     */
    IPage<BaseApp> findListPage(PageParams pageParams);

    /**
     * 获取app信息
     *
     * @param appId
     * @return
     */
    BaseApp getAppInfo(String appId);

    /**
     * 获取app和应用信息
     *
     * @param clientId
     * @return
     */
    AlpsClientDetails getAppClientInfo(String clientId);


    /**
     * 更新应用开发新型
     *
     * @param client
     */
    void updateAppClientInfo(AlpsClientDetails client);

    /**
     * 添加应用
     *
     * @param app 应用
     * @return 应用信息
     */
    BaseApp addAppInfo(BaseApp app);

    /**
     * 修改应用
     *
     * @param app 应用
     * @return 应用信息
     */
    BaseApp updateInfo(BaseApp app);


    /**
     * 重置秘钥
     *
     * @param appId
     * @return
     */
    String restSecret(String appId);

    /**
     * 删除应用
     *
     * @param appId
     * @return
     */
    void removeApp(String appId);
}
