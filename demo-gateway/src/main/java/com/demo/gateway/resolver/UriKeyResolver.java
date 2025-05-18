package com.demo.gateway.resolver;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author owen
 * @date 2025/4/22 16:10
 * @description
 */
@Component
public class UriKeyResolver  implements KeyResolver {
    @Override
    public Mono<String> resolve(ServerWebExchange exchange) {
        HttpHeaders headers = exchange.getRequest().getHeaders();
        Set<Map.Entry<String, List<String>>> entries = headers.entrySet();
        return Mono.just(exchange.getRequest().getRemoteAddress().getAddress().getHostName());
    }

}
