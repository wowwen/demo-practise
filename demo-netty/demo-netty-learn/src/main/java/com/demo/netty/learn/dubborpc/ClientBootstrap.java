package com.demo.netty.learn.dubborpc;

/**
 * @author owen
 * @date 2025/4/2 22:03
 * @description
 */
public class ClientBootstrap {
    // 定义协议头
    public static final String providerName = "HelloService#hello#";

    public static void main(String[] args) throws InterruptedException {
        // 创建一个消费者
        NettyClient customer = new NettyClient();
        // 创建代理对象
        HelloService service = (HelloService) customer.getBean(HelloService.class, providerName);

        for (; ; ) {
            Thread.sleep(2 * 1000);

            // 通过代理对象 调用服务提供者的方法
            String res = service.hello("你好  dubbo~~~~");
            System.out.println("调用的结果 res =" + res);
        }
    }

    //调用方式二，下面的说明是针对这段代码的。至于方式一有没有性能问题，还需研究
//    public static void main(String[] args) {
//        //创建一个消费者
//        NettyClient consumer = new NettyClient();
//
//        for (int i=0; i<10; i++) {
//            //创建代理对象
//            HelloService helloService = (HelloService)consumer.getBean(HelloService.class, providerName);
//
//            //通过代理对象调用服务提供者的方法
//            String result = helloService.hello("你好 RPC~");
//            System.out.println("调用结果 result：" + result);
//            System.out.println("-------------------------");
//        }
//
//    }
}

