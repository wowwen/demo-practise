package com.demo.annotation.springDI;

import javax.annotation.Resource;

public class FirstBean {
    @Resource
    private SecondBean secondBean;

    @Resource
    private ThirdBean thirdBean;

    public FirstBean() {
    }
}
