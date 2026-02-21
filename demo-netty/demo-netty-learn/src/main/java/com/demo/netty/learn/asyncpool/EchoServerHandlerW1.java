package com.demo.netty.learn.asyncpool;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

import java.nio.charset.Charset;
import java.util.concurrent.Callable;

/**
 * @author owen
 * @date 2025/4/8 23:55
 * @description 异步业务线程池方式1：--在handler中加入线程池
 */
@ChannelHandler.Sharable
public class EchoServerHandlerW1 extends ChannelInboundHandlerAdapter {
    // group 就是充当业务线程池，可以将任务提交到该线程池
    // 创建了 16 个线程
    static final EventExecutorGroup group = new DefaultEventExecutorGroup(16);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println("EchoServerHandlerW1 的线程是：" + Thread.currentThread().getName());

        /*ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5 * 1000);
                    // 输出线程名
                    System.out.println("EchoServerHandlerW1 execute 的线程是：" + Thread.currentThread().getName());
                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello，客户端~ 喵2", CharsetUtil.UTF_8));
                } catch (InterruptedException e) {
                    System.out.println("发生异常 " + e.getMessage());
                    e.printStackTrace();
                }

            }
        });*/

        // 将任务提交到 group 线程池
        group.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                // 接收客户端信息
                ByteBuf buf = (ByteBuf) msg;
                byte[] bytes = new byte[buf.readableBytes()];
                buf.readBytes(bytes);
                String body = new String(bytes, Charset.forName("utf-8"));
                // 休眠10秒
                Thread.sleep(10 * 1000);
                System.out.println("group.submit 的 call 线程是" + Thread.currentThread().getName());

                ctx.writeAndFlush(Unpooled.copiedBuffer("hello，客户端~ 2喵喵喵喵", CharsetUtil.UTF_8));
                return null;
            }
        });
        System.out.println("go on.....");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}
