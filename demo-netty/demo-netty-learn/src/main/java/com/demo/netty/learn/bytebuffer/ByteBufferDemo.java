package com.demo.netty.learn.bytebuffer;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/**
 * @author owen
 * @date 2025/3/27 15:21
 * @description java nio的bytebuffer演示
 */
public class ByteBufferDemo {
    public static void main(String[] args) {
        // 举例说明buffer的使用(简单说明)
        // 创建一个Buffer, 大小为5，即可以存放5个int
        IntBuffer intBuffer = IntBuffer.allocate(5);
        // 向buffer中存放数据
        for (int i = 0; i < intBuffer.capacity(); i++) {
            intBuffer.put(i * 2);
        }
        // 如何从buffer读取数据
        // 将buffer转换，读写切换
        intBuffer.flip();
        while (intBuffer.hasRemaining()){
            System.out.println(intBuffer.get());
        }

        //创建一个缓冲区，创建的是堆内存
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        System.out.println("------------初始时缓冲区------------");
        printBuffer(byteBuffer);

        // 添加一些数据到缓冲区中
        System.out.println("------------添加数据到缓冲区------------");

        String s = "love";
        byteBuffer.put(s.getBytes());
        printBuffer(byteBuffer);

        // 切换成读模式
        System.out.println("------------执行flip切换到读取模式------------");
        byteBuffer.flip();
        printBuffer(byteBuffer);

        // 读取数据
        System.out.println("------------读取数据------------");
        // 创建一个limit()大小的字节数组(因为就只有limit这么多个数据可读)
        byte[] bytes = new byte[byteBuffer.limit() - 1];

        // 将读取的数据装进我们的字节数组中，会直接放进bytes数组中
        byteBuffer.get(bytes);
        printBuffer(byteBuffer);

        // 执行compact
        System.out.println("------------执行compact------------");
        byteBuffer.compact();
        printBuffer(byteBuffer);

        // 执行clear
        System.out.println("------------执行clear清空缓冲区------------");
        byteBuffer.clear();
        printBuffer(byteBuffer);
    }

    /**
     * 打印出ByteBuffer的信息
     *
     * @param buffer
     */
    private static void printBuffer(ByteBuffer buffer) {
        System.out.println("content:" + buffer.get(1));
        System.out.println("mark：" + buffer.mark());
        System.out.println("position：" + buffer.position());
        System.out.println("limit：" + buffer.limit());
        System.out.println("capacity：" + buffer.capacity());
    }
}
