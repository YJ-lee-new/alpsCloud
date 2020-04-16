package com.alps.framework.aspectj;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import com.alps.common.config.datasource.DynamicDataSourceContextHolder;
import com.alps.common.enums.DataSourceType;
/**
 * 多数据源处理
 * 
 * @author : yujie.lee
 */
@Aspect
@Order(1)
@Component
public class DataSourceSlavePortalAspect
{
    protected Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
    @Pointcut("execution(* com.alps.slaveportal.service..*.*(..)) || execution(* com.alps.ace.service..*.*(..))")   
    
    public void dsPointCut()
    {
 
    }
  
    @Around("dsPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable
    {
            DynamicDataSourceContextHolder.setDataSourceType(DataSourceType.SLAVEPTDS.name());
        try
        {
            return point.proceed();
        }
        finally
        {
            // 销毁数据源 在执行方法之后
            DynamicDataSourceContextHolder.clearDataSourceType();
        }
    }
}
