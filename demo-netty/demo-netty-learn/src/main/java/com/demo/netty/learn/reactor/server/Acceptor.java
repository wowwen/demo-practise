package com.demo.netty.learn.reactor.server;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author owen
 * @date 2025/3/24 21:40
 * @description 该模块负责处理连接就绪的事件，并初始化一批subReactor进行分发处理，拿到客户端的socketChannel，绑定Handler，这样就可以继续完成接下来的读写任务了。
 */
public class Acceptor implements Runnable {
    private final ServerSocketChannel serverSocketChannel;
    //CPU数
    private final int coreNum = Runtime.getRuntime().availableProcessors();
    //创建selector
    private final Selector[] selectors = new Selector[coreNum];
    //轮询使用subReactor的下标索引
    private int next = 0;
    //创建数组，里面装的都是SubReactor实例
    private SubReactor[] subReactors = new SubReactor[coreNum];
    // subReactor的处理线程
    private Thread[] threads = new Thread[coreNum];

    Acceptor(ServerSocketChannel serverSocketChannel) throws IOException {
        this.serverSocketChannel = serverSocketChannel;
        //初始化
        for (int i = 0; i < coreNum; i++) {
            selectors[i] = Selector.open();
            //初始化SubReactor
            subReactors[i] = new SubReactor(selectors[i], i);
            //初始化运行subReactor的线程，即这个线程用来分配跑这个reactor
            threads[i] = new Thread(subReactors[i]);
            //启动（启动后的执行参考SubReactor里的run方法）
            threads[i].start();
        }
    }

    @Override
    public void run() {
        SocketChannel socketChannel;
        try {
            //连接，即acceptor连接mainReactor的serverSocketChannel，注意这个serverSocketChannel是从mainReactor传进来的
            socketChannel = serverSocketChannel.accept();
            if (socketChannel != null) {
                System.out.println(String.format("accpet %s", socketChannel.getRemoteAddress()));
                //这里代表的是mainReactor连接acceptor的channel不阻塞
                socketChannel.configureBlocking(false);
                // 注意一个selector在select时是无法注册新事件的，因此这里要先暂停下select方法触发的程序段，
                // 下面的weakup和这里的setRestart都是做这个事情的，具体参考SubReactor里的run方法
                subReactors[next].registering(true);
                // 使一个阻塞住的selector操作立即返回
                selectors[next].wakeup();
                //注册一个读事件
                SelectionKey selectionKey = socketChannel.register(selectors[next], SelectionKey.OP_READ);
                // 使一个阻塞住的selector操作立即返回
                selectors[next].wakeup();
                // 本次事件注册完成后，需要再次触发select的执行，
                // 因此这里Restart要在设置回false（具体参考SubReactor里的run方法）
                subReactors[next].registering(false);
                // 绑定Handler
                selectionKey.attach(new AsyncHandler(socketChannel, selectors[next], next));
                //注意此处比较的是selectors.length,是比下标要多1的，所以当next下标索引到达最后一个后，++next后的值就与selector.length相等了
                if (++next == selectors.length) {
                    //越界后重新分配
                    next = 0;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

