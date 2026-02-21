package com.demo.netty.learn.define.nianbao.server;

import com.demo.netty.learn.define.nianbao.MessageProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;
import java.util.UUID;

/**
 * @author juven
 * @date 2025/10/7 3:13
 * @description
 */
public class MyServerHandlerV2 extends SimpleChannelInboundHandler<MessageProtocol> {
    private int count;

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        // 接收到数据，并处理
        final int len = msg.getLen();
        final byte[] content = msg.getContent();
        System.out.println();
        System.out.println("服务端接收到信息如下");
        System.out.println("长度=" + len);
        System.out.println("内容=" + new String(content, Charset.forName("utf-8")));

        System.out.println("服务器接收到消息包数量=" + (++this.count));

        // 回复消息
        String responseContent = UUID.randomUUID().toString();
        int responseLen = responseContent.getBytes(Charset.forName("utf-8")).length;
        // 构建一个协议包
        MessageProtocol messageProtocol = new MessageProtocol();
        messageProtocol.setLen(responseLen);
        messageProtocol.setContent(responseContent.getBytes(Charset.forName("utf-8")));

        ctx.writeAndFlush(messageProtocol);
    }
}
