package com.demo.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

/**
 * @author owen
 * @date 2025/4/23 1:50
 * @description
 */
@Configuration
public class GatewayBeanConfig {

//    @Bean
//    public RouteLocator routes(RouteLocatorBuilder builder) {
//        System.out.println("修改请求与响应BODY");
//        return builder.routes()
//                .route("service-02", r -> r.path("/modify/**")
//                        .filters(f -> f.modifyRequestBody(String.class, String.class,
//                                                (exchange, body) -> Mono.just(body.toUpperCase()))
//                                        .modifyRequestBody(String.class, String.class,
//                                                (exchange, body) -> Mono.just(body.toUpperCase())))
//                        .uri("http://localhost:8099"))
//                .build();
//    }


}
