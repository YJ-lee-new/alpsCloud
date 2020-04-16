package com.alps.common.oauth2.constants;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author:Yujie.lee
 * Date:2019年11月27日
 * TodoTODO
 */
@Component
@ConfigurationProperties(prefix = "alps.client")
public class AlpsOAuth2ClientProperties {

    private Map<String, AlpsOAuth2ClientDetails> oauth2;

    public Map<String, AlpsOAuth2ClientDetails> getOauth2() {
        return oauth2;
    }

    public void setOauth2(Map<String, AlpsOAuth2ClientDetails> oauth2) {
        this.oauth2 = oauth2;
    }
}
