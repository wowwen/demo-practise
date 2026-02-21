package com.demo.netty.learn.groupchat.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author owen
 * @date 2025/3/30 0:58
 * @description
 */
public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {
    // 定义一个 channel 组，管理所有的 channel。意思是所有用户的channel需要记录起来（每一个用户有一个channel），然后，用户的信息需要群发给其他用户，则需要其他用户的channel
    // GlobalEventExecutor.INSTANCE 是全局的事件执行器，是一个单例.
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // handlerAdded 表示连接建立，一旦连接，第一个被执行
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        // 将当前 channel 加入到 channelGroup
        Channel channel = ctx.channel();
        // 将该客户加入聊天的信息推送到给其他在线的客户端
        // 该方法会将 channelGroup 中所有的 channel 遍历，并发送消息，不需要自己遍历
        channelGroup.writeAndFlush("【客户端】" + channel.remoteAddress() + "加入聊天 " + simpleDateFormat.format(new Date()) + "\n");
        channelGroup.add(channel);
    }

    // 断开连接，将 xx客户离开信息推送给当前在线的客户
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("【客户端】" + channel.remoteAddress() + "退出了群聊~~~\n");
        System.out.println("channelGroup size: " + channelGroup.size());
    }

    // 表示 channel 处于活动状态，提示 xx上线
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " 上线了~~~~");
    }

    // 表示 channel 处于非活动状态，提示 XX离线
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " 离线了~~~~");
    }

    // 读取数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        // 获取到当前 channel
        Channel channel = ctx.channel();
        // 这是遍历 channelGroup，根据不同的情况，回送不同的消息
        channelGroup.forEach(ch -> {
            if (channel != ch) {
                // 不是当前的 channel，直接转发消息
                ch.writeAndFlush("【客户】" + channel.remoteAddress() + " 发送了消息：" + msg + "\n");
            } else {
                ch.writeAndFlush("【自己】发送了消息：" + msg + "\n");
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 关闭通道
        ctx.close();
    }
}
