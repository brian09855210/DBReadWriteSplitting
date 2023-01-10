package com.hyena.backstage.datasource;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定義切換數據源的註解：（此處定義的註解，可用於method上及class上）
 *
 * @author brian.chang
 * @version 2022/01/21 
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DynamicDataSourceSwitch {
    String dataSourceId() default DynamicDataSourceId.MASTER;
}
