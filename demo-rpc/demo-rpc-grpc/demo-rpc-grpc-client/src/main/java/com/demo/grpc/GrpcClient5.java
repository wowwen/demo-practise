package com.demo.grpc;

import com.demo.grpc.api.HelloProto;
import com.demo.grpc.api.HelloServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * @author juven
 * @date 2025/9/28 15:52
 * @description 客户端监听，异步方式处理服务端流式RPC的开发
 */
public class GrpcClient5 {
    public static void main(String[] args) {
        //1 创建通信的管道
        final ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 9000).usePlaintext().build();
        try {
            //newStub异步监听的stub
            final HelloServiceGrpc.HelloServiceStub serviceStub = HelloServiceGrpc.newStub(managedChannel);
            StreamObserver<HelloProto.HelloRequest> requestStreamObserver = serviceStub.cs2s(new StreamObserver<HelloProto.HelloResponse>() {
                @Override
                public void onNext(HelloProto.HelloResponse helloResponse) {
                    //监控响应
                    System.out.println("服务端响应：" + helloResponse.getResult());
                }

                @Override
                public void onError(Throwable throwable) {
                }

                @Override
                public void onCompleted() {
                    System.out.println("服务端一个响应结束");
                }
            });

            //客户端不定时发送数据到服务端
            for (int i = 0; i < 10; i++) {
                final HelloProto.HelloRequest.Builder builder = HelloProto.HelloRequest.newBuilder();
                builder.setName("客户端流式通信" + i);
                HelloProto.HelloRequest helloRequest = builder.build();
                requestStreamObserver.onNext(helloRequest);
                Thread.sleep(1000);
            }
            requestStreamObserver.onCompleted();
            //注意：不能少了这句，否则客户端异步处理，服务端还未返回就关闭了通道
            managedChannel.awaitTermination(12, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //防止异常，最后关闭通道
            managedChannel.shutdown();
        }

    }
}
