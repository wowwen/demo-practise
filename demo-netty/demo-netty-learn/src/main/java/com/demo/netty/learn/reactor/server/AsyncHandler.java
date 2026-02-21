package com.demo.netty.learn.reactor.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author owen
 * @date 2025/3/25 2:17
 * @description AsyncHandler负责接下来的读写操作。
 */
public class AsyncHandler implements Runnable {
    private final Selector selector;
    private final SelectionKey selectionKey;
    private final SocketChannel socketChannel;
    private ByteBuffer readBuffer = ByteBuffer.allocate(1024);
    private ByteBuffer sendBuffer = ByteBuffer.allocate(2048);
    //读取就绪
    private final static int READ = 0;
    //响应就绪
    private final static int SEND = 1;
    //处理中
    private final static int PROCESSING = 2;
    // 所有连接完成后都是从一个读取动作开始的
    private int status = READ;
    // 从反应堆（subReactor）序号
    private int num;
    //这种利用Executors工厂方法创建线程池
    private static final ExecutorService workers = Executors.newFixedThreadPool(5);

    AsyncHandler(SocketChannel socketChannel, Selector selector, int num) throws IOException {
        //为了区分Handler被哪个从反应堆（SubReactor）触发执行做的标记
        this.num = num;
        //接收客户端的连接
        this.socketChannel = socketChannel;
        //设置为非阻塞模式
        this.socketChannel.configureBlocking(false);
        //将客户端注册到selector
        selectionKey = socketChannel.register(selector, 0);
        //附加处理对象，当前是Handler对象
        selectionKey.attach(this);
        // 连接已完成，那么接下来就是读取动作
        selectionKey.interestOps(SelectionKey.OP_READ);
        this.selector = selector;
        this.selector.wakeup();
    }

    @Override
    public void run() {
        // 如果一个任务正在异步处理，那么这个run是直接不触发任何处理的，
        // read和send只负责简单的数据读取和响应，业务处理完全不阻塞这里的处理
        switch (status) {
            case READ:
                read();
                break;
            case SEND:
                send();
                break;
            default:
        }
    }

    private void read() {
        if (selectionKey.isValid()) {
            try {
                readBuffer.clear();
                // read方法结束，意味着本次"读就绪"变为"读完毕"，标记着一次就绪事件的结束
                //todo 为什么readbuffer清除了 还会从readbuffer里面read
                int count = socketChannel.read(readBuffer);
                if (count > 0) {
                    // 置为处理中
                    status = PROCESSING;
                    // 丢到线程池异步处理
                    workers.execute(this::readWorker);
                } else {
                    selectionKey.cancel();
                    socketChannel.close();
                    System.out.println(String.format("NO %d SubReactor read closed", num));
                }
            } catch (IOException e) {
                System.err.println("处理read业务时发生异常！异常信息：" + e.getMessage());
                selectionKey.cancel();
                try {
                    socketChannel.close();
                } catch (IOException e1) {
                    System.err.println("处理read业务关闭通道时发生异常！异常信息：" + e.getMessage());
                }
            }
        }
    }

    void send() {
        if (selectionKey.isValid()) {
            status = PROCESSING; // 置为执行中
            workers.execute(this::sendWorker); // 异步处理
            selectionKey.interestOps(SelectionKey.OP_READ); // 重新设置为读
        }
    }

    // 读入信息后的业务处理
    private void readWorker() {
        try {
            // 模拟一段耗时操作
            Thread.sleep(5000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            System.out.println(String.format("NO %d %s -> Server：%s",
                    num,
                    socketChannel.getRemoteAddress(),
                    new String(readBuffer.array())));
        } catch (IOException e) {
            System.err.println("异步处理read业务时发生异常！异常信息：" + e.getMessage());
        }
        status = SEND;
        selectionKey.interestOps(SelectionKey.OP_WRITE); // 注册写事件
        this.selector.wakeup(); // 唤醒阻塞在select的线程
    }

    private void sendWorker() {
        try {
            sendBuffer.clear();
            sendBuffer.put(String.format("NO %d SubReactor recived %s from %s",
                    num,
                    new String(readBuffer.array()),
                    socketChannel.getRemoteAddress()).getBytes());
            sendBuffer.flip();

            // write方法结束，意味着本次写就绪变为写完毕，标记着一次事件的结束
            int count = socketChannel.write(sendBuffer);

            if (count < 0) {
                // 同上，write场景下，取到-1，也意味着客户端断开连接
                selectionKey.cancel();
                socketChannel.close();
                System.out.println(String.format("%d SubReactor send closed", num));
            }

            // 没断开连接，则再次切换到读
            status = READ;
        } catch (IOException e) {
            System.err.println("异步处理send业务时发生异常！异常信息：" + e.getMessage());
            selectionKey.cancel();
            try {
                socketChannel.close();
            } catch (IOException e1) {
                System.err.println("异步处理send业务关闭通道时发生异常！异常信息：" + e.getMessage());
            }
        }
    }


}
