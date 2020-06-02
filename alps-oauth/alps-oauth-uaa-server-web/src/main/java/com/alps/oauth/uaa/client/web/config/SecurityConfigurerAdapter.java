package com.alps.oauth.uaa.client.web.config;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.alps.oauth.uaa.client.web.hander.SecurityAuthenticationFailureHandler;
import com.alps.oauth.uaa.client.web.hander.SecurityAuthenticationSuccessHandler;
import com.alps.oauth.uaa.client.web.hander.SecurityLogoutSuccessHandler;
import com.alps.oauth.uaa.client.web.priorities.SecurityProperties;
/**
 * 
 * @author YUJIE.lee
 * @version 
 * @category
 *
 */
@Configuration
@EnableWebSecurity
public class SecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
    @Autowired
    private SecurityProperties securityProperties;
    @Autowired
    private SecurityAuthenticationSuccessHandler securityAuthenticationSuccessHandler;
    @Autowired
    private SecurityAuthenticationFailureHandler securityAuthenticationFailureHandler;
    @Autowired
    private SecurityLogoutSuccessHandler securityLogoutSuccessHandler;
    @Autowired
    private UserDetailsService securityUserDetailsService;
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;
    @Resource
    private DataSource dataSource;
   
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new  BCryptPasswordEncoder();
    }
    @Bean
    public JdbcClientDetailsService clientDetailsService() {
        JdbcClientDetailsService jdbcClientDetailsService = new JdbcClientDetailsService(dataSource);
        jdbcClientDetailsService.setPasswordEncoder(passwordEncoder());
        return jdbcClientDetailsService;
    }
    
    @Bean
    public RedisTokenStore redisTokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
    }  
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(securityUserDetailsService).passwordEncoder(passwordEncoder());
    }
    
    @Override
    public void configure(HttpSecurity http) throws Exception{
        CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
        encodingFilter.setEncoding("UTF-8");
        encodingFilter.setForceEncoding(true);
        http.addFilterBefore(encodingFilter, CsrfFilter.class);
        http.formLogin()
                .loginPage(securityProperties.getLoginPage())
                .failureUrl(securityProperties.getFailureUrl())
                .loginProcessingUrl(securityProperties.getLoginProcessingUrl())
                .successHandler(securityAuthenticationSuccessHandler)
                .failureHandler(securityAuthenticationFailureHandler)
                .and()
                .logout()
                .logoutUrl(securityProperties.getLogoutUrl())
                .logoutSuccessUrl(securityProperties.getLoginPage())
                .permitAll()
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessHandler(securityLogoutSuccessHandler)
                .and()
                .authorizeRequests()
                .antMatchers(securityProperties.getMatchers())
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                //  .rememberMe()
                //            .tokenRepository(persistentTokenRepository())
                //            .tokenValiditySeconds(securityProperties.ngetRememberSeconds())
                .userDetailsService(securityUserDetailsService)
                //  .and()
                .headers()
                .frameOptions()
                .disable()
                .and()
                .csrf()
                .disable();
    }
}
