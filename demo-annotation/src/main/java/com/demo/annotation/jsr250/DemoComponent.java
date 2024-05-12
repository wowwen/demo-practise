package com.demo.annotation.jsr250;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;

@Component
public class DemoComponent {

    private List<String> list = new ArrayList<>();

    /**
     * @PostConstruct注解用于标注在Bean被Spring初始化之前需要执行的方法
     */
    @PostConstruct
    public void init(){
        System.out.println("Bean初始化之前执行");
        list.add("hello");
        list.add("world");
    }

    /**
     * @PreDestroy注解用户标注Bean被销毁前需要执行的方法
     */
    @PreDestroy
    public void destory(){
        System.out.println("Bean被销毁前执行");
        list.clear();
    }
}
