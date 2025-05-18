package com.demo.gateway.gray;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author owen
 * @date 2025/4/27 11:02
 * @description 请求上下文过滤器
 * 上下文管理要点：
 * 请求级别隔离：使用ThreadLocal保证请求间上下文隔离
 *
 * 资源清理：通过doFinally确保上下文及时清理，避免内存泄漏
 *
 * 信息收集：可扩展收集各种灰度决策所需信息
 */
@Component
public class GrayRequestContextFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 创建灰度请求上下文
        GrayRequestContext context = new GrayRequestContext();
        // 从原始请求中提取需要的信息
        HttpHeaders headers = exchange.getRequest().getHeaders();
        context.setHeaders(headers);
        // 可以从Cookie、JWT等提取更多信息
        if (headers.containsKey("User-Id")) {
            context.setUserId(headers.getFirst("User-Id"));
        }

        // 将上下文存入ThreadLocal
        GrayRequestContextHolder.setContext(context);

        // 继续过滤器链，并在完成后清理上下文
        return chain.filter(exchange)
                .doFinally(signal -> GrayRequestContextHolder.clearContext());
    }
}
