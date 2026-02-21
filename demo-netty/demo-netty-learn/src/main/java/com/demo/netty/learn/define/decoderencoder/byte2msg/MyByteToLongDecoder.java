package com.demo.netty.learn.define.decoderencoder.byte2msg;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author owen
 * @date 2025/3/31 3:17
 * @description
 */
public class MyByteToLongDecoder extends ByteToMessageDecoder {
    /**
     * decode 会根据接收的数据，被调用多次，知道确定没有新的元素被添加到list，或者是Bytebuf 没有更多的可读字节为止
     * 如果 list out 不为空，就会将list的内容传递给下一个 channelInboundHandler 处理，该处理器的方法也会被调用多次
     *
     * @param ctx 上下文
     * @param in 入站的 ByteBuf
     * @param out List 集合，将解码后的数据传给下一个 handler 处理
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("MyByteToLongDecoder 的 decode()方法被调用");
        // 因为 long 是 8 个字节，需要判断有 8 个字节，才能读取一个 long
        if(in.readableBytes() >= 8){
            out.add(in.readLong());
        }
    }
}
