package com.demo.encode.param.service;

import com.demo.encode.param.entity.User;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl {
    public User getOne() {
        User user = User.builder().id(1).name("玛利亚").age(18).email("22@qq.com").build();
        return user;
    }
}
