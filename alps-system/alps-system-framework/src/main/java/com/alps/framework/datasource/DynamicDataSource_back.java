package com.alps.framework.datasource;

import java.util.HashMap;
import java.util.Map;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
/**
 * 动态数据源
 * 
 * @author : yujie.lee
 */
public class DynamicDataSource_back extends AbstractRoutingDataSource {
    private static DynamicDataSource_back instance;
    private static byte[] lock=new byte[0];
    private static Map<Object,Object> dataSourceMap=new HashMap<Object, Object>();

    @Override
    public void setTargetDataSources(Map<Object, Object> targetDataSources) {
        super.setTargetDataSources(targetDataSources);
        dataSourceMap.putAll(targetDataSources);
        super.afterPropertiesSet();// 必须添加该句，否则新添加数据源无法识别到
    }

    public Map<Object, Object> getDataSourceMap() {
        return dataSourceMap;
    }

    public static synchronized DynamicDataSource_back getInstance(){
        if(instance==null){
            synchronized (lock){
                if(instance==null){
                    instance=new DynamicDataSource_back();
                }
            }
        }
        return instance;
    }
    //必须实现其方法
    protected Object determineCurrentLookupKey() {
        return DataSourceContextHolder.getDBType();
    }
}
