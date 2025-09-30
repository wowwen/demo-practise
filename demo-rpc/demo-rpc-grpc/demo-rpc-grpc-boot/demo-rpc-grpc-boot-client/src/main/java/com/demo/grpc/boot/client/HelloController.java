package com.demo.grpc.boot.client;

import com.demo.grpc.boot.api.HelloProto;
import com.demo.grpc.boot.api.HelloServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author juven
 * @date 2025/10/1 1:34
 * @description
 */
@RestController
public class HelloController {

    @GrpcClient(value = "grpc-server-1")
    private HelloServiceGrpc.HelloServiceBlockingStub stub;

    @RequestMapping("/hello")
    public String hello(String name){
        System.out.println("参数name：" + name);
        final HelloProto.HelloResponse response = stub.hello(HelloProto.HelloRequest.newBuilder().setName("李逵").build());
        return response.getResult();
    }


}
