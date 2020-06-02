package com.alps.oauth.uaa.client.web.service;

import java.util.List;

import com.alps.base.api.model.entity.SysApp;
import com.alps.common.oauth2.security.AlpsClientDetails;
import com.alps.oauth.uaa.client.web.model.PageParams;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * 应用信息管理
 *
 * @author YJ.lee
 */
public interface ISysAppService extends IBaseService<SysApp> {
    /**
     * 查询应用列表
     *
     * @param pageParams
     * @return
     */
    IPage<SysApp> findListPage(PageParams pageParams);

    /**
     * 获取app信息
     *
     * @param appId
     * @return
     */
    SysApp getAppInfo(Long appId);

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
    SysApp addAppInfo(SysApp app);

    /**
     * 修改应用
     *
     * @param app 应用
     * @return 应用信息
     */
    SysApp updateInfo(SysApp app);


    /**
     * 重置秘钥
     *
     * @param appId
     * @return
     */
    String restSecret(Long appId);

    /**
     * 删除应用
     *
     * @param appId
     * @return
     */
    void removeApp(Long appId);
    
    	/**
         * 查询sys_app列表
         * 
         * @param SysApp sys_app信息
         * @return sys_app集合
         */
    	public List<SysApp> selectSysAppList(SysApp SysApp);
    	
    		
    	/**
         * 删除sys_app信息
         * 
         * @param ids 需要删除的数据ID
         * @return 结果
         */
    	public int deleteSysAppByIds(String ids);
}
