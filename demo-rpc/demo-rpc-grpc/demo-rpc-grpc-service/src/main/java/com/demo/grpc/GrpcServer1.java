package com.demo.grpc;

import com.demo.grpc.server.HelloServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

/**
 * @author juven
 * @date 2025/9/28 15:01
 * @description
 */
public class GrpcServer1 {
    public static void main(String[] args) throws InterruptedException, IOException {
        //1 绑定端口
        final ServerBuilder<?> serverBuilder = ServerBuilder.forPort(9000);
        //2 发布服务
        serverBuilder.addService(new HelloServiceImpl());
        //serverBuilder.addService(new HelloServiceImpl()); //可以这样发布多个服务，也可以通过addServices（）方法
        //3 创建服务对象
        final Server server = serverBuilder.build();
        server.start();
        server.awaitTermination();
    }
}
