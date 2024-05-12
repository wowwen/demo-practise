package com.demo.annotation.springDI;

import org.springframework.stereotype.Component;

@Component
public class DataBeanInitializer {

    public void init(){
        System.out.println("初始化DataBeanInitializer");
    }

    public void destroy(){
        System.out.println("销毁DataBeanInitializer");
    }
}
