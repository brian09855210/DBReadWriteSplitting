package com.hyena.backstage.datasource;

import java.lang.reflect.Method;

import org.apache.logging.log4j.LogManager;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 定義切面
 *
 * @author brian.chang
 * @version 2022/01/21 
 *
 */
@Order(1)
@Aspect
//@Component // 切面必須交給容器管理
public class DynamicDataSourceHandlerAspect {

    @Pointcut("@annotation(com.hyena.backstage.datasource.DynamicDataSourceSwitch)")
    public void pointcut() {
    }

    @Before("pointcut()")
    public void doBefore(JoinPoint joinPoint) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        DynamicDataSourceSwitch annotationClass = method.getAnnotation(DynamicDataSourceSwitch.class);// 獲取method上的註解
        if (annotationClass == null) return;
        
        // 獲取註解上數據源值的內容 + 判斷dataSourceId是否有效
        String dataSourceId = annotationClass.dataSourceId();
        if(!DynamicDataSourceId.containsDataSourceId(dataSourceId)) {
        	LogManager.getLogger().info("dataSourceId無效: " + dataSourceId);
        	return;
        }
        
		// 此處，切換數據源
        DynamicDataSourceContextHolder.setDataSourceId(dataSourceId);
        LogManager.getLogger().info("AOP動態切換數據源，className: " + joinPoint.getTarget().getClass().getName() + " methodName: " + method.getName() + " dataSourceId: " + dataSourceId);
    }

    // 清理掉當前設置的數據源，讓默認數據源不受影響
    @After("pointcut()")
    public void after(JoinPoint point) {
        DynamicDataSourceContextHolder.clearDataSourceId();
    }
}
