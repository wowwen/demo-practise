package com.demo.netty.proxy.handler;

import com.demo.netty.proxy.constant.Constants;
import com.demo.netty.proxy.entity.ClientMsg;
import com.demo.netty.proxy.service.ClientChannelManager;
import com.demo.netty.proxy.service.DisconnectService;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author owen
 * @date 2024/9/22 17:48
 * @description 解码客户端上传的信息
 */
public class ClientMsgDecoder extends StringDecoder {
    private static final Logger logger = LoggerFactory.getLogger(ClientMsgDecoder.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf inBuf, List<Object> out) throws Exception {
        AtomicBoolean loginFlag = new AtomicBoolean(false);
        ClientMsg clientMsg = new ClientMsg();
        String msg = inBuf.toString(CharsetUtil.UTF_8);
        //登录请求 设置登录信息
        clientMsg.setContent(msg);
        Date date = new Date(System.currentTimeMillis());
        clientMsg.setLogintime(date.getTime() / 1000);
        String[] fields = msg.split(" ");
        switch (fields[0]) {
            case "Authorization:":
                //Authorization: Basic QzYwMDAwMTp6aGRncHNfMDAx
                //区分是登录请求还是坐标上传请求
                clientMsg.setLoginAuth(true);
                String userNameAndPwd = new String(Base64.getDecoder().decode(fields[2]), CharsetUtil.UTF_8);
                String[] namePwd = userNameAndPwd.split(":");
                clientMsg.setUsername(namePwd[0]);
                clientMsg.setPassword(namePwd[1]);
                logger.info("解密出的用户名：{}, 密码：{}", namePwd[0], namePwd[1]);
                loginFlag.compareAndSet(false, true);
                break;
            default:
                ClientMsg loginMsg =
                        ClientChannelManager.get(ctx.channel().attr(Constants.UserChannelAttributeKey.USER_NAME).get());
                if (!ObjectUtils.isEmpty(loginMsg)) {
                    loginFlag.compareAndSet(false, true);
                    clientMsg.setContent(fields[0]);
                    //转换成坐标，demo此处省略转换逻辑，直接塞进去
                    clientMsg.setPredicates(fields[0]);
                }
                break;
        }
        if (loginFlag.get()) {
            out.add(clientMsg);
        } else {
            logger.info("未登陆,关闭");
            DisconnectService.closeChannel(ctx);
        }
    }
}
