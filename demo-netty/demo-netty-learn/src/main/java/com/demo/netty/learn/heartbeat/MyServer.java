package com.demo.netty.learn.heartbeat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author owen
 * @date 2025/3/30 14:24
 * @description
 * 需求：
 *
 * 1、编写一个 Netty 心跳检测机制案例，当服务器超过3秒没有读时，就提示读空闲；
 *
 * 2、当服务器超过5秒没有写操作时，就提示写空闲；
 *
 * 3、实现当服务器超过7秒没有读或写操作时，就提示读写空闲。
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
                            // 加入 netty 提供的 IdleStateHandler
                            /**
                             * 说明
                             * 1. IdleStateHandler 是 netty 提供的处理空闲状态的处理器
                             * 2. readerIdleTime：表示多长时间没有读，就会发送一个心跳检测包，检测是否还是连接状态
                             * 3. writerIdleTime：表示多长时间没有写，就会发送一个心跳检测包，检测是否还是连接状态
                             * 4. allIdleTime：表示多长时间没有读写，就会发送一个心跳检测包，检测是否还是连接状态
                             * 5. 当 IdleStateEvent 触发后，就会传递给管道的下一个handler进行处理，通过调用（触发）下一个 handler 的userEventTrigged 方法，
                             * 在该方法中去处理 IdleStateEvent 事件
                             */
                            pipeline.addLast(new IdleStateHandler(3,5,7, TimeUnit.SECONDS));
                            // 加入一个对空闲检测进一步处理的handler（自定义）
                            pipeline.addLast(new MyServerHandler());
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
