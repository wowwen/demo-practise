package com.demo.gateway.resolver;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author owen
 * @date 2025/4/23 22:00
 * @description
 */
@Configuration
public class RateLimiteConfig {
    //ip限流
    //userId限流
    //路径限流
    @Bean
    @Primary  //如果不使用 @Primary 注解，项目启动会报错
    public KeyResolver pathKeyResolver(){
        //写法1
//        return exchange-> Mono.just(
//                exchange.getRequest()
//                .getPath()
//                .toString()
//        );
        //写法2
        return new KeyResolver(){
            @Override
            public Mono<String> resolve(ServerWebExchange exchange){
                return Mono.just(exchange.getRequest()
                        .getPath()
                        .toString());
            }
        };
    }
    //根据请求IP限流
    @Bean
    public KeyResolver ipKeyResolver(){
        return exchange -> Mono.just(
                exchange.getRequest()
                        .getRemoteAddress()
                        .getHostName()
        );
    }
    //根据userid限流
    @Bean
    public KeyResolver userKeyResolver(){
        return exchange -> Mono.just(
                exchange.getRequest()
                        .getQueryParams()
                        .getFirst("userId")
        );
    }
}
