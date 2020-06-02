package com.alps.oauth.uaa.client.web.priorities;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author:Yujie.lee
 * Date:2020年4月16日
 */
@ConfigurationProperties(prefix = "alps.security")
@Configuration
@Data
public class SecurityProperties {
    private String loginPage = "/alps/login";
    private String failureUrl = "/alps/login?error=true";
    private String loginProcessingUrl = "/alps/login";
    private String logoutUrl = "/alps/logout";
    private String[] matchers = new String[]{"/alps/requrie",
    		 "/authority/access",
             "/authority/app",
             "/authority/apiList",
             "/app/*/info",
             "/app/client/*/info",
             "/gateway/api/**",
             "/user/add/thirdParty",
             "/user/info",
             "/user/login",
            "/js/**",
            "/ajax/**",
            "/alps/**", 
            "/favicon.*",
            "/bootstrap/**",
            "/iview/**",
            "/css/**",
            "/alps/login",
            "/alps/logout",
            "/images/**",
            "/api/**",
            "/auth/token"
    };
    // 默认登录页
    private String loginPageHtml = "login";


    private int rememberSeconds = 3600*24*7;
}
