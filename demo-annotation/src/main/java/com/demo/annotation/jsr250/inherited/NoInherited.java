package com.demo.annotation.jsr250.inherited;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author jiangyw
 * @date 2024/10/23 21:59
 * @description 此注解不带@Inherited注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface NoInherited {
}
