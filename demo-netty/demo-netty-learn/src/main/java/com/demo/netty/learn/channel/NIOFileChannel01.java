package com.demo.netty.learn.channel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author owen
 * @date 2025/3/28 0:39
 * @description
 * 需求：
 * 1、使用前面学习的ByteBuffer（缓冲）和FileChannel（通道），将“hello，everybody”写入到text01.txt文件中。
 * 2、文件不存在就创建
 */
public class NIOFileChannel01 {
    public static void main(String[] args) throws IOException {
        String str = "hello, 你好吗";
        // 创建一个输出流 -> channel
        FileOutputStream fileOutputStream = new FileOutputStream("d:\\text01.txt");
        // 通过输出流fileOutputStream获取对应的文件channel
        //这个fileChannel真实类型是FileChannelImpl
        FileChannel fileChannel = fileOutputStream.getChannel();
        // 创建一个缓冲区。缓冲区中只有前面15个byte被填充（hello=5 ,=1 一个汉字在UTF-8编码下占三个byte=9  5+1+9=16 【0-14】）
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        // 将str放入byteBuffer
        byteBuffer.put(str.getBytes());
        // 对byteBuffer进行flip
        byteBuffer.flip();
        // 将byteBuffer 数据写入到channel中
        fileChannel.write(byteBuffer);
        // 关闭流
        fileOutputStream.close();
    }
}
