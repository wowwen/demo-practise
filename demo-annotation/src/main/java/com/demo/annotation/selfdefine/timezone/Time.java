package com.demo.annotation.selfdefine.timezone;

import java.lang.annotation.*;

/**
 * @author owen
 * @date 2024/10/22 22:52
 * @description
 */
@Target({ElementType.FIELD,ElementType.PARAMETER})//注解放置的目标位置别
@Retention(RetentionPolicy.RUNTIME)//注解在哪个阶段执行
@Documented
public @interface Time {

}