package com.demo.annotation.springbean;

import org.springframework.stereotype.Component;

/**
 * @Component 注解用于标识一个普通的组件类，没有明确的业务范围，只是通知Spring被注解的类需要被纳入到SpringBean容器中并进行管理
 */
@Component
public class EncryptUserPasswordComponent {

    public String encrypt(String password, String salt){

        return null;
    }
}
