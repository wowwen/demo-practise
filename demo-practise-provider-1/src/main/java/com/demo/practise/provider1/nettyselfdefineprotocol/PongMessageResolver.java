package com.demo.practise.provider1.nettyselfdefineprotocol;

//pong消息处理器
public class PongMessageResolver implements Resolver{


    @Override
    public boolean support(Message message) {
        return message.getMessageType() == MessageTypeEnum.PONG;
    }

    @Override
    public Message resolve(Message message) {
        //接收到pong消息后，不需要处理，直接返回一个空的message
        System.out.println("收到pong消息：" + System.currentTimeMillis());
        Message empty = new Message();
        empty.setMessageType(MessageTypeEnum.EMPTY);
        return empty;
    }
}
