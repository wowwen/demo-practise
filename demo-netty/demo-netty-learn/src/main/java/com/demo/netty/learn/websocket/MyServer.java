package com.demo.netty.learn.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author owen
 * @date 2025/3/30 19:39
 * @description
 * 实例要求：
 *
 * 1、Http 协议是无状态的，浏览器和服务器间的请求响应一下，下一次会重新创建连接；
 *
 * 2、要求：实现基于 WebSocket 的长连接的全双工的交互；
 *
 * 3、改变 Http 协议多次请求的约束，实现长连接了，服务器可以发送消息给浏览器；
 *
 * 4、客户端浏览器和服务器端会相互感知，比如服务器关了，浏览器会感知，同样浏览器关了，服务器会感知；
 */
public class MyServer {
    public static void main(String[] args) throws InterruptedException {
        // 创建两个线程组
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(8);

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO)) // 在bossgroup增加一个日志处理器
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            // 因为基于 Http 协议，使用 http 的编码和解码器
                            pipeline.addLast(new HttpServerCodec());
                            // 是以块方式写，添加 ChunkedWrite 处理器
                            pipeline.addLast(new ChunkedWriteHandler());
                            /**
                             * 说明
                             * 1.因为 http 的数据在传输过程中是分段的，HttpObjectAggregator，可以将多个段聚合起来
                             * 2.这就是为什么当浏览器发送大量数据时，就会发出多次 http 请求
                             */
                            pipeline.addLast(new HttpObjectAggregator(8192));
                            /**
                             * 说明
                             * 1.对于websocket，它的数据是以帧的形式传递
                             * 2.可以看到 WebsocketFrame 下面有六个子类
                             * 3.浏览器请求时 ws://localhost:7000/xxx，表示请求的uri
                             * 4.WebSocketServerProtocolHandler 核心功能是将 http 协议升级为 ws 协议，保持长连接
                             * 5.是通过一个 状态码 101
                             */
                            pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));

                            // 自定义的handler，处理业务逻辑
                            pipeline.addLast(new MyTextWebsocketFrameHandler());
                        }
                    });

            ChannelFuture channelFuture = serverBootstrap.bind(7000).sync();
            channelFuture.channel().closeFuture().sync();

        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
