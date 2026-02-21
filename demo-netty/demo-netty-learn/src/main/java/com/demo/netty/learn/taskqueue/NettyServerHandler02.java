package com.demo.netty.learn.taskqueue;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * @author owen
 * @date 2025/3/31 2:36
 * @description
 */
public class NettyServerHandler02 extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 比如这里有一个非常耗费时间的业务 -> 异步任务 -> 提交到当前channel对应的 NIOEventLoop 的 taskQueue中
        // 解决方案2 用户自定义的定时任务 -> 该任务是提交到 scheduleTaskQueue 中
        ctx.channel().eventLoop().schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5 * 1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello，客户端~ 喵4", CharsetUtil.UTF_8));
                } catch (InterruptedException e) {
                    System.out.println("发生异常 " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }, 5, TimeUnit.SECONDS);

        System.out.println("go on........");
    }

    // 数据读取完毕
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // writeAndFlush 是 write + flush
        // 将数据写入到缓存，并刷新
        // 一般讲，我们对这个发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello，客户端~", CharsetUtil.UTF_8));
    }

    // 处理异常，一般是需要关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
