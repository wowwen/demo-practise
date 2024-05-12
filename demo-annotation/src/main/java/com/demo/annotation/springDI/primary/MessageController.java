package com.demo.annotation.springDI.primary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {
    @Autowired
    private MessageService messageService;

    @Autowired
    @Qualifier("emailService")
    private MessageService emailService;

    private MessageService smsService;

    @Autowired
    @Qualifier("smsService")
    public void setSmsService(MessageService smsService){
        this.smsService = smsService;
    }

    @GetMapping("/info")
    public String info(){
        return messageService.sendMessage();
    }

    @GetMapping("/send")
    public String send(){
        return emailService.sendMessage();
    }

    @GetMapping("/sms")
    public String sms(){
        return smsService.sendMessage();
    }

}
