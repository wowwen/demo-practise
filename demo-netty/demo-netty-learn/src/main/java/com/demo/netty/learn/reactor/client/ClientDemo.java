package com.demo.netty.learn.reactor.client;

/**
 * @author owen
 * @date 2025/3/25 22:29
 * @description
 */
public class ClientDemo {
    public static void main(String[] args) {
        new Thread(new NIOClient("127.0.0.1", 2333)).start();
        new Thread(new NIOClient("127.0.0.1", 2333)).start();
    }
}
