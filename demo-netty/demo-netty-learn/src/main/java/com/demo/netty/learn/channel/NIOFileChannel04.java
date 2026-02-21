package com.demo.netty.learn.channel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * @author owen
 * @date 2025/3/28 1:35
 * @description
 * 需求：
 *
 * 1、使用FileCHannel（通道）和方法transferFrom，完成文件的拷贝。
 *
 * 2、拷贝一张图片。
 */
public class NIOFileChannel04 {
    public static void main(String[] args) throws IOException {
        // 创建相关流
        FileInputStream fileInputStream = new FileInputStream("d:\\second.jpg");
        FileOutputStream fileOutputStream = new FileOutputStream("d:\\second2.png");
        // 获取各个流对应的fileChannel
        FileChannel sourceChannel = fileInputStream.getChannel();
        FileChannel destChannel = fileOutputStream.getChannel();
        // 使用trandferFrom完成拷贝
        destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        // 关闭相关通道和流
        sourceChannel.close();
        destChannel.close();
        fileInputStream.close();
        fileOutputStream.close();
    }
}
