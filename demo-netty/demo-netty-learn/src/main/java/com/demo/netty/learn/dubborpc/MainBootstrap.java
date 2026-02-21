package com.demo.netty.learn.dubborpc;

/**
 * @author owen
 * @date 2025/4/2 10:16
 * @description
 */
public class MainBootstrap {
    public static void main(String[] args) {
        NettyServer.startServer("127.0.0.1", 7000);
    }
}


