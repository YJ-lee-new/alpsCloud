package com.alps.common.oauth2.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author:Yujie.lee
 * Date:2019年12月7日
 * 资源扫描配置
 */
@ConfigurationProperties(prefix = "alps.scan")
public class AlpsScanProperties {

    /**
     * 请求资源注册到API列表
     */
    private Boolean registerRequestMapping = false;

    public boolean isRegisterRequestMapping() {
        return registerRequestMapping;
    }

    public void setRegisterRequestMapping(boolean registerRequestMapping) {
        this.registerRequestMapping = registerRequestMapping;
    }


}
