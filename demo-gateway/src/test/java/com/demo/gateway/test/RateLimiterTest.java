package com.demo.gateway.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ReactiveRedisTemplate;

/**
 * @author owen
 * @date 2025/4/23 22:33
 * @description
 */
@SpringBootTest
public class RateLimiterTest {

    @Autowired
    private ReactiveRedisTemplate<String, String> redisTemplate;

    @Test
    void testRedisConnection() {
        redisTemplate.opsForValue().set("test", "value")
                .then(redisTemplate.opsForValue().get("test"))
        .subscribe();
    }

}
