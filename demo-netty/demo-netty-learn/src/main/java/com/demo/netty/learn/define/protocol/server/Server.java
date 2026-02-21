package com.demo.netty.learn.define.protocol.server;

import com.demo.netty.learn.define.protocol.MessageDecoder;
import com.demo.netty.learn.define.protocol.MessageEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author owen
 */
public class Server {
    public static void main(String[] args) {
        //说明
        //1. 创建两个线程组 bossGroup 和 workerGroup
        //2. bossGroup 只是处理连接请求 , 真正的和客户端业务处理，会交给 workerGroup完成
        //3. 两个都是无限循环
        //4. bossGroup 和 workerGroup 含有的子线程(NioEventLoop)的个数
        //   默认实际 cpu核数 * 2
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootStrap = new ServerBootstrap();
            bootStrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    //设置线程队列等待连接的个数，option主要是针对boss线程组
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    //设置保持活动连接状态 child主要是针对worker线程组
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            //添加用于处理粘包和拆包问题的处理器
                            // maxFrameLength：发送数据包最大的长度。
                            // lengthFieldOffset：长度域偏移量，指的是长度域位于整个数据包字节数组中的下标。
                            // lengthFieldLength：长度域的字节长度。
                            // lengthAdjustment：长度域的偏移量矫正。对一些不仅包含有消息头和消息体的数据进行消息头的长度的调整，这样就可以只得到消息体的数据，这里的 lengthAdjustment 指定的就是消息头的长度；
                            // initialBytesToStrip：丢弃的初始字节数。丢弃处于有效数据前面的字节数量。对于长度字段在消息头中间的情况，可以通过 initialBytesToStrip 忽略掉消息头以及长度字段占用的字节。
                            pipeline.addLast(new LengthFieldBasedFrameDecoder(1024, 0, 4, 0, 4));
                            pipeline.addLast(new LengthFieldPrepender(4));
                            //添加自定义协议消息的编码和解码处理器
                            pipeline.addLast(new MessageEncoder());
                            pipeline.addLast(new MessageDecoder());
                            //添加具体的消息处理器
                            pipeline.addLast(new ServerMessageHandler());
                        }
                    });
            ChannelFuture future = bootStrap.bind(8585).sync();
            //给future注册监听器，监控我们关心的事件（实际上没什么用，可以不写，此处只是做演示）
            future.addListener(new ChannelFutureListener(){
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()){
                        System.out.println("Server启动成功");
                    }else {
                        System.out.println("Server启动失败");
                    }
                }
            });
            //对关闭通道进行监听
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
