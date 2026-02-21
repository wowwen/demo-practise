package com.demo.netty.learn.protobuf;

import com.demo.netty.learn.protobuf.handler.NettyProtoServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;

/**
 * @author juven
 * @date 2025/10/5 2:02
 * @description 采用google的protobuf来实现通信序列化
 */
public class NettyProtoServer {
//    static {
//        System.setProperty("io.netty.tryReflectionSetAccessible", "true");
//    }

    public static void main(String[] args) throws InterruptedException {
        // 创建 BossGroup 和 WorkerGroup
        // 说明
        // 1.创建两个线程组 BossGroup 和 WorkerGroup
        // 2. BossGroup 只是处理连接请求，真正的和客户端业务处理，会交给 WorkerGroup 完成
        // 3. 两个都是无线循环
        // 4. bossGroup 和 workerGroup 含有的子线程(NioEventLoop)的个数
        // 默认 cpu的核数 * 2
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(16);

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup) //设置两个线程组
                    .channel(NioServerSocketChannel.class) //使用NioServerSocketChannel作为i服务器通道实现
                    .option(ChannelOption.SO_BACKLOG, 128) //设置线程队列等待链接队列
                    .childOption(ChannelOption.SO_KEEPALIVE, true) //设置保持活动链接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() { //创建一个通道初始化对象（匿名对象）
                        //给pipeline设置处理器
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            // 在 pipeline 加入 ProtobufDecoder
                            // 指定对那种对象进行解码
                            pipeline.addLast("decoder", new ProtobufDecoder(MyDataInfo.MyMessage.getDefaultInstance()));
                            pipeline.addLast(new NettyProtoServerHandler());
                        }
                    }); // 给我们的WorkerGroup 的 EventLoop 对应的管道设置处理器
            System.out.println(".....服务器 is ready.....");
            // 绑定一个端口，并且同步，生成一个ChannelFuture对象
            // 启动服务器
            ChannelFuture channelFuture = serverBootstrap.bind(6668).sync();
            // 给 cf 注册监听器，监控我们关心的事件
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (channelFuture.isSuccess()){
                        System.out.println("监听端口 6668 成功");
                    }else {
                        System.out.println("监听端口 6668 失败");
                    }
                }
            });
            // 对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
