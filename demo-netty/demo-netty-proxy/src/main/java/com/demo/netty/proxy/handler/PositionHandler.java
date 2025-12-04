package com.demo.netty.proxy.handler;

import cn.hutool.core.util.ObjectUtil;
import com.demo.netty.proxy.constant.Constants;
import com.demo.netty.proxy.entity.ClientMsg;
import com.demo.netty.proxy.service.ClientChannelManager;
import com.demo.netty.proxy.service.DisconnectService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * @author owen
 * @date 2024/9/24 21:33
 * @description
 */
public class PositionHandler extends SimpleChannelInboundHandler<ClientMsg> {
    private static final Logger logger = LoggerFactory.getLogger(PositionHandler.class);
    private String topic;
    private KafkaTemplate kfkProducer;


    public PositionHandler(String topic, KafkaTemplate kfkProducer) {
        this.topic = topic;
        this.kfkProducer = kfkProducer;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ClientMsg msg) {
        Channel clientChannel = ctx.channel();
        Integer userId = clientChannel.attr(Constants.UserChannelAttributeKey.USER_ID).get();
        String content = msg.getContent();

        kfkProducer.send(topic, userId, content);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        Integer userId = ctx.channel().attr(Constants.UserChannelAttributeKey.USER_ID).get();
        logger.error("代理服务异常,用户{}通道关闭,异常原因{}", userId, cause.getMessage());
        DisconnectService.closeChannel(ctx);
        cause.printStackTrace();
    }

    /**
     * 非活动状态触发
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        String username = ctx.channel().attr(Constants.UserChannelAttributeKey.USER_NAME).get();
        Long loginTime = ctx.channel().attr(Constants.UserChannelAttributeKey.LOGIN_TIME)
                .get();
        ClientMsg msg = ClientChannelManager.get(username);

        if (ObjectUtil.isNotNull(msg) && ObjectUtil.isNotNull(msg.getChannel())) {
            String uid = msg.getId();
            Long msgLogintime = msg.getLogintime();
            logger.info("用户：{}，登录时间msgLogintime：{}通道关闭", uid, msgLogintime);
        }
        logger.info("用户：{}, loginTime:{},与真实服务器断开连接", username, loginTime);
        DisconnectService.closeChannel(ctx);
    }
}
