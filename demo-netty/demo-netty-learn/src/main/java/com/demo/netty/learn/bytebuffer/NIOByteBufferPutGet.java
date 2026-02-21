package com.demo.netty.learn.bytebuffer;

import java.nio.ByteBuffer;

/**
 * @author owen
 * @date 2025/3/28 1:49
 * @description
 * ByteBuffer支持类型化的put和get，put放入的是什么数据类型，get就应该使用相应的数据类型来取出，否则可能有BufferUnderflowException异常
 */
public class NIOByteBufferPutGet {
    public static void main(String[] args) {
        // 创建一个Buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(64);
        // 类型化方式放入数据
        byteBuffer.putInt(100);
        byteBuffer.putLong(9);
        byteBuffer.putChar('飞');
        byteBuffer.putShort((short) 4);
        // 取出
        byteBuffer.flip();
        System.out.println(byteBuffer.getInt());
        System.out.println(byteBuffer.getLong());
        System.out.println(byteBuffer.getChar());
        System.out.println(byteBuffer.getShort());


        /**
         * 这样类型不对应的时候，会抛BufferUnderflowException异常
         */
//        System.out.println(byteBuffer.getShort());
//        System.out.println(byteBuffer.getInt());
//        System.out.println(byteBuffer.getLong());
//        System.out.println(byteBuffer.getLong());
    }
}
