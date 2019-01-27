package com.hqs.flashsales.annotation;

import java.lang.annotation.*;

/**
 * 自定义limit注解
 * @author huangqingshi
 * @Date 2019-01-17
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistriLimitAnno {
     String limitKey() default "limit";
     int limit() default 1;
     String seconds() default "1";
}
