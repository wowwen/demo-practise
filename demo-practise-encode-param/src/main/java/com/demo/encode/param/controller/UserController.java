package com.demo.encode.param.controller;

import com.demo.encode.param.entity.User;
import com.demo.encode.param.service.UserServiceImpl;
import com.demo.practise.common.resp.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @GetMapping("/data/mask/user")
    public Message<User> getAllStudent() throws Exception {
        User user = userServiceImpl.getOne();
        return new Message<User>(user);
    }
}
