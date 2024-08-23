package com.demo.redis.lettuce.config;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.Serializable;

/**
 * 默认的模板只能存储字符串类型的，所以需要自定义redistemplate，满足多样的数据结构
 * @author jans9
 */
@Configuration
public class LettuceRedisConfig {

    @Bean
    public RedisTemplate<String, Serializable> redisTemplate(LettuceConnectionFactory connectionFactory){
        RedisTemplate<String, Serializable> redisTemplate = new RedisTemplate<>();

        FastJsonRedisSerializer<Object> fastJsonRedisSerializer = new FastJsonRedisSerializer<>(Object.class);
        // 设置key的序列化器
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //设置Value采用什么样的序列化方式
//        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        redisTemplate.setValueSerializer(fastJsonRedisSerializer);
        //hash key也采用String的序列化方式
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
//        redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));

        redisTemplate.setHashValueSerializer(fastJsonRedisSerializer);
        //设置数据源的连接工厂(默认会传入框架中自带的（也就是读取完配置文件装配的）LettuceConnectionFactory)
        // 也可以自己定义，注入容器，再通过@Qualifier("")传进来
        redisTemplate.setConnectionFactory(connectionFactory);
        System.out.println("配置Lettuce完成");
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    /**
     * 如果开启下面的Bean配置，则上面的Bean不会初始化，只会初始化下面的这个，会导致采用的是
     */

//    @Bean
//    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory  redisConnectionFactory) {
//        RedisTemplate<String, Object> template = new RedisTemplate<>();
//        //使用fastjson序列化
//        FastJsonRedisSerializer<Object> fastJsonRedisSerializer = new FastJsonRedisSerializer<>(Object.class);
//        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
//        // value值的序列化采用fastJsonRedisSerializer
//        template.setValueSerializer(fastJsonRedisSerializer);
//        template.setHashValueSerializer(fastJsonRedisSerializer);
//        // key的序列化采用StringRedisSerializer
//        template.setKeySerializer(stringRedisSerializer);
//        template.setHashKeySerializer(stringRedisSerializer);
//        template.setConnectionFactory(redisConnectionFactory);
//        template.afterPropertiesSet();
//        System.out.println("配置默认值完成");
//        return template;
//    }


}
