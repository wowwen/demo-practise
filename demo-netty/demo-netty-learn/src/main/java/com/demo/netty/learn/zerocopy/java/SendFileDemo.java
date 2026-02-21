package com.demo.netty.learn.zerocopy.java;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

/**
 * @author owen
 * @date 2025/3/23 21:34
 * @description Java FileChannel.transferTo() 底层实现就是通过 Linux 的 sendfile实现的。该方法直接将当前通道内容传输到另一个通道，没有涉及Buffer的任何操作。
 */
public class SendFileDemo {
    public static void main(String[] args) throws IOException {
        File file = new File("D:\\OWEN\\demo-practise\\demo-netty\\demo-netty-learn\\src\\main\\resources\\data.txt");
        //使用sendfile读取磁盘文件，并网络发送
        FileChannel fileChannel = new RandomAccessFile(file, "rw").getChannel();
        //todo 连接socket
        SocketChannel socketChannel = SocketChannel.open();
        fileChannel.transferTo(0, fileChannel.size(), socketChannel);

    }
}
