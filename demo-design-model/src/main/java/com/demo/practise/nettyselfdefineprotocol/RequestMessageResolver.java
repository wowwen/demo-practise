package com.demo.practise.nettyselfdefineprotocol;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * request类型的消息
 */
public class RequestMessageResolver implements Resolver {

    private static final AtomicInteger counter = new AtomicInteger(1);
    @Override
    public boolean support(Message message) {
        return message.getMessageType() ==  MessageTypeEnum.REQUEST;
    }

    @Override
    public Message resolve(Message message) {
        //接收到request消息之后，对消息进行处理， 这里主要是将其打印出来
        int index = counter.getAndIncrement();
        System.out.println("[trx:" + message.getSessionId() + "]" + index + ".receive request:" + message.getBody());
        System.out.println("[trx:" + message.getSessionId() + "]" + index + ".attachments:" + message.getAttachments());

        //处理完成后，生成一个响应消息返回
        Message respone = new Message();
        respone.setMessageType(MessageTypeEnum.RESPONSE);
        respone.setBody("nice to meet u");
        respone.addAttachment("name", "张三");
        respone.addAttachment("hometown", "北京");
        return respone;

    }
}
