package com.demo.redis.jedis.service;

import com.demo.redis.jedis.controller.JedisTestController;
import com.demo.redis.jedis.helper.JedisHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JedisServiceImpl {

    @Autowired
    private JedisHelper jedisHelper;

    @Autowired
    private JedisTestController jedisTestController;

    public void jedisSet(){
        String key = "key";
        String value = "value";

        jedisHelper.set(key, value);
     }
}

