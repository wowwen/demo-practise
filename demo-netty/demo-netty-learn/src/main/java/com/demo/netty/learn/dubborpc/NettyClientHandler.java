package com.demo.netty.learn.dubborpc;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;

/**
 * @author owen
 * @date 2025/4/2 20:10
 * @description
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter implements Callable<Object> {
    //定义属性
    //感觉这三个变量都有并发问题？？？
    private ChannelHandlerContext context; //上下文
    private String result; //调用后返回的结果
    private String param; //客户端调用方法时，传入的参数

    /**
     * Callable接口有一个非常重要的方法call()
     * 被代理对象调用，发送数据给服务器 -> wait -> 等待被唤醒 -> 返回结果
     * 一句话，就是客户端要自己控制什么时候发消息，channelActive不行，而且还要等待结果返回所以要wait，等channelRead返回后call再返回
     */
    @Override
    public synchronized Object call() throws Exception {
        System.out.println("call 被调用 before");
        context.writeAndFlush(param); //把参数发过去
        //进行wait
        wait(); //等待channelRead方法获取到服务器的结果后，唤醒
        System.out.println("call 被调用 after");
        return result; //服务方返回的结果
    }

    /**
     * 与服务器的连接创建成功后，就会被调用
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelActive 被调用");
        if (context == null) {
            context = ctx; //因为在其他方法会使用到这个ctx
        }
    }

    /**
     * 收到服务器的数据后，就会被调用
     */
    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("channelRead 被调用");
        result = msg.toString();
        notify(); //唤醒等待的线程
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.channel().close();
    }

    public void setParam(String param) {
        System.out.println("setParam 被调用");
        this.param = param;
    }
}
