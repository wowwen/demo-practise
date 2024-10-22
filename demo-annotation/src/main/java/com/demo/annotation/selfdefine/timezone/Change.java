package com.demo.annotation.selfdefine.timezone;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * @author jans9
 */
@Target(ElementType.FIELD)//注解放置的目标位置
@Retention(RetentionPolicy.RUNTIME)//注解在哪个阶段执行
public @interface Change {

    String type() default "";  // 字段类型
}
