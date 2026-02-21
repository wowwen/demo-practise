package com.demo.netty.learn.define.protocol.server;

import com.demo.netty.learn.define.protocol.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 服务端消息处理器
 * 对于消息的处理，主要是要根据消息的不同类型，对消息进行相应的处理，
 * 比如对于request类型消息，要写入响应数据，对于ping消息，要写入pong消息作为回应。
 * 下面我们通过定义Netty handler的方式实现对消息的处理：
 *
 * @author owen
 */
public class ServerMessageHandler extends SimpleChannelInboundHandler<Message> {
    /**
     * 获取一个消息处理器工厂类实例
     */
    private MessageResolverFactory resolverFactory = MessageResolverFactory.getInstance();

    /**
     * 1. ChannelHandlerContext ctx:上下文对象, 含有 管道pipeline , 通道channel, 地址
     * 2. Object msg: 就是客户端发送的数据 默认Object.这里是因为前面的SimpleChannelInboundHandler的泛型已经指定为了Message
   */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message message) throws Exception {
        //获取消息处理器
        Resolver resolver = resolverFactory.getMessageResolver(message);
        //对消息进行处理并获取响应数据
        Message result = resolver.resolve(message);
        //将响应数据写入到处理器中
        ctx.writeAndFlush(result);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        //注册request消息处理器
        resolverFactory.registerResolver(new RequestMessageResolver());
        //注册response消息处理器
        resolverFactory.registerResolver(new ResponseMessageResolver());
        //注册ping消息处理器
        resolverFactory.registerResolver(new PingMessageResolver());
        //注册pong消息处理器
        resolverFactory.registerResolver(new PongMessageResolver());
    }
}
