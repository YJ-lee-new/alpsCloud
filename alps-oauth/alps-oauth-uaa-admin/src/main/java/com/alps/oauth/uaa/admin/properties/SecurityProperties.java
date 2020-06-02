package com.alps.oauth.uaa.admin.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author:Yujie.lee
 * Date:2019年12月7日
 *  
 */
@ConfigurationProperties(prefix = "demo.security")
@Configuration
@Data
public class SecurityProperties {
    private String loginPage = "/login";
    private String failureUrl = "/oauth/login?error=true";
    private String loginProcessingUrl = "/oauth/login";
    private String logoutUrl = "/oauth/logout";
    private String[] matchers = new String[]{"/oauth/requrie",
            "/js/**",
            "/iview/**",
            "/css/**",
            "/images/**",
            "/api/**",
            "/error",
            "/login2",
            "/static/**",
            "/v2/api-docs/**",
            "/swagger-resources/**",
            "/webjars/**",
            "/favicon.png/",
            "/favicon.ico",
            "/login/**",
            "/oauth/**",
            "/web/**",
            "/client/token",
            "/syslogin",
            "/ajax/**",
            "/signin"
    };
    // 默认登录页
    private String loginPageHtml = "signin";


    private int rememberSeconds = 3600*24*7;
}
