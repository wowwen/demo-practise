package com.demo.netty.learn.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author owen
 * @date 2025/3/28 23:58
 * @description
 */
public class NIOClient {
    public static void main(String[] args) throws IOException {
        // 得到一个网络通道
        SocketChannel socketChannel = SocketChannel.open();
        // 设置非阻塞模式
        socketChannel.configureBlocking(false);
        // 提供服务器端的IP和端口
        InetSocketAddress socketAddress = new InetSocketAddress("127.0.0.1", 6666);
        // 连接服务器
        if(!socketChannel.connect(socketAddress)){
            while (!socketChannel.finishConnect()){
                System.out.println("因为连接需要时间，客户端不会阻塞，可以做其他工作......");
            }
        }
        // 如果连接成功就发送数据
        String str = "hello, 大家好....";
        //这种方式创建buffer会按照str的大小创建，以防大了浪费，小了不够用
        ByteBuffer buffer = ByteBuffer.wrap(str.getBytes());
        // 发送数据，将buffer写入到channel中
        socketChannel.write(buffer);
        //代码会停在这
        System.in.read();
    }
}
