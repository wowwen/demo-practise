package com.demo.netty.learn.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author owen
 * @date 2025/4/1 11:18
 * @description
 * 1、 netty服务器在6668端口监听，浏览器发出请求http://localhost:6668/；
 * 在写netty的http server的例子过程中，发现浏览器使用端口号6668一直无法连接，报错ERR_UNSAFE_PORT。改成7000就可以了。
 * 2、 服务器可以回复消息给客户端“hello，我是服务器”，并对特定请求资源进行过滤；
 * 3、 目的：netty可以做http服务开发，并且理解handler实例和客户端及其请求的关系；
 */
public class TestServer {
    public static void main(String[] args) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(8);

        try {
            // 2.创建服务器端启动对象，配置参数
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,128)// 设置线程队列得到连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE,true)// 设置保持活动连接
                    .childHandler(new TestServerInitializer());
            System.out.println("服务器就绪...");
            ChannelFuture channelFuture = serverBootstrap.bind(8848).sync();

            // 给 cf 注册监听器，监控我们关心的事件
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()) {
                        System.out.println("监听端口 8848 成功");
                    } else {
                        System.out.println("监听端口 8848 失败");
                    }
                }
            });

            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
