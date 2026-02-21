package com.demo.netty.learn.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author owen
 * @date 2025/3/28 21:43
 * @description
 * 需求：
 * 1、编写一个NIO入门案例，实现服务器端和客户端之间的数据简单通讯（非阻塞）
 * 2、目的：理解NIO非阻塞网络编程机制
 */
public class NIOServer {
    public static void main(String[] args) throws IOException {
        // 创建ServerSocketChannel -> ServerSocket
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 得到一个Selector实例
        Selector selector = Selector.open();
        // 绑定一个端口6666，在服务器端监听
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));
        // 设置为非阻塞
        serverSocketChannel.configureBlocking(false);
        // 把 serverSocketChannel 注册到Selector中，关心事件为 OP_ACCEPT,注意：OP_ACCEPT仅对只对**服务端套接字**有效（ServerSocketChannel）
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        //返回所有已注册的通道键集合,这是所有的selectionkey（无论有没有事件发生）
        final Set<SelectionKey> keys = selector.keys();
        System.out.println("注册后selectionKey的数量：" + keys.size());

        // 循环等待客户端连接
        while (true) {
            // 等待一秒钟，如果没有事件发生，返回
            if (selector.select(1000) == 0) {// 没有事件发生
                System.out.println("服务器等待了1秒，无连接");
                continue;
            }
            // 如果返回的>0，就获取到相关的SelectionKey集合
            // 1.如果返回的>0，表示已经获取到关注的事件
            // 2.selector.selectedKeys() 返回关注事件的集合
            // 通过selectionKeys反向获取通道
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            System.out.println("有事件就绪的selectionKeys数量 = " + selectionKeys.size());
            // 遍历集合Set<SelectionKey>
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
            while (keyIterator.hasNext()) {
                // 获取到SelectionKey
                SelectionKey key = keyIterator.next();
                // 根据key对应的通道发生的事件做相应的处理
                if (key.isAcceptable()) {// 如果是 OP_ACCEPT，有新的客户端连接
                    // 给该客户端生成一个SocketChannel.注意：虽然accept()方法是阻塞的，但是上面if已经知道了有连接上来了，所以这个accept（）很快就返回了
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    //每连接一次会产生一个新的channel
                    System.out.println("客户端连接成功 生成一个socketchannel" + socketChannel.hashCode());
                    //将socketChannel设置为非阻塞， 解决java.nio.channels.IllegalBlockingModeException
                    socketChannel.configureBlocking(false);
                    //服务端通常先监听OP_ACCEPT（上面的注册代码），接受连接后，再为每个SocketChannel注册 OP_READ/OP_WRITE
                    // 将SocketChannel 注册到selector，关注事件为OP_READ，同时给socketChannel关联一个Buffer
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                    System.out.println("from " + socketChannel.hashCode() + " 客户端建立了连接");
                    System.out.println("客户端连接后，注册的selectionKey的数量：" + selector.keys().size());
                }
                if(key.isReadable()){//如果是发生OP_READ
                    // 通过key反向获取到对应的channel （强转--向下转型）
                    SocketChannel channel = (SocketChannel) key.channel();
                    // 获取到该channel关联的buffer
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    int read = channel.read(buffer);
                    System.out.println("From 客户端 " + new String(buffer.array()));
                }
                // 手动从集合中移除当前的SelectionKey，通常我们在处理完一个就绪事件后，需要手动从该集合中移除对应的SelectionKey。如果不移除，那么下一次select()操作返回时，这个SelectionKey还会在集合中，即使它对应的事件并没有就绪（因为SelectionKey不会自动从selectedKeys集合中移除），这会导致重复处理同一个通道的事件。
                keyIterator.remove();
            }
        }
    }
}
