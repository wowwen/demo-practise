package com.demo.netty.learn.bytebuffer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * @author owen
 * @date 2025/3/28 15:53
 * @description Scattering：将数据写入到buffer时，可以采用buffer数组，依次写入
 * Gathering：从buffer读取数据时，可以采用buffer数组，依次读
 * <p>
 * 使用telnet进行测试，打开cmd命令行窗口，连接命令：telnet 127.0.0.1 7000
 * <p>
 * 发送消息快捷键 ctrl+]
 */
public class ScatteringAndGatheringTest {
    public static void main(String[] args) throws IOException {
        // 使用ServerSocketChannel 和 SocketChannel网络
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //创建server的address
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);
        //绑定端口到socket，并启动
        serverSocketChannel.socket().bind(inetSocketAddress);
        // 创建Buffer数组
        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);
        // 等待客户端连接(telnet)
        SocketChannel socketChannel = serverSocketChannel.accept();
        // 假定接收从客户端接收8个字节
        int messageLength = 8;
        //循环读取
        while (true) {
            int byteRead = 0;
            while (byteRead < messageLength) {
                long readCount = socketChannel.read(byteBuffers);
                // 累计读取的字节数
                byteRead += readCount;
                System.out.println("byteRead=" + byteRead);
                // 使用流打印, 看看当前的这个buffer的position和limit
                Arrays.asList(byteBuffers).stream()
                        .map(buffer -> "position=" + buffer.position() + " ,limit=" + buffer.limit())
                        .forEach(System.out::println);
            }
            // 将所有的buffer进行反转
            //Arrays.asList(byteBuffers).stream().map(buffer -> buffer.flip());
            Arrays.asList(byteBuffers).forEach(buffer -> buffer.flip()); //注意stream().map遍历不会改变原来的值
            // 将数据读出，显示到客户端
            long byteWrite = 0;
            while (byteWrite < messageLength) {
                long writeCount = socketChannel.write(byteBuffers);
                byteWrite += writeCount;
            }
            // 将所有的buffer进行clear操作
//                Arrays.asList(byteBuffers).forEach(buffer -> buffer.clear());
            System.out.println("byteRead=" + byteRead + " ,byteWrite=" + byteWrite + ", messageLength=" + messageLength);
        }
    }
}

