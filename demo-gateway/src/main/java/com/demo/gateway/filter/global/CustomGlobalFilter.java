package com.demo.gateway.filter.global;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author owen
 * @date 2025/4/26 0:57
 * @description 方式1：实现GlobalFilter接口。GlobalFilter只需要添加@Component注解即可
 */
@Component
public class CustomGlobalFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        System.out.println("GlobalFilter：" + path + "处理request");

        HttpHeaders headerStr = exchange.getRequest().getHeaders();
        Mono<Void> filter = chain.filter(exchange);
        System.out.println("GlobalFilter的filter之后，处理response");
        return filter;
    }
}
