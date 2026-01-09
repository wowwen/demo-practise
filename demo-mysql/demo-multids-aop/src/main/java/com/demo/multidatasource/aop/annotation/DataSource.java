package com.demo.multidatasource.aop.annotation;

import com.demo.multidatasource.aop.enums.DataSourceEnum;
import java.lang.annotation.*;

/**
 * @author owen9
 * mybatis-plus中有同样的注解，这算是仿的
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSource {
    DataSourceEnum value() default DataSourceEnum.PRIMARY;
}
