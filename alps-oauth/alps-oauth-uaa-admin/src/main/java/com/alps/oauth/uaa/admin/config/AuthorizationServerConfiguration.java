package com.alps.oauth.uaa.admin.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import com.alps.common.oauth2.exception.AlpsOAuth2WebResponseExceptionTranslator;
import com.alps.common.oauth2.security.AlpsHelper;
import com.alps.common.oauth2.security.AlpsTokenEnhancer;
import com.alps.oauth.uaa.admin.serivceImp.UserDetailsServiceImpl;

import javax.sql.DataSource;
/**
 * @author:Yujie.lee
 * Date:2019年12月7日
 * Todo平台认证服务器配置
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;
//    @Autowired
//    private UserDetailsService userDetailsService;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    /**
     * 自定义获取客户端,为了支持自定义权限,
     */
    @Autowired
    @Qualifier(value = "clientDetailsServiceImpl")
    private ClientDetailsService customClientDetailsService;

    /**
               *  令牌存放  统一存放在缓存中
     *
     * @return
     */
    @Bean
    public TokenStore tokenStore() {
       return new RedisTokenStore(redisConnectionFactory);
//       return new JdbcTokenStore(dataSource); // 使用Jdbctoken store
    }


    /**
     * 授权store
     *
     * @return
     */
    @Bean
    public ApprovalStore approvalStore() {
        return new JdbcApprovalStore(dataSource);
    }

    /**
     * 令牌信息拓展
     *
     * @return
     */
    @Bean
    public TokenEnhancer tokenEnhancer() {
        return new AlpsTokenEnhancer();
    }

    /**
     * 授权码
     *
     * @return
     */
    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        return new JdbcAuthorizationCodeServices(dataSource);
    }
    
    /**
     * 配置客户端详情服务
     * 客户端详细信息在这里进行初始化，你能够把客户端详情信息写死在这里或者是通过数据库来存储调取详情信息
     * @param clients
     * @throws Exception
     */

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
         clients.withClientDetails(customClientDetailsService);
//    	  clients.inMemory()
//          .withClient("sysPro")//用于标识用户ID
//          .authorizedGrantTypes("authorization_code","refresh_token")//授权方式
//          .scopes("test")//授权范围
//          .secret(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("123456"));//客户端安全码,secret密码配置从 Spring Security 5.0开始必须以 {bcrypt}+加密后的密码 这种格式填写;
//    	
    }
    
    /**
                * 用来配置授权（authorization）以及令牌（token）的访问端点和令牌服务(token services)
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
                .authenticationManager(authenticationManager)
                .approvalStore(approvalStore())
                .userDetailsService(userDetailsService)
                .tokenServices(createDefaultTokenServices())
                .accessTokenConverter(AlpsHelper.buildAccessTokenConverter());
//                .authorizationCodeServices(authorizationCodeServices());
//     // 存数据库
//        endpoints.tokenStore(tokenStore()).authenticationManager(authenticationManager)
//                .userDetailsService(userDetailsService);
        
        // 自定义确认授权页面
        endpoints.pathMapping("/oauth/confirm_access", "/oauth/confirm_access");
        // 自定义错误页
        endpoints.pathMapping("/oauth/error", "/oauth/error");
        // 自定义异常转换类
        endpoints.exceptionTranslator(new AlpsOAuth2WebResponseExceptionTranslator());
    }

    private DefaultTokenServices createDefaultTokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(tokenStore());
        tokenServices.setTokenEnhancer(tokenEnhancer());
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setReuseRefreshToken(true);
        tokenServices.setClientDetailsService(customClientDetailsService);
        return tokenServices;
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                // 开启/oauth/check_token验证端口认证权限访问
                .checkTokenAccess("isAuthenticated()")
                // 开启/oauth/token支持client_id以及client_secret作登录认证,开启表单认证
                .allowFormAuthenticationForClients();
    }

}
