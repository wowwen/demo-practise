package com.demo.netty.learn.bytebuffer;

import java.nio.ByteBuffer;

/**
 * @author owen
 * @date 2025/3/28 15:08
 * @description 将一个普通Buffer转成只读Buffer
 */
public class ReadOnlyBuffer {
    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(64);
        for (int i = 0; i < 64; i++) {
            byteBuffer.put(((byte) i));
        }
        //转换成读取
        byteBuffer.flip();
        //得到一个只读的buffer
        ByteBuffer readOnlyBuffer = byteBuffer.asReadOnlyBuffer();
        System.out.println(readOnlyBuffer.getClass());
        while (readOnlyBuffer.hasRemaining()){
            System.out.println(readOnlyBuffer.get());
        }
        // 此处报ReadOnlyBufferException异常
//        readOnlyBuffer.put((byte) 500);
    }
}
