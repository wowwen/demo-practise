package com.demo.redis.jedis.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Component
public class JedisHelper {

    @Autowired
    private JedisPool jedisPool;

    public void set(String key, String value) {
        Jedis jedis = jedisPool.getResource();
        if (null != jedis) {
            jedis.set(key, value);
            jedis.close();
        }
    }

    public String get(String key){
        Jedis jedis = jedisPool.getResource();
        if (null != jedis){
            String value = jedis.get(key);
            jedis.close();
            return value;
        }
        return null;
    }


}
