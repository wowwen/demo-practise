package com.demo.annotation.springDI.primary;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

public interface MessageService {
    String sendMessage();
}

@Component
class EmailMessageServiceImpl implements MessageService {

    @Override
    public String sendMessage() {
        return "send by email";
    }
}

@Component
class WechatMessageImpl implements MessageService {

    @Override
    public String sendMessage() {
        return "send by wechat";
    }
}

@Primary
@Component
class DingDingMessageImpl implements MessageService {

    @Override
    public String sendMessage() {
        return "send by ding ding";
    }
}

@Service("emailService")
class EmailServiceImpl implements MessageService{

    @Override
    public String sendMessage() {

        return "email content";
    }
}

@Service("smsService")
class SMSServiceImpl implements MessageService{

    @Override
    public String sendMessage() {
        return "sms content";
    }
}


