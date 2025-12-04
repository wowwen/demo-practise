package com.demo.netty.proxy.handler;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import com.demo.netty.proxy.constant.Constants;
import com.demo.netty.proxy.entity.ClientMsg;
import com.demo.netty.proxy.model.ServiceAccount;
import com.demo.netty.proxy.service.AccountService;
import com.demo.netty.proxy.service.ClientChannelManager;
import com.demo.netty.proxy.service.DisconnectService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * @author owen
 * @date 2024/9/24 1:00
 * @description 登录信息处理器
 */
public class LoginServerHandler extends SimpleChannelInboundHandler<ClientMsg> {

    private AccountService accountService;

    private static final String KEY = "7ae99675a9a76d7199fc2180d41b5b74";
    /**
     * 声明为静态变量，并用unreleasableBuffer包装防止误释放，适合长期存在的对象.提高性能
     * Unpooled.unreleasableBuffer()包装，防止缓冲区被意外释放
     * 直接缓冲区(Direct Buffer)分配在堆外内存，可以减少一次内存拷贝，适合I/O操作
     * 直接缓冲区虽然性能更好，但分配和释放成本较高，因此适合长期存在的静态缓冲区。
     */
    private static final ByteBuf RESULT_FAIL = Unpooled.unreleasableBuffer(Unpooled.directBuffer().writeBytes(("HTTP" +
            "/1.1 401 Unauthorized, No or wrong authorization\r\n\r\n").getBytes( CharsetUtil.UTF_8)));
    private static final ByteBuf RESULT_OK = Unpooled.unreleasableBuffer(Unpooled.directBuffer().writeBytes(
            "ICY 200 OK\r\n".getBytes( CharsetUtil.UTF_8)));

    static AES aes = SecureUtil.aes(KEY.getBytes());

    public LoginServerHandler(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ClientMsg msg) throws Exception {
        Channel userChannel = ctx.channel();
        //非登录请求，传播给下一个
        if (!msg.isLoginAuth()) {
            ctx.fireChannelRead(msg);
            return;
        }
        //下面的逻辑是登录请求
        String username = msg.getUsername();
        ServiceAccount serviceAccount = accountService.selectByName(username);

        if (!(StrUtil.isNotEmpty(username)
                && ObjectUtil.isNotNull(serviceAccount)
                && StrUtil.isNotEmpty(serviceAccount.getPassword())
                && (msg.getPassword().equals(aes.decryptStr(serviceAccount.getPassword()))))) {
            //ctx的writeAndFlush从当前的handler往前经过outboundhandler发出
            ctx.writeAndFlush(RESULT_FAIL);
            DisconnectService.closeChannel(ctx);
            return;
        }

        userChannel.attr(Constants.UserChannelAttributeKey.USER_ID).set(serviceAccount.getId());
        userChannel.attr(Constants.UserChannelAttributeKey.USER_NAME).set(username);
        userChannel.attr(Constants.UserChannelAttributeKey.USER_LOGIN_CONTENT).set(msg.getContent());
        userChannel.attr(Constants.UserChannelAttributeKey.LOGIN_TIME).set(msg.getLogintime());
        msg.setChannel(ctx.channel());

        ClientChannelManager.add(username, msg);
        // 返回登录成功
        ctx.writeAndFlush(RESULT_OK);
    }
}
