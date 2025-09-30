package com.demo.grpc;

import com.demo.grpc.api.HelloProto;
import com.demo.grpc.api.HelloServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/**
 * @author juven
 * @date 2025/9/28 15:52
 * @description 客户端监听，异步方式处理服务端流式RPC的开发
 */
public class GrpcClient4 {
    public static void main(String[] args) {
        //1 创建通信的管道
        final ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 9000).usePlaintext().build();
        try {
            //newStub异步监听的stub
            final HelloServiceGrpc.HelloServiceStub serviceStub = HelloServiceGrpc.newStub(managedChannel);
            final HelloProto.HelloRequest.Builder builder = HelloProto.HelloRequest.newBuilder();
            builder.setName("服务端流式通信，客户端异步");
            final HelloProto.HelloRequest helloRequest = builder.build();

            serviceStub.c2ss(helloRequest, new StreamObserver<HelloProto.HelloResponse>() {
                final ArrayList<String> list = new ArrayList<>();
                @Override
                public void onNext(HelloProto.HelloResponse helloResponse) {
                    //服务端响应一个消息后，需要立即处理的话，在这个方法中处理
                    final String result = helloResponse.getResult();
                    System.out.println("客户端收到响应：" + result);
                    list.add(result);
                }

                @Override
                public void onError(Throwable throwable) {

                }

                @Override
                public void onCompleted() {
                    //需要把服务端响应的所有数据拿到后，再处理的话，在这个方法中处理
                    System.out.println("响应数据总数：" + list.size());
                }
            });

            //注意：需要防止客户端过早的结束（执行了下面的shutdown方法），导致通道关闭接收不到服务端的响应消息。所以需要等待。
            managedChannel.awaitTermination(12, TimeUnit.SECONDS);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //防止异常，最后关闭通道
            managedChannel.shutdown();
        }

    }
}
