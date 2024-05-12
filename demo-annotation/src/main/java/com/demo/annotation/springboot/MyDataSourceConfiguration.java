package com.demo.annotation.springboot;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@ConditionalOnClass(DataSource.class)
//当DingDingMessageImpl类在classpath中不存在时，才会创建相应的bean。这在某些场景下非常有用，例如你可能想要提供一种备选的实现，只有当默认的类不存在时才使用它。
@ConditionalOnMissingClass("com.demo.annotation.springDI.primary.DingDingMessageImpl")
public class MyDataSourceConfiguration {

}
