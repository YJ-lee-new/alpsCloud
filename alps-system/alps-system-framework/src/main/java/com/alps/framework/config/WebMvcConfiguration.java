package com.alps.framework.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
/**
 * @author:Yujie.lee
 * Date:2019年12月7日
 *  配置全局不需要登录的uri
 */
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    /**
     * 多个WebSecurityConfigurerAdapter
     */
    @Configuration
    @Order(101)
    public static class ApiWebSecurityConfiguration extends WebSecurityConfigurerAdapter {
//        @Override
//        public void configure(WebSecurity web) throws Exception {
//            web.ignoring().antMatchers(
//                    "/error",
//                    "/login2",
//                    "/static/**",
//                    "/v2/api-docs/**",
//                    "/swagger-resources/**",
//                    "/webjars/**",
//                    "/favicon.ico");
//        }
    	@Override
    	protected void configure(HttpSecurity http) throws Exception {
    	http
    	.authorizeRequests()
    	.antMatchers("/**").permitAll()
    	.anyRequest().authenticated()
    	.and().csrf().disable();
    	
    	http.headers().frameOptions().disable();
    	}
    }
    


//    /**
//     * 资源处理器
//     *
//     * @param registry
//     */
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
//        registry.addResourceHandler("swagger-ui.html", "doc.html")
//                .addResourceLocations("classpath:/META-INF/resources/");
//        registry.addResourceHandler("/webjars/**")
//                .addResourceLocations("classpath:/META-INF/resources/webjars/");
//    }
}
