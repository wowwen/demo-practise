package com.demo.netty.learn.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @author owen
 * @date 2025/4/1 11:19
 * @description
 */
public class TestServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        // 向管道加入处理器
        // 得到管道
        ChannelPipeline pipeline = ch.pipeline();

        // 加入一个netty提供的httpServerCodec codec => [coder - decoder]
        // HttpServerCodec 说明
        // 1. HttpServerCodec 是 netty 提供的处理 http 的 编-解码器
        pipeline.addLast("MyHttpServerCodec", new HttpServerCodec());
        // 2. 增加一个自定义的处理器
        pipeline.addLast("MyTestHttpServerHandler", new TestHttpServerHandler());
    }
}
