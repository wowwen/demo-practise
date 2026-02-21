package com.demo.netty.learn.taskqueue;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @author owen
 * @date 2025/3/31 2:34
 * @description
 */
public class NettyServerHandler01  extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 比如这里有一个非常耗费时间的业务 -> 异步任务 -> 提交到当前channel对应的 NIOEventLoop 的 taskQueue中
        // 解决方案1 用户程序自定义的普通任务
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10 * 1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello，客户端~", CharsetUtil.UTF_8));
                } catch (InterruptedException e) {
                    System.out.println("发生异常 " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
        //任务二
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("任务二...");
                ctx.channel().writeAndFlush(Unpooled.copiedBuffer("hello，客户端3~", CharsetUtil.UTF_8));
            }
        });

        System.out.println("go on........");
        //客户端
        //会收到：hello，客户端2~
        //再收到：hello，客户端~
        //再收到：hello，客户端3~
    }

    // 数据读取完毕
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // writeAndFlush 是 write + flush
        // 将数据写入到缓存，并刷新
        // 一般讲，我们对这个发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello，客户端2~", CharsetUtil.UTF_8));
    }

    // 处理异常，一般是需要关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
    /**
     * 这是我们启动程序发现：首先收到消息：hello,客户端…；然后过 5秒 收到消息：hello，客户端~ 喵2
     */
}
