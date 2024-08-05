package com.demo.annotation.selfdefine.datacheck;

import java.lang.annotation.*;

/**
 * @author jiangyw
 * @date 2024/8/5 18:23
 * @description
 */
@Documented
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckUserData {

    /**
     * 是否要校验是否为普通管理不允许操做
     * 比如想在哪里不允许普通成员操作则只需添加这个注解并配置checkIsNormal=true
     */
    boolean checkUserData() default false;
}
