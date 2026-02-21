package com.demo.netty.learn.reactor.server;

import java.io.IOException;

/**
 * @author owen
 * @date 2025/3/24 18:16
 * @description
 */
public class MainSubReactorDemo {
    public static void main(String[] args) throws IOException {
        new Thread(new Reactor(2333)).start();
    }
}
