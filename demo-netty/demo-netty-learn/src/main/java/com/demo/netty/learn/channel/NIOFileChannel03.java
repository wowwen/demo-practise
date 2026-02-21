package com.demo.netty.learn.channel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author owen
 * @date 2025/3/28 1:23
 * @description
 * 需求：
 *
 * 1、使用FileChannel（通道）和方法read、write，完成文件的拷贝。
 *
 * 2、拷贝一个文本文件1.txt，放在项目下即可。
 */
public class NIOFileChannel03 {
    public static void main(String[] args) throws IOException {
        FileInputStream fileInputStream = new FileInputStream("d:\\text01.txt");
        FileChannel fileChannel01 = fileInputStream.getChannel();
        FileOutputStream fileOutputStream = new FileOutputStream("2.txt");
        FileChannel fileChannel02 = fileOutputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
        // 循环读取
        while (true) {
            // 重要操作， byteBuffer重置。如果不重置，在第一次读取完成后。position和limit相等了，再就读出来的永远是0了（read会一直是0）
            byteBuffer.clear();
            int read = fileChannel01.read(byteBuffer);
            System.out.println("read = " + read);
            if (read == -1) {// 读取结束
                break;
            }
            // 将buffer中的数据写入到fileChannel02 --> 2.txt
            byteBuffer.flip();
            fileChannel02.write(byteBuffer);
        }
        // 关闭通道和流
        fileChannel01.close();
        fileChannel02.close();
        fileInputStream.close();
        fileOutputStream.close();
    }
}
