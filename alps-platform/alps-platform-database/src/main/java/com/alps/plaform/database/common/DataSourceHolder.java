package com.alps.plaform.database.common;
/**
 * @author:Yujie.lee
 * Date:2019年11月13日
    * 用于数据源切换
 */
public class DataSourceHolder {

	//注意使用ThreadLocal，微服务下游建议使用信号量
    private static final ThreadLocal<DataSourceKey> dataSourceKey = new ThreadLocal<>();

    //得到当前的数据库连接
    public static DataSourceKey getDataSourceKey() {
        return dataSourceKey.get();
    }
    //设置当前的数据库连接
    public static void setDataSourceKey(DataSourceKey type) {
        dataSourceKey.set(type);
    }
    //清除当前的数据库连接
    public static void clearDataSourceKey() {
        dataSourceKey.remove();
    }


}