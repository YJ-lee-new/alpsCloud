package com.alps.oauth.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import com.alps.common.oauth2.exception.AlpsAccessDeniedHandler;
import com.alps.common.oauth2.exception.AlpsAuthenticationEntryPoint;
import com.alps.common.oauth2.security.AlpsHelper;

import javax.sql.DataSource;

/**
 * @author:Yujie.lee
 * Date:2019年12月7日
 * Todo oauth2资源服务器配置
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;
    @Autowired
    private DataSource dataSource;


    @Bean
    public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
    }
    
    @Bean
    public RedisTokenStore redisTokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
    }


    @Bean
    public JdbcClientDetailsService clientDetailsService() {
        JdbcClientDetailsService jdbcClientDetailsService = new JdbcClientDetailsService(dataSource);
        jdbcClientDetailsService.setPasswordEncoder(passwordEncoder());
        return jdbcClientDetailsService;
    }


    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        // 构建redis获取token服务类
        resources.tokenServices(AlpsHelper.buildRedisTokenServices(redisConnectionFactory));
        resources.authenticationEntryPoint(new AlpsAuthenticationEntryPoint()); //  update 2019.12.23
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .authorizeRequests()
                // 监控端点内部放行
                .requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
                // fegin访问或无需身份认证
                .antMatchers(
                        "/authority/access",
                        "/authority/app",
                        "/authority/apiList",
                        "/app/*/info",
                        "/app/client/*/info",
                        "/gateway/api/**",
                        "/user/add/thirdParty",
                        "/user/info",
                        "/user/login",
                        "/developer/add/thirdParty",
                        "/developer/info",
                        "/developer/login"
                ).permitAll()
                .anyRequest().authenticated()
                .and()
                //认证鉴权错误处理,为了统一异常处理。每个资源服务器都应该加上。
                .exceptionHandling()
                .accessDeniedHandler(new AlpsAccessDeniedHandler())
                .authenticationEntryPoint(new AlpsAuthenticationEntryPoint())
                .and()
                .csrf().disable();
    }


}

