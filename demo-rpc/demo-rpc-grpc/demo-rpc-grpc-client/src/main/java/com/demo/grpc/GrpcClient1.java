package com.demo.grpc;

import com.demo.grpc.api.HelloProto;
import com.demo.grpc.api.HelloServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

/**
 * @author juven
 * @date 2025/9/28 15:52
 * @description 简单RPC （一元RPC）
 */
public class GrpcClient1 {
    public static void main(String[] args) {
        //1 创建通信的管道
        final ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 9000).usePlaintext().build();
        try {
            //2 获取代理对象stub
            final HelloServiceGrpc.HelloServiceBlockingStub blockingStub = HelloServiceGrpc.newBlockingStub(managedChannel);
            //3 完成RPC调用
            final HelloProto.HelloRequest.Builder builder = HelloProto.HelloRequest.newBuilder();
            builder.setName("张三");
            final HelloProto.HelloRequest helloRequest = builder.build();

            final HelloProto.HelloResponse helloResponse = blockingStub.hello(helloRequest);
            System.out.println("收到响应结果: " + helloResponse.getResult());
        }catch (Exception e){

        }finally {
            //防止异常，最后关闭通道
            managedChannel.shutdown();
        }

    }
}
