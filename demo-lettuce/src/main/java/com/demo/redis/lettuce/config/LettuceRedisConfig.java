package com.demo.redis.lettuce.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.Serializable;

/**
 * 默认的模板只能存储字符串类型的，所以需要自定义redistemplate，满足多样的数据结构
 */
@Configuration
public class LettuceRedisConfig {

    public RedisTemplate<String, Serializable> redisTemplate(LettuceConnectionFactory connectionFactory){
        RedisTemplate<String, Serializable> redisTemplate = new RedisTemplate<>();
        // 设置key的序列化器
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //设置Value采用什么样的序列化方式
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        //hash key也采用String的序列化方式
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<Object>(Object.class));
        //设置数据源的连接工厂(默认会传入框架中自带的（也就是读取完配置文件装配的）LettuceConnectionFactory)
        // 也可以自己定义，注入容器，再通过@Qualifier("")传进来
        redisTemplate.setConnectionFactory(connectionFactory);
        return redisTemplate;
    }



}
