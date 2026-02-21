package com.demo.netty.learn.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

/**
 * @author owen
 * @date 2025/4/1 19:17
 * @description
 */
public class NettyByteBuf02 {
    public static void main(String[] args) {
        // 创建一个ByteBuf
        ByteBuf byteBuf = Unpooled.copiedBuffer("hello,world!", Charset.forName("utf-8"));

        // 使用相关的 API
        if (byteBuf.hasArray()) { // true
            byte[] content = byteBuf.array();

            // 将 content 转成字符串
            System.out.println(new String(content, Charset.forName("utf-8")));

            System.out.println("byteBuf=" + byteBuf);

            System.out.println(byteBuf.arrayOffset());
            System.out.println(byteBuf.readerIndex());
            System.out.println(byteBuf.writerIndex());// 12（数据长度）
            System.out.println(byteBuf.capacity());// 64（底层数组大小）
            System.out.println(byteBuf.readByte());// 104（'h' 的 ASCII 码）
            // 当前可读的字节数
            int len = byteBuf.readableBytes();// 11（剩余可读字节）
            System.out.println("len=" + len);

            // 使用for循环 取出各个字节
            for (int i = 0; i < len; i++) {
                System.out.println((char) byteBuf.getByte(i));
            }

            System.out.println(byteBuf.getCharSequence(0, 5, Charset.forName("utf-8")));
            System.out.println(byteBuf.getCharSequence(4, 8, Charset.forName("utf-8")));
        }
    }
}
