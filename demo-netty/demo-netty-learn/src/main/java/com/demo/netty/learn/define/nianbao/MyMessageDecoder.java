package com.demo.netty.learn.define.nianbao;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author juven
 * @date 2025/10/7 13:19
 * @description
 */
public class MyMessageDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("MyMessageDecoder decode()方法被调用~~~~");
        // 将得到的二进制字节码转成 MessageProtocol 数据包
        int length = in.readInt();

        byte[] content = new byte[length];
        in.readBytes(content);

        // 封装成 MessageProtocol 对象，放入到 out，传递给下一个 handler进行业务处理
        MessageProtocol messageProtocol = new MessageProtocol();
        messageProtocol.setContent(content);
        messageProtocol.setLen(length);

        out.add(messageProtocol);
    }
}
