package com.demo.designmodel.mediator;

/**
 * @author juven
 * @date 2025/5/28 0:36
 * @description 使用 User 对象来显示他们之间的通信
 */
public class MediatorPatternDemo {
    public static void main(String[] args) {
        User robert = new User("Robert");
        User john = new User("John");

        robert.sendMessage("Hi! John!");
        john.sendMessage("Hello! Robert!");
    }
}
