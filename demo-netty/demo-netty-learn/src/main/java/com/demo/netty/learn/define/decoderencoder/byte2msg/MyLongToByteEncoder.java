package com.demo.netty.learn.define.decoderencoder.byte2msg;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author owen
 * @date 2025/3/31 9:14
 * @description
 */
public class MyLongToByteEncoder extends MessageToByteEncoder<Long> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Long msg, ByteBuf out) throws Exception {
        System.out.println("MyLongToByteEncoder 的 encode()方法被调用");
        System.out.println("msg = " + msg);
        out.writeLong(msg);
    }
}
