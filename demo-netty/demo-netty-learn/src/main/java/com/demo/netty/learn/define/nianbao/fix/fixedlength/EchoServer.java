package com.demo.netty.learn.define.nianbao.fix.fixedlength;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author juven
 * @date 2025/10/11 15:11
 * @description - FixedLengthFrameDecoder：以固定长度进行数据包的解码；
 */
public class EchoServer {
    public void bind(int port) throws InterruptedException{
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // 这里将FixedLengthFrameDecoder添加到pipeline中，指定长度为20,用于处理固定长度的消息的粘包、拆包问题
                            ch.pipeline().addLast(new FixedLengthFrameDecoder(20)); //处理顺序1
                            // 将前一步解码得到的数据转码为字符串
                            ch.pipeline().addLast(new StringDecoder()); //处理顺序2
                            // 这里FixedLengthFrameEncoder是我们自定义的，用于将长度不足20的消息进行补全空格
                            ch.pipeline().addLast(new FixedLengthFrameEncoder(20)); //处理顺序4
                            // 最终的数据处理
                            ch.pipeline().addLast(new EchoServerHandler()); //处理顺序3
                        }
                    });

            ChannelFuture future = bootstrap.bind(port).sync();
            future.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new EchoServer().bind(8080);
    }
}
