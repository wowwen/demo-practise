package com.demo.websocket.springboot.way3;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * @author jiangyw
 * @date 2024/7/15 22:36
 * @description websocket配置类，将自定义处理器，自定义拦截器，websocket操作类依次注入到IOC容器中
 * - @EnableWebSocket：开启WebSocket功能
 * - addHandler：添加处理器
 * - addInterceptors：添加拦截器
 * - setAllowedOrigins：设置允许跨域（允许所有请求来源）
 */
@Configuration
public class WebSocketConfiguration implements WebSocketConfigurer {

    @Bean
    public DefaultWebSocketHandler defaultWebSocketHandler(){
        return new DefaultWebSocketHandler();
    }

    @Bean
    public WebSocketService webSocket(){
        return new WebSocketServiceImpl();
    }

    @Bean
    public WebSocketInterceptor webSocketInterceptor(){
        return new WebSocketInterceptor();
    }


    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        //链接方式：ws://127.0.0.1:9085/globalWs/message?token=qwncjncwqqdnjncz.adqwascsvcgrgb.cbrtbfvb
        // 如果你设置了springboot的 contentPath 那就需要在:9085端口后面 加上contentPath 的值，在拼接上  globalWs/message?token=qwncjncwqqdnjncz.adqwascsvcgrgb.cbrtbfvb
        registry.addHandler(defaultWebSocketHandler(),"/globalWs/message")
                // WebSocketHandlerRegistry的addHandler()方法返回WebSocketHandlerRegistration，WebSocketHandlerRegistration
                // 还有addHandler（）方法可以添加更多的处理器
                .addInterceptors(webSocketInterceptor())
                .setAllowedOrigins("*");

    }
}
