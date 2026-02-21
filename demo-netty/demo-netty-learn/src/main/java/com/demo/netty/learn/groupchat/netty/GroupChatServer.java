package com.demo.netty.learn.groupchat.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @author owen
 * @date 2025/3/30 0:54
 * @description
 * 需求：
 * 1、编写一个 Netty 群聊系统，实现服务器端和客户端之间的数据简单通讯（非阻塞）；
 * 2、实现多人群聊；
 * 3、服务器端：可以监测用户上线，离线，并实现消息转发功能；
 * 4、客户端：通过 channel 可以无阻塞发送消息给其它所有用户，同时可以接受其它用户发送的消息（有服务器转发得到）
 */
public class GroupChatServer {
    // 监听端口
    private int port;

    public GroupChatServer(int port) {
        this.port = port;
    }

    // 编写 run 方法处理客户端请求
    public void run() throws InterruptedException {
        // 创建两个线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(8);

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // 获取到 pipeline
                            ChannelPipeline pipeline = ch.pipeline();
                            // 向 pipeline 加入一个解码器
                            pipeline.addLast("decoder", new StringDecoder());
                            // 向 pipeline 加入一个编码器
                            pipeline.addLast("encoder", new StringEncoder());
                            // 加入自己的业务处理 handler
                            pipeline.addLast(new GroupChatServerHandler());
                        }
                    });

            System.out.println("netty 服务器启动......");
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();

            // 监听关闭事件
            channelFuture.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        try {
            GroupChatServer groupChatServer = new GroupChatServer(7000);
            groupChatServer.run();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
