package com.demo.redis.jedis.controller;

import com.demo.redis.jedis.service.JedisServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/jedis")
public class JedisTestController {

    @Autowired
    private JedisServiceImpl jedisService;

    @GetMapping(value = "/set")
    public void test1(){
        jedisService.jedisSet();
    }
}
