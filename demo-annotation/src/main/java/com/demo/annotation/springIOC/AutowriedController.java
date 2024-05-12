package com.demo.annotation.springIOC;

import com.demo.annotation.springbean.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AutowriedController {

    @Autowired //作用于字段
    private UserService userService;

    /**
     * @Autowired 注解用于标记Spring将要解析和注入的依赖项。
     * 此注解可以作用在构造函数，字段和setter方法上
     */
    /**
     * 下面是@Autowired注解标注构造函数的使用示例
     */
//    @Autowired
//    AutowriedController(UserService userService){
//        this.userService = userService;
//    }

//    @Autowired //作用于setter方法
//    public void setUserService(UserService userService){
//        this.userService = userService;
//    }

}
