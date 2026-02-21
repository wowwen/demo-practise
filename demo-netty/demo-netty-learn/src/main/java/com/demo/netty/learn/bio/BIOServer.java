package com.demo.netty.learn.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author juven
 * @date 2025/10/8 16:31
 * @description 这里是作为BIO的服务端，客户端可以通过cmd窗口的telnet “ip” “port” 命令来模拟。进入telnet窗口后，按ctrl+]即可以用send命令发送数据
 */
public class BIOServer {
    public static void main(String[] args) throws IOException {
        // 线程池机制
        //思路
        //1.创建一个线程池
        //2.如果有客户端连接，就创建一个线程，与之通讯
        ExecutorService threadPool = Executors.newCachedThreadPool();
        // 创建ServerSocket
        ServerSocket serverSocket = new ServerSocket(6666);
        System.out.println("服务器启动了");
        while (true) {
            // 监听，等待客户端连接
            System.out.println("等待连接......");
            final Socket socket = serverSocket.accept();
            System.out.println("连接到一个客户端");
            // 就创建一个线程，与之通信
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    //可以和客户端通讯
                    handler(socket);
                }
            });
        }
    }

    //编写一个handler方法，和客户端通信
    public static void handler(Socket socket) {
        try {
            System.out.println("线程信息 id=" + Thread.currentThread().getId() + "， 名字：" + Thread.currentThread().getName());
            byte[] bytes = new byte[1024];
            // 通过socket 获取输入流
            InputStream inputStream = socket.getInputStream();
            // 循环读取客户端发送的数据
            while (true) {
                System.out.println("线程信息 id=" + Thread.currentThread().getId() + "， 名字：" + Thread.currentThread().getName());
                System.out.println("read..........");
                //将流中的数据写入到bytes数组中
                int read = inputStream.read(bytes);
                if (read != -1) {
                    //说明还有数据
                    System.out.println(new String(bytes, 0, read, "utf-8"));
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                System.out.println("关闭和client连接");
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
