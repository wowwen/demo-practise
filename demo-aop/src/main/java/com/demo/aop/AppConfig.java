package com.demo.aop;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class AppConfig implements WebMvcConfigurer {

    /**
     * step2: 配置拦截器并设置拦截规则
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**") //拦截所有请求
                .excludePathPatterns("/user/login") //不拦截的url
                .excludePathPatterns("/user/register")
                .excludePathPatterns("/**/*.html"); //不拦截所有页面
    }

    /**
     * aop的另一个作用
     * 给所有接口添加/aop前缀
     * 方式1：通过如下方式添加配置
     * 方式2：通过application.yml文件中配置server.servlet.context-path=/aop
     */
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix("/aop", predicate -> true);
    }

}
