package com.demo.gateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author owen
 * @date 2025/4/18 18:49
 * @description
 */
@RestController
public class FallbackController {

    /**
     * 如果直接请求gateway的这个地址，会直接返回这个控制器的响应。不会走filter。（控制器的优先于filter）
     * @param exchange
     * @return
     */
    @RequestMapping("/fallback")
    public Mono<ResponseEntity<String>> fallback(ServerWebExchange exchange) {
        System.out.println("=====");
        return Mono.just(ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Service temporarily unavailable. Please try again later."));
    }
}
