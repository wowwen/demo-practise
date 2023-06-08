package com.demo.exercise.loadbean;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class TestPostConstruct {
    //静态代码块
    static{
        System.out.println("静态代码static");
    }

    public TestPostConstruct(){
        System.out.println("构造器constructor");
    }

    @PostConstruct
    public void postConstruct(){
        System.out.println("PostConstruct");
    }

    /**
     * 执行结果
     *
     * 静态代码static
     * 构造器constructor
     * PostConstruct
     *
     * order1:TestApplicationRunner实现ApplicationRunner接口
     * order2:TestCommandLineRunner实现CommandLineRunner
     */

}
