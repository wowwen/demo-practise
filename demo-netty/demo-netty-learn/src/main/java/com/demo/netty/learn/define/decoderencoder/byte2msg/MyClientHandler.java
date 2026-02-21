package com.demo.netty.learn.define.decoderencoder.byte2msg;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author owen
 * @date 2025/3/31 9:18
 * @description
 */
public class MyClientHandler extends SimpleChannelInboundHandler<Long> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {
        System.out.println("服务器的ip=" + ctx.channel().remoteAddress());
        System.out.println("服务器回送的消息：" + msg );
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("MyClientHandler 发送数据");
        ctx.writeAndFlush(123456L);

        // 分析
        // 1. "abcdabcdabcdabcd" 是16个字节
        // 2. 该处理器的前一个 handler 是 MyLongToByteEncoder
        // 3. MyLongToByteEncoder 父类是 MessageToByteEncoder
        // 4. 父类 MessageToByteEncoder 中 acceptOutboundMessage(msg) 会判断当前 msg 是不是应该处理的类型，如果是就处理，不是就跳过
        // 5. 编写 Encoder 时要注意传入的数据类型和处理的数据类型需要一致
//        ctx.writeAndFlush(Unpooled.copiedBuffer("abcdabcdabcdabcd", CharsetUtil.UTF_8));
    }
}
