package com.alps.common.oauth2.autoconfig;

import com.alps.common.oauth2.config.AlpsCommonProperties;
import com.alps.common.oauth2.config.AlpsIdGenProperties;
import com.alps.common.oauth2.config.AlpsScanProperties;
import com.alps.common.oauth2.constants.AlpsOAuth2ClientProperties;
import com.alps.common.oauth2.constants.AlpsRestTemplate;
import com.alps.common.oauth2.constants.RequestMappingScan;
import com.alps.common.oauth2.constants.SnowflakeIdGenerator;
import com.alps.common.oauth2.exception.AlpsGlobalExceptionHandler;
import com.alps.common.oauth2.exception.AlpsRestResponseErrorHandler;
import com.alps.common.oauth2.utils.SpringContextHolder;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.bus.BusProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;
/**
 * @author:Yujie.lee
 * Date:2019年12月7日
 * Todo 默认配置
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({AlpsOAuth2ClientProperties.class})
public class AutoConfiguration {


    @Bean
    @ConditionalOnMissingBean(AlpsScanProperties.class)
     public  AlpsScanProperties scanProperties(){
         return  new AlpsScanProperties();
     }

    /**
     * 分页插件
     */
    @ConditionalOnMissingBean(PaginationInterceptor.class)
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        log.info("PaginationInterceptor [{}]", paginationInterceptor);
        return paginationInterceptor;
    }
//
//    /**
//     * 默认加密配置
//     *
//     * @return
//     */
//    @Bean
//    @ConditionalOnMissingBean(BCryptPasswordEncoder.class)
//    public BCryptPasswordEncoder bCryptPasswordEncoder() {
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        log.info("BCryptPasswordEncoder [{}]", encoder);
//        return encoder;
//    }


    /**
     * Spring上下文工具配置
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(SpringContextHolder.class)
    public SpringContextHolder springContextHolder() {
        SpringContextHolder holder = new SpringContextHolder();
        log.info("SpringContextHolder [{}]", holder);
        return holder;
    }

    /**
     * 统一异常处理配置
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(AlpsGlobalExceptionHandler.class)
    public AlpsGlobalExceptionHandler exceptionHandler() {
        AlpsGlobalExceptionHandler exceptionHandler = new AlpsGlobalExceptionHandler();
        log.info("AlpsGlobalExceptionHandler [{}]", exceptionHandler);
        return exceptionHandler;
    }

    /**
     * ID生成器配置
     *
     * @param properties
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(AlpsIdGenProperties.class)
    public SnowflakeIdGenerator snowflakeIdWorker(AlpsIdGenProperties properties) {
        SnowflakeIdGenerator snowflakeIdGenerator = new SnowflakeIdGenerator(properties.getWorkId(), properties.getCenterId());
        log.info("SnowflakeIdGenerator [{}]", snowflakeIdGenerator);
        return snowflakeIdGenerator;
    }


    /**
     * 自定义注解扫描
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(RequestMappingScan.class)
    public RequestMappingScan resourceAnnotationScan(AmqpTemplate amqpTemplate, AlpsScanProperties scanProperties) {
        RequestMappingScan scan = new RequestMappingScan(amqpTemplate,scanProperties);
        log.info("RequestMappingScan [{}]", scan);
        return scan;
    }

    /**
     * 自定义Oauth2请求类
     *
     * @param alpsCommonProperties
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(AlpsRestTemplate.class)
    public AlpsRestTemplate alpsRestTemplate(AlpsCommonProperties alpsCommonProperties, BusProperties busProperties, ApplicationEventPublisher publisher) {
        AlpsRestTemplate restTemplate = new AlpsRestTemplate(alpsCommonProperties, busProperties, publisher);
        //设置自定义ErrorHandler
        restTemplate.setErrorHandler(new AlpsRestResponseErrorHandler());
        log.info("AlpsRestTemplate [{}]", restTemplate);
        return restTemplate;
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        //设置自定义ErrorHandler
        restTemplate.setErrorHandler(new AlpsRestResponseErrorHandler());
        log.info("RestTemplate [{}]", restTemplate);
        return restTemplate;
    }

  
}
