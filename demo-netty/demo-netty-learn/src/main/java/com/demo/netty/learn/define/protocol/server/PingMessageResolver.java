package com.demo.netty.learn.define.protocol.server;

import com.demo.netty.learn.define.protocol.Message;
import com.demo.netty.learn.define.protocol.MessageTypeEnum;
import com.demo.netty.learn.define.protocol.Resolver;

//ping消息处理器
public class PingMessageResolver implements Resolver {
    @Override
    public boolean support(Message message) {
        return message.getMessageType() == MessageTypeEnum.PING;
    }

    @Override
    public Message resolve(Message message) {
        //收到一个ping消息，返回一个pong消息
        System.out.println("收到ping消息：" + System.currentTimeMillis());
        Message pong = new Message();
        pong.setMessageType(MessageTypeEnum.PONG);
        return pong;
    }
}
