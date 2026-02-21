package com.demo.netty.learn.heartbeat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @author owen
 * @date 2025/3/30 15:21
 * @description 心跳处理器
 */
public class MyServerHandler extends ChannelInboundHandlerAdapter {
    /**
     * 当ChannelInboundHandler.fireUserEventTriggered()方法被调用时被调用，因为一个POJO被传经了ChannelPipleline。
     * @param ctx 上下文
     * @param evt 事件
     * @throws Exception 异常
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            // 将 evt 向下转型
            IdleStateEvent event = (IdleStateEvent) evt;
            String eventType = null;
            switch (event.state()){
                case READER_IDLE:
                    eventType = "读空闲";
                    break;
                case WRITER_IDLE:
                    eventType = "写空闲";
                    break;
                case ALL_IDLE:
                    eventType = "读写空闲";
                    break;
            }

            System.out.println(ctx.channel().remoteAddress() + "--超时事件发生-->" + eventType);
            System.out.println("服务器做相应处理......");
        }
    }
}
