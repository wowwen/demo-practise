package com.demo.designmodel.mediator;

/**
 * @author juven
 * @date 2025/5/28 0:35
 * @description
 */
public class User {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User(String name){
        this.name  = name;
    }

    public void sendMessage(String message){
        ChatRoom.showMessage(this,message);
    }
}

