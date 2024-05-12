package com.demo.annotation.springDI;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class CustomBeanConfig {

    @Bean("firstBean")
    @DependsOn(value = {"secondBean","thirdBean"}) //@DependsOn注解可以配置Spring IoC容器在初始化一个Bean之前，先初始化其他的Bean对象
    public FirstBean firstBean(){
        return new FirstBean();
    }

    @Bean("secondBean")
    @DependsOn("thirdBean")
    public SecondBean secondBean(){
        return new SecondBean();
    }

    @Bean("thirdBean")
    public ThirdBean thirdBean(){
        return new ThirdBean();
    }

//    @Bean(initMethod = "init", destroyMethod = "destroy")//@Bean注解主要的作用是告知Spring，被此注解所标注的类将需要纳入到Bean管理工厂中
//    public DataBeanInitializer dataBeanInitializer(){
//        System.out.println("================");
//        return new DataBeanInitializer();
//    }
}
