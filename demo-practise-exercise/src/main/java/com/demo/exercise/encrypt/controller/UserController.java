package com.demo.exercise.encrypt.controller;

import com.demo.exercise.encrypt.bean.User;
import com.demo.exercise.encrypt.bean.UserTypeEnum;
import com.demo.practise.common.resp.Message;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = {"/user", "/secret/user"})
public class UserController {
    @RequestMapping("/list")
    Message<List<User>> listUser() {
        List<User> users = new ArrayList<>();
        User u = new User();
        u.setId(1);
        u.setName("boyka");
        u.setRegisterTime(LocalDateTime.now());
        u.setUserTypeEnum(UserTypeEnum.COMMON);
        users.add(u);
        Message<List<User>> response = new Message<>();
        response.setCode(200);
        response.setData(users);
        response.setMessage("成功");
        return response;
    }
}