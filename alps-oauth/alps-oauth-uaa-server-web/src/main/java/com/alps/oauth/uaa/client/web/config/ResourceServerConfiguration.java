//package com.alps.oauth.uaa.client.web.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
//import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
//import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
//import org.springframework.security.web.csrf.CsrfFilter;
//import org.springframework.web.filter.CharacterEncodingFilter;
//
//import com.alps.common.oauth2.exception.AlpsAccessDeniedHandler;
//import com.alps.common.oauth2.exception.AlpsAuthenticationEntryPoint;
//import com.alps.oauth.uaa.client.web.hander.SecurityAuthenticationFailureHandler;
//import com.alps.oauth.uaa.client.web.hander.SecurityAuthenticationSuccessHandler;
//import com.alps.oauth.uaa.client.web.hander.SecurityLogoutSuccessHandler;
//import com.alps.oauth.uaa.client.web.priorities.SecurityProperties;
//import com.alps.oauth.uaa.client.web.serviceImp.WebUserDetailsServiceImp;
//import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
//
//import javax.sql.DataSource;
//
///**
// * @author:Yujie.lee
// * Date:2019年12月7日
// * Todo oauth2资源服务器配置
// */
//@Configuration
//@EnableResourceServer
//public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
//    @Autowired
//    private RedisConnectionFactory redisConnectionFactory;
//    @Autowired
//    private DataSource dataSource;
//    
//    @Autowired
//    private SecurityProperties securityProperties;
//    @Autowired
//    private SecurityAuthenticationSuccessHandler securityAuthenticationSuccessHandler;
//    @Autowired
//    private SecurityAuthenticationFailureHandler securityAuthenticationFailureHandler;
//    @Autowired
//    private SecurityLogoutSuccessHandler securityLogoutSuccessHandler;
//    @Autowired
//    private WebUserDetailsServiceImp webUserDetailsServiceImp;
//    @Autowired
//
//    
//    @Bean
//    public RedisTokenStore redisTokenStore() {
//        return new RedisTokenStore(redisConnectionFactory);
//    }
//    
//    
//	  /**
//	   * 分页插件
//	   */
//	  @ConditionalOnMissingBean(PaginationInterceptor.class)
//	  @Bean
//	  public PaginationInterceptor paginationInterceptor() {
//	      PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
//	      return paginationInterceptor;
//	  }
//
//
//    @Bean
//    public JdbcClientDetailsService clientDetailsService() {
//        JdbcClientDetailsService jdbcClientDetailsService = new JdbcClientDetailsService(dataSource);
//        jdbcClientDetailsService.setPasswordEncoder(new BCryptPasswordEncoder());
//        return jdbcClientDetailsService;
//    }
//    
//    @Override
//    public void configure(HttpSecurity http) throws Exception{
//        CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
//        encodingFilter.setEncoding("UTF-8");
//        encodingFilter.setForceEncoding(true);
//        http.addFilterBefore(encodingFilter, CsrfFilter.class);
//        http.formLogin()
//                .loginPage(securityProperties.getLoginPage())
//                .failureUrl(securityProperties.getFailureUrl())
//                .loginProcessingUrl(securityProperties.getLoginProcessingUrl())
//                .successHandler(securityAuthenticationSuccessHandler)
//                .failureHandler(securityAuthenticationFailureHandler)
//                .and()
//                .logout()
//                .logoutUrl(securityProperties.getLogoutUrl())
//                .logoutSuccessUrl(securityProperties.getLoginPage())
//                .permitAll()
//                .invalidateHttpSession(true)
//                .deleteCookies("JSESSIONID")
//                .logoutSuccessHandler(securityLogoutSuccessHandler)
//                .and()
//                .authorizeRequests()
//                .antMatchers(securityProperties.getMatchers())
//                .permitAll()
//                .anyRequest()
//                .authenticated()
//                .and()
//                //  .rememberMe()
//                //            .tokenRepository(persistentTokenRepository())
//                //            .tokenValiditySeconds(securityProperties.ngetRememberSeconds())
//                .userDetailsService(webUserDetailsServiceImp)
//                //  .and()
//                .headers()
//                .frameOptions()
//                .disable()
//                .and()
//                .csrf()
//                .disable();
//    }
//
//
//}
//
