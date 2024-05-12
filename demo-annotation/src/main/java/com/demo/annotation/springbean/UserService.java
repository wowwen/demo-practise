package com.demo.annotation.springbean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Service 注解时@Component的一个特例，它用户标注业务逻辑类，与@Component注解一样，被此批注标注的类，会自动被Spring管理
 */
@Service("user")
public class UserService {
    @Autowired
    private  UserRepository userRepository;

    public void test(){
        System.out.println("aaaaa");
    }

}
