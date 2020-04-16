package com.alps.framework.datasource;

/**
 * Created by YHYR on 2017-12-25
 */

public class DataSourceContextHolder {
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

    public static synchronized void setDBType(String dbType){
    	 if (dbType == null) {
             throw new NullPointerException();
         }
         System.err.println("[将当前数据源改为]：" + dbType);
        contextHolder.set(dbType);
    }

    public static String getDBType(){
        return contextHolder.get();
    }

    public static void clearDBType(){
        contextHolder.remove();
    }
}
