package com.demo.netty.learn.zerocopy.java;

import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author owen
 * @date 2025/3/23 16:09
 * @description Java NIO 提供的MappedByteBuffer，用于提供mmap/write方式.只适合大文件，小文件效率不高。
 */
public class MmapWriteDemo {
    public static void main(String[] args) throws IOException {
        File file = new File("D:\\OWEN\\demo-practise\\demo-netty\\demo-netty-learn\\src\\main\\resources\\data.txt");
        //rw为模式，固定写法，有r， rw， rws。 rwd
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
        FileChannel fileChannel = randomAccessFile.getChannel();
        MappedByteBuffer map = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, file.length());
        map.put("ze".getBytes());
        fileChannel.position(file.length());
        map.clear();
        fileChannel.write(map);
    }

}
