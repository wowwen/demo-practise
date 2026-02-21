package com.demo.netty.learn.define.nianbao.client;

import com.demo.netty.learn.define.nianbao.MyMessageDecoder;
import com.demo.netty.learn.define.nianbao.MyMessageEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @author juven
 * @date 2025/10/7 11:43
 * @description
 */
public class MyClientInitializerV2 extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //加入编码器
        pipeline.addLast(new MyMessageEncoder());
        //加入解码器
        pipeline.addLast(new MyMessageDecoder());
        pipeline.addLast(new MyClientHandler());
    }
}
