package com.demo.multidatasource.aop.annotation;

import com.demo.multidatasource.aop.enums.DataSourceEnum;
import java.lang.annotation.*;

/**
 * @author owen9
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSource {
    DataSourceEnum value() default DataSourceEnum.PRIMARY;
}
