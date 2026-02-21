package com.demo.netty.learn.define.nianbao.server;

import com.demo.netty.learn.define.nianbao.MyMessageDecoder;
import com.demo.netty.learn.define.nianbao.MyMessageEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @author juven
 * @date 2025/10/7 3:12
 * @description
 */
public class MyServerInitializerV2 extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new MyMessageDecoder());
        pipeline.addLast(new MyMessageEncoder());
        pipeline.addLast(new MyServerHandlerV2());
    }
}
