package com.demo.netty.learn.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author owen
 * @date 2025/3/22 0:36
 * @description 区别于java.nio的ByteBuffer，Netty自己创建了ButeBuf缓存
 */
public class ByteBufDemo {
    public static void main(String[] args) {
        //创建一个缓冲区
        ByteBuf buf = Unpooled.buffer(10);
        System.out.println("--初始缓冲区--");
        printBuf(buf);

        //添加数据
        System.out.println("--添加数据--");
        String s = "AAA";
        buf.writeBytes(s.getBytes());
        printBuf(buf);

        //读取数据
        System.out.println("--读取数据--");
        while (buf.isReadable()){
            System.out.println(buf.readByte());
        }
        printBuf(buf);

        //执行释放空间
        System.out.println("--执行discardReadBytes--");
        buf.discardReadBytes();
        printBuf(buf);

        //清空缓存
        System.out.println("--执行clear清空缓存区--");
        buf.clear();
        printBuf(buf);
    }

    private static void printBuf(ByteBuf buf){
        System.out.println("readerIndex:" + buf.readerIndex());
        System.out.println("writerIndex:" + buf.writerIndex());
        System.out.println("capacity:" + buf.capacity());
    }

}
