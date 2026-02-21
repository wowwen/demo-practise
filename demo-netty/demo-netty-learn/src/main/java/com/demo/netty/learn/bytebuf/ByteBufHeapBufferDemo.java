package com.demo.netty.learn.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author owen
 * @date 2025/3/22 1:25
 * @description 堆缓冲区模式（Heap Buffer） --ByteBuf三种使用模式之一
 */
public class ByteBufHeapBufferDemo {

    public static void main(String[] args) {
        // 创建一个堆缓冲区
        ByteBuf buffer = Unpooled.buffer(10);
        String s = "waylau";
        buffer.writeBytes(s.getBytes());

        // 检查是否是支撑数组
        if (buffer.hasArray()) {

            // 获取支撑数组的引用
            byte[] array = buffer.array();

            // 计算第一个字节的偏移量
            int offset = buffer.readerIndex() + buffer.arrayOffset();

            // 可读字节数
            int length = buffer.readableBytes();
            printBuf(array, offset, length);
        }
    }

    private static void printBuf(byte[] array, int offset, int len) {
        System.out.println("array：" + array);
        System.out.println("array->String：" + new String(array));
        System.out.println("offset：" + offset);
        System.out.println("len：" + len);
    }


}
