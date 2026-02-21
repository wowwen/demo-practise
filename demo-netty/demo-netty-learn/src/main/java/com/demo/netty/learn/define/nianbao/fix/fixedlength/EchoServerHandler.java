package com.demo.netty.learn.define.nianbao.fix.fixedlength;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author juven
 * @date 2025/10/11 17:43
 * @description
 */
public class EchoServerHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println("server receives message: " + msg.trim());
        ctx.writeAndFlush("hello client!");
    }
}
