package com.alps.framework.aspectj;

import java.lang.reflect.Method;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import com.alps.common.annotation.DataSource;
import com.alps.common.config.datasource.DynamicDataSourceContextHolder;
import com.alps.common.utils.StringUtils;

/**
 * 多数据源处理
 * 
 * @author : yujie.lee
 * order 指定优先级
 */
@Aspect
@Order(3)
@Component
public class DataSourceAspect
{
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Pointcut(("@annotation(com.alps.common.annotation.DataSource)"))
    public void dsPointCut()
    {

    }

    @Around("dsPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable
    {
        MethodSignature signature = (MethodSignature) point.getSignature();
      //获得当前访问的Method
        Method method = signature.getMethod();

        DataSource dataSource = method.getAnnotation(DataSource.class);

        if (StringUtils.isNotNull(dataSource))
        {
            DynamicDataSourceContextHolder.setDataSourceType(dataSource.value().name());
        }

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
