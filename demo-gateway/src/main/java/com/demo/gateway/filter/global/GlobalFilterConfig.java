package com.demo.gateway.filter.global;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import reactor.core.publisher.Mono;

/**
 * @author owen
 * @date 2025/4/26 2:10
 * @description 方式2：直接在容器中注入GlobalFilter类型的Bean
 */
@Configuration
public class GlobalFilterConfig {
    //order 越小，越先执行
    @Bean
    @Order(-1)
    public GlobalFilter globalFilter1(){
        return (exchange, chain) -> {
            System.out.println("pro filter globalFilter1...");
            return chain.filter(exchange).then(Mono.fromRunnable(()->{
                System.out.println("post filter globalFilter1...");
            }));
        };
    }
    @Bean
    @Order(1)
    public GlobalFilter globalFilter2(){
        return (exchange, chain) -> {
            System.out.println("pro filter globalFilter2...");
            return chain.filter(exchange).then(Mono.fromRunnable(()->{
                System.out.println("post filter globalFilter2...");
            }));
        };
    }

    /**
     *
     应答顺序为：
     pro filter globalFilter1...
     pro filter globalFilter2...
     post filter globalFilter2...
     post filter globalFilter1...
     */

}
