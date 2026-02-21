package com.demo.netty.learn.reactor.server;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;

/**
 * @author owen
 * @date 2025/3/25 0:42
 * @description 这个类负责Acceptor交给自己的事件select，在上述例子中实际就是read和send操作。
 */
public class SubReactor implements Runnable {
    private final Selector selector;
    //注册开关
    private boolean register = false;
    //序号，也就是Acceptor初始化SubReactor时的下标
    private int num;

    public SubReactor(Selector selector, int num) {
        this.selector = selector;
        this.num = num;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            System.out.println(String.format("NO %d SubReactor waitting for register...", num));
            while (!Thread.interrupted() && !register) {
                try {
                    //如果没有线程空闲
                    if (selector.select() == 0) {
                        continue;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    dispatch(iterator.next());
                    iterator.remove();
                }
            }
        }
    }

    private void dispatch(SelectionKey key) {
        Runnable r = (Runnable) key.attachment();
        if (r != null) {
            r.run();
        }
    }

    void registering(boolean register) {
        this.register = register;
    }
}
