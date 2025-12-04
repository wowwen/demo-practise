package com.demo.netty.proxy.service;

import cn.hutool.core.util.ObjectUtil;
import com.demo.netty.proxy.constant.Constants;
import com.demo.netty.proxy.entity.ClientMsg;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author owen
 * @date 2024/9/23 8:47
 * @description
 */
public class DisconnectService {

    /**
     * 关闭连接
     *
     * @param ctx
     */
    public static void closeChannel(ChannelHandlerContext ctx) {
        String userName = ctx.channel().attr(Constants.UserChannelAttributeKey.USER_NAME).get();
        ClientMsg clientMsg = ClientChannelManager.get(userName);
        ClientChannelManager.remove(userName);
        if (ObjectUtil.isNotNull(clientMsg) && ObjectUtil.isNotNull(clientMsg.getChannel())) {
            clientMsg.getChannel().close();
        }
        ctx.close();
    }
}
