package com.demo.netty.proxy.handler;

import cn.hutool.core.util.ObjectUtil;
import com.demo.netty.proxy.constant.Constants;
import com.demo.netty.proxy.entity.ClientMsg;
import com.demo.netty.proxy.service.DisconnectService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author owen
 * @date 2024/9/4 22:17
 * @description 心跳的处理器
 */
public class HeartBeatServerHandler extends SimpleChannelInboundHandler<ClientMsg> {
    private final static Logger logger = LoggerFactory.getLogger(HeartBeatServerHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ClientMsg msg) throws Exception {
        ctx.fireChannelRead(msg);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            switch (idleStateEvent.state()) {
                case READER_IDLE:
                case WRITER_IDLE:
                case ALL_IDLE:
                    DisconnectService.closeChannel(ctx);
                    break;
                default:
                    break;
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

}
