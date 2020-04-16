package com.alps.plaform.database.datasource;


import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.alps.plaform.database.aop.DataSourceAOP;
import com.alps.plaform.database.common.DataSourceKey;
import com.alps.plaform.database.common.DynamicDataSource;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;

/**
 * @author:Yujie.lee
 * Date:2019年11月14日
 * TodoTODO
 */
@Configuration
@AutoConfigureBefore(DruidDataSourceAutoConfigure.class)
@ConditionalOnProperty(name = {"spring.datasource.dynamic.enable"}, matchIfMissing = false, havingValue = "true")
@Import(DataSourceAOP.class)
public class DataSourceAutoConfig {
 
	@Autowired
    private Environment env; 
	
//	主库
	@Bean
	@ConfigurationProperties("spring.datasource.druid.portalds")
	public DataSource dataSourceCore(){
	    return DruidDataSourceBuilder.create().build();
	}
	
	@Bean
	@ConfigurationProperties("spring.datasource.druid.slaveportalds")
	public DataSource dataSourceLog(){
	    return DruidDataSourceBuilder.create().build();
	}
	
	
	@Primary
    @Bean // 只需要纳入动态数据源到spring容器
    public DataSource dataSource() {
        DynamicDataSource dataSource = new DynamicDataSource();
        DataSource coreDataSource =  dataSourceCore() ;
        DataSource logDataSource =  dataSourceLog();
        dataSource.addDataSource(DataSourceKey.portal, coreDataSource);
        dataSource.addDataSource(DataSourceKey.sharding, logDataSource);
        dataSource.setDefaultTargetDataSource(coreDataSource);
        return dataSource;
    }

    
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource)
            throws Exception {
    	MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
		sqlSessionFactory.setDataSource(dataSource);
		//以jar包形式提供服务时,需要单独加载Mybatis相关的* mapper.xml
		sqlSessionFactory.setTypeAliasesPackage(env.getProperty("mybatis.typeAliasesPackage"));// 指定基包
		sqlSessionFactory.setMapperLocations(
        new PathMatchingResourcePatternResolver().getResources(env.getProperty("mybatis.mapperLocations")));
		
		MybatisConfiguration configuration = new MybatisConfiguration();
		configuration.setLogImpl(org.apache.ibatis.logging.stdout.StdOutImpl.class);
		configuration.setMapUnderscoreToCamelCase(true);
		configuration.setCacheEnabled(false);
		sqlSessionFactory.setConfiguration(configuration);
		return sqlSessionFactory.getObject();
    }

    
    @Bean // 将数据源纳入spring事物管理
    public DataSourceTransactionManager transactionManager(@Qualifier("dataSource")  DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
   
}
