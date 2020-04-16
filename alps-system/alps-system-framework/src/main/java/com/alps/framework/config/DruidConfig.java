package com.alps.framework.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.alps.framework.config.properties.DruidProperties;
import com.alps.framework.datasource.DynamicDataSource;
import com.alps.common.enums.DataSourceType;
/**
 * druid 配置多数据源
 * 
 * @author : yujie.lee
 */
@Configuration
public class DruidConfig
{
    @Bean
    @ConfigurationProperties("spring.datasource.druid.portalds")
    public DataSource masterDataSource(DruidProperties druidProperties)
    {
        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
        return druidProperties.dataSource(dataSource);
    }
    
    @Bean
    @ConfigurationProperties("spring.datasource.druid.slaveportalds")
    @ConditionalOnProperty(prefix = "spring.datasource.druid.slaveportalds", name = "enabled", havingValue = "true")
    public DataSource slavePortalDataSource(DruidProperties druidProperties)
    {
        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
        dataSource.setBreakAfterAcquireFailure(true);
        return druidProperties.dataSource(dataSource);
    }
    
    @Bean
    @ConfigurationProperties("spring.datasource.druid.busds")
    @ConditionalOnProperty(prefix = "spring.datasource.druid.busds", name = "enabled", havingValue = "true")
    public DataSource slaveDataSource(DruidProperties druidProperties)
    {
        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
        dataSource.setBreakAfterAcquireFailure(true);
        return druidProperties.dataSource(dataSource);
    }
    
    @Bean
    @ConfigurationProperties("spring.datasource.druid.nbds")
    @ConditionalOnProperty(prefix = "spring.datasource.druid.nbds", name = "enabled", havingValue = "true")
    public DataSource NBDataSource(DruidProperties druidProperties)
    {
        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
        dataSource.setBreakAfterAcquireFailure(true);
        return druidProperties.dataSource(dataSource);
    }
   /*
    * 动态切换数据源
    */
    @Bean(name = "dynamicDataSource")
    @Primary
    public DynamicDataSource dataSource(DataSource masterDataSource, DataSource slavePortalDataSource ,DataSource slaveDataSource,DataSource NBDataSource)
    {
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DataSourceType.PORTALDS.name(), masterDataSource);
        targetDataSources.put(DataSourceType.SLAVEPTDS.name(), slavePortalDataSource);
        targetDataSources.put(DataSourceType.BUSDS.name(), slaveDataSource);
        targetDataSources.put(DataSourceType.NBDS.name(), NBDataSource);
        return new DynamicDataSource(masterDataSource, targetDataSources);
    }
    
//    /*
//     * 动态切换数据源
//     */
//     @Bean(name = "dynamicDataSource")
//     @Primary
//     public DynamicDataSource_back dataSource(DataSource masterDataSource, DataSource slaveDataSource)
//     {
//         DynamicDataSource_back dynamicDataSource = DynamicDataSource_back.getInstance();
//         Map<Object, Object> targetDataSources = new HashMap<>();
//         targetDataSources.put(DataSourceType.PORTALDS.name(), masterDataSource);
//         targetDataSources.put(DataSourceType.BUSDS.name(), slaveDataSource);
//         dynamicDataSource.setTargetDataSources(targetDataSources);
//         dynamicDataSource.setDefaultTargetDataSource(masterDataSource);
//         return dynamicDataSource;
//     }
////     
//     @Bean
//     public SqlSessionFactory sqlSessionFactory(
//             @Qualifier("dynamicDataSource") DataSource dynamicDataSource)
//             throws Exception {
//         SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
//         bean.setDataSource(dynamicDataSource);
//         bean.setMapperLocations(new PathMatchingResourcePatternResolver()
//                 .getResources("classpath*:mapper/*.xml"));
//         return bean.getObject();
//
//     }
//
//     @Bean(name = "sqlSessionTemplate")
//     public SqlSessionTemplate sqlSessionTemplate(
//             @Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory)
//             throws Exception {
//         return new SqlSessionTemplate(sqlSessionFactory);
//     }
}
