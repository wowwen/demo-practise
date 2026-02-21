package com.demo.netty.learn.channel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author owen
 * @date 2025/3/28 1:10
 * @description
 * 需求：
 * 1、使用前面学习的ByteBuffer（缓冲）和FileChannel（通道），将text01.txt文件中的数据读入到程序中，并显示在控制台屏幕。
 * 2、假定文件已经存在。
 */
public class NIOFileChannel02 {
    public static void main(String[] args) throws IOException {
        // 创建文件的输入流
        File file = new File("d:\\text01.txt");
        FileInputStream fileInputStream = new FileInputStream(file);
        // 通过fileInputStream 获取对应的FileChannel -> 实际类型 FileChannelImpl
        FileChannel fileChannel = fileInputStream.getChannel();
        // 创建缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());
        // 将通道的数据读入到buffer中
        fileChannel.read(byteBuffer);
        // 将 byteBuffer的字节数据转成String
        System.out.println(new String(byteBuffer.array()));
        //关闭流
        fileInputStream.close();
    }
}
