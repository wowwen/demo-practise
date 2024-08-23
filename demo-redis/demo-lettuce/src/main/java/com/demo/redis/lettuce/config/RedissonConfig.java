package com.demo.redis.lettuce.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author jans9
 */
@Configuration
public class RedissonConfig {
    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private String port;

    @Bean
    public RedissonClient redissonClient(){
        Config config = new Config();
        // redis为单机模式
        // starter依赖进来的redisson要以redis://开头，其他不用
        config.useSingleServer().setAddress("redis://" + host + ":" + port);
        RedissonClient redissonClient = Redisson.create(config);
        System.out.println("配置redission完成");
        return redissonClient;
    }

}
