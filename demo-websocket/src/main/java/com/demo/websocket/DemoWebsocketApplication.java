package com.demo.websocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

/**
 * @author jiangyw
 * @date 2022/3/29 20:09
 * @description
 */
@SpringBootApplication
//way3需要在这里开启websocket
@EnableWebSocket
public class DemoWebsocketApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoWebsocketApplication.class, args);
    }
}
