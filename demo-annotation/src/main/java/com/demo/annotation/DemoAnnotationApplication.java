package com.demo.annotation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author owen
 * @SpringBootApplication 注解是一个快捷的配置注解，在被它标注的类中，可以定义一个或者多个Bean，并自动触发自动配置Bean和自定扫描组件。此注解相当于@Configuration
 * 、@EnableAutoConfiguration和@ComponentScan的组合
 */
@SpringBootApplication
public class DemoAnnotationApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoAnnotationApplication.class, args);
    }
}
