package com.alps.plaform.database.datasource;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.alps.plaform.database.common.DataSourceKey;
import com.alps.plaform.database.common.DynamicDataSource;
import com.alps.plaform.database.common.ModuloDatabaseShardingAlgorithm;
import com.alps.plaform.database.common.ModuloTableShardingAlgorithm;

import io.shardingjdbc.core.api.config.ShardingRuleConfiguration;
import io.shardingjdbc.core.api.config.TableRuleConfiguration;
import io.shardingjdbc.core.api.config.strategy.StandardShardingStrategyConfiguration;
import io.shardingjdbc.core.jdbc.core.datasource.ShardingDataSource;
/**
 * @author:Yujie.lee
 * Date:2019年11月13日
 * TodoTODO
 */
@Configuration
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class}) //排除DataSourceConfiguratrion
@ConditionalOnProperty(name = { "spring.datasource.sharding.enable" }, matchIfMissing = false, havingValue = "true")
public class ShardingDataSourceConfig {
	
	@Bean
	@ConfigurationProperties("spring.datasource.druid.master")
	public DataSource dataSourceMaster() {
		return DruidDataSourceBuilder.create().build();
	}

	@Bean
	@ConfigurationProperties("spring.datasource.druid.slave")
	public DataSource dataSourceSlave() {
		return DruidDataSourceBuilder.create().build();
	}

	@Bean
	@ConfigurationProperties("spring.datasource.druid.single")
	public DataSource dataSourceSingle() {
		return DruidDataSourceBuilder.create().build();
	}
	
	
	
	@Bean(name = "shardingDataSource")
	public DataSource getShardingDataSource(@Qualifier("dataSourceMaster") DataSource dataSourceMaster,
			@Qualifier("dataSourceSlave") DataSource dataSourceSlave) throws SQLException {
		ShardingRuleConfiguration shardingRuleConfig;
		shardingRuleConfig = new ShardingRuleConfiguration();
		shardingRuleConfig.getTableRuleConfigs().add(getUserTableRuleConfiguration());
		shardingRuleConfig.getBindingTableGroups().add("t_order");
		shardingRuleConfig.setDefaultDatabaseShardingStrategyConfig(
				new StandardShardingStrategyConfiguration("user_id", ModuloDatabaseShardingAlgorithm.class.getName()));
		shardingRuleConfig.setDefaultTableShardingStrategyConfig(
				new StandardShardingStrategyConfiguration("order_id", ModuloTableShardingAlgorithm.class.getName()));

		Map<String, DataSource> dataSourceMap = new HashMap<>(); // 设置分库映射
		dataSourceMap.put("test_msg0", dataSourceMaster);
		dataSourceMap.put("test_msg1", dataSourceSlave);

		return new ShardingDataSource(shardingRuleConfig.build(dataSourceMap));
	}
	
	
	@Bean // 只需要纳入动态数据源到spring容器
    @Primary
    public DataSource dataSource(@Qualifier("shardingDataSource") DataSource shardingDataSource) {
        DynamicDataSource dataSource = new DynamicDataSource();
        DataSource coreDataSource = this.dataSourceSingle();
        dataSource.addDataSource(DataSourceKey.sharding, shardingDataSource);
        dataSource.addDataSource(DataSourceKey.portal, coreDataSource);
        dataSource.setDefaultTargetDataSource(coreDataSource);
        return dataSource;
    }
	

	/**
	 * 设置表的node
	 * 
	 * @return
	 */
	@Bean
	public TableRuleConfiguration getUserTableRuleConfiguration() {
		TableRuleConfiguration orderTableRuleConfig = new TableRuleConfiguration();
		orderTableRuleConfig.setLogicTable("t_order");
		orderTableRuleConfig.setActualDataNodes("test_msg${0..1}.t_order_${0..1}");
		orderTableRuleConfig.setKeyGeneratorColumnName("order_id");
		return orderTableRuleConfig;
	}

	/**
	 * 需要手动配置事务管理器
	 * 
	 * @param dataSource
	 * @return
	 */
	@Bean
	public DataSourceTransactionManager transactitonManager(@Qualifier("dataSource") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	@Bean(name = "sqlSessionFactory")
	public SqlSessionFactory testSqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSource);
		bean.setMapperLocations(
				new PathMatchingResourcePatternResolver().getResources("classpath*:com/central/**/dao/*.xml"));
		return bean.getObject();
	}

	@Bean // 将数据源纳入spring事物管理
	public DataSourceTransactionManager transactionManager(@Qualifier("dataSource") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

}
