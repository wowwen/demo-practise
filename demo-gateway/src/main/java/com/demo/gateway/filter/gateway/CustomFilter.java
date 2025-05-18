package com.demo.gateway.filter.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author owen
 * @date 2025/4/23 3:10
 * @description
 */
@Component
@Order(1)
@Slf4j
public class CustomFilter implements GatewayFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        System.out.println("请求path：" + path + "处理request");

        HttpHeaders headerStr = exchange.getRequest().getHeaders();
        Mono<Void> filter = chain.filter(exchange);
        System.out.println("filter之后，处理response");

        return filter;
    }

}
