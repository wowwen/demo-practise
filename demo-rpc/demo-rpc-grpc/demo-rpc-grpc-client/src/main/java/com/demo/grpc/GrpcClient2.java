package com.demo.grpc;

import com.demo.grpc.api.HelloProto;
import com.demo.grpc.api.HelloServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

/**
 * @author juven
 * @date 2025/9/28 15:52
 * @description 简单RPC（一元RPC）。传输多个请求参数
 */
public class GrpcClient2 {
    public static void main(String[] args) {
        //1 创建通信的管道
        final ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 9000).usePlaintext().build();
        try {
            //2 获取代理对象stub
            final HelloServiceGrpc.HelloServiceBlockingStub blockingStub = HelloServiceGrpc.newBlockingStub(managedChannel);
            //3 完成RPC调用
            final HelloProto.HelloRequestList.Builder builder = HelloProto.HelloRequestList.newBuilder();
            builder.addName("李四");
            builder.addName("王五");
            final HelloProto.HelloRequestList build = builder.build();
            final HelloProto.HelloResponseList helloResponseList = blockingStub.helloList(build);
            System.out.println("收到响应结果: " + helloResponseList.getResult());
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //防止异常，最后关闭通道
            managedChannel.shutdown();
        }

    }
}
