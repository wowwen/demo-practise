package com.demo.netty.learn.protobuf;

import com.demo.netty.learn.protobuf.handler.NettyProtoClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufEncoder;

/**
 * @author juven
 * @date 2025/10/5 14:08
 * @description
 */
public class NettyProtoClient {
    public static void main(String[] args) throws InterruptedException {
        // 客户端需要一个事件循环组
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            // 创建客户端启动对象
            // 注意客户端使用的不是 ServerBootStrap 而是 BootStrap
            final Bootstrap bootstrap = new Bootstrap();

            bootstrap.group(group)
                    .channel(NioSocketChannel.class) //设置客户端通道的实现类（反射） 服务端是NioServerSocketChannel
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            final ChannelPipeline pipeline = ch.pipeline();
                            //在pipeline中加入protobuf编码器
                            pipeline.addLast("encoder", new ProtobufEncoder());
                            pipeline.addLast(new NettyProtoClientHandler()); // 加入自己的处理器
                        }
                    });
            System.out.println("客户端 ok....");

            // 启动客户端去连接服务端
            // 关于 ChannelFuture 要分析，涉及到 netty 的异步模型
            final ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 6668).sync();
            // 给关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }
}
