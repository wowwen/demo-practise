package com.demo.redis.lettuce.helper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * opsForZSet()：对应redis中的zset （有序集合）
 * opsForValue()：对应redis中的String (字符串类型)
 * opsForHash()：对应redis中的Hash （哈希）
 * opsForList()：对应redis中的List（链表）
 * opsForSet()：对应redis中的Set（集合）
 * ————————————————
 * 版权声明：本文为CSDN博主「MrYuShiwen」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
 * 原文链接：https://blog.csdn.net/MrYushiwen/article/details/122493709
 */
@Component
@Slf4j
public class RedisHelper {

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 设置值
     *
     * @param key
     * @param value
     */
    public boolean set(String key, Object value) {
        try {
            redisTemplate.boundValueOps(key).set(value);
            return true;
        } catch (Exception e) {
            log.error("redis set error : ", e);
            return false;
        }
    }

    /**
     * 获取值并转换为对应对象
     *
     * @param key
     * @param clazz
     * @return
     */
    public Object get(String key, Class clazz) {
        try {
            return Optional.ofNullable(redisTemplate.boundValueOps(key).get())
                    .map(obj -> JSON.toJavaObject((JSONObject) obj, clazz)).orElse(null);
        } catch (Exception e) {
            log.error("redis get error : ", e);
            return null;
        }
    }


}
