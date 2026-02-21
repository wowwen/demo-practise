package com.demo.netty.learn.reactor.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Set;


/**
 * @author owen
 * @date 2025/3/24 18:46
 * @description
 */
public class Reactor implements Runnable {
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;

    public Reactor(int port) throws IOException {
        //打开一个selector
        selector = Selector.open();
        //建立一个server端通道
        serverSocketChannel = ServerSocketChannel.open();
        //绑定端口。 地址默认为网卡地址
        serverSocketChannel.socket().bind(new InetSocketAddress(port));
        //SELECTOR模式下，所有通道必须是非阻塞的。这里代表的是client连接server的socketchannel不阻塞
        serverSocketChannel.configureBlocking(false);
        //Reactor是入口，最初给一个channel注册上去的事件都是accept
        SelectionKey selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        //绑定Acceptor处理类
        selectionKey.attach(new Acceptor(serverSocketChannel));
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                //就绪事件到达之前，阻塞。意思是没有可用的channel
                int count = selector.select();
                if (count == 0) {
                    continue;
                }
                // 拿到本次select获取的就绪事件
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                for (SelectionKey selectionKey : selectionKeys) {
                    //进行任务分发
                    dispatch(selectionKey);
                }
                selectionKeys.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void dispatch(SelectionKey selectionKey) {
        //附带对象为Acceptor
        Runnable runnable = (Runnable) selectionKey.attachment();
        //调用之前注册的回调对象
        if (runnable != null){
            runnable.run();
        }
    }


}
