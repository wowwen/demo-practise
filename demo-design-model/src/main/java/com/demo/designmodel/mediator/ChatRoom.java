package com.demo.designmodel.mediator;

import java.util.Date;

/**
 * @author juven
 * @date 2025/5/28 0:34
 * @description 创建中介类
 */
public class ChatRoom {
    public static void showMessage(User user, String message){
        System.out.println(new Date().toString()
                + " [" + user.getName() +"] : " + message);
    }
}