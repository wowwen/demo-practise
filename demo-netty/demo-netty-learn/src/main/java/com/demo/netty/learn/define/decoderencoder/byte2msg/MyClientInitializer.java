package com.demo.netty.learn.define.decoderencoder.byte2msg;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @author owen
 * @date 2025/3/31 8:19
 * @description
 */
public class MyClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        //加入一个出站的handler，对数据进行一个编码
        pipeline.addLast(new MyLongToByteEncoder());
        pipeline.addLast(new MyByteToLongDecoder());
        //加入一个自定义的handler，处理业务逻辑
        pipeline.addLast(new MyClientHandler());
    }
}
