package com.demo.netty.learn.dubborpc;

/**
 * @author owen
 * @date 2025/4/2 3:14
 * @description 服务提供者, 这个代码只在服务端有，客户端不存在
 */
public class HelloServiceImpl implements HelloService{
    private static int count = 0;

    // 当有消费方调用该方法时，就返回一个结果
    @Override
    public String hello(String message) {
        System.out.println("收到客户端消息=" + message);
        // 根据 message 返回不同的结果
        if(message != null){
            return "你好，客户端，我已经收到你的消息【" + message + "】第" + (++count) + "次";
        }else{
            return "你好，客户端，我已经收到你的消息";
        }
    }
}
