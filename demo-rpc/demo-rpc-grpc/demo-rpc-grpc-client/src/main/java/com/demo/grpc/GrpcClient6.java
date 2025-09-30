package com.demo.grpc;

import com.demo.grpc.api.HelloProto;
import com.demo.grpc.api.HelloServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.TimeUnit;

/**
 * @author juven
 * @date 2025/9/28 15:52
 * @description 客户端监听，异步方式处理服务端流式RPC的开发
 */
public class GrpcClient6 {
    public static void main(String[] args) {
        //1 创建通信的管道
        final ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 9000).usePlaintext().build();
        try {
            //newStub异步监听的stub
            final HelloServiceGrpc.HelloServiceStub serviceStub = HelloServiceGrpc.newStub(managedChannel);
            final StreamObserver<HelloProto.HelloRequest> requestStreamObserver = serviceStub.cs2ss(new StreamObserver<HelloProto.HelloResponse>() {
                @Override
                public void onNext(HelloProto.HelloResponse helloResponse) {
                    System.out.println("双端--服务端响应：" + helloResponse.getResult());
                }

                @Override
                public void onError(Throwable throwable) {

                }

                @Override
                public void onCompleted() {
                    System.out.println("双端--服务端响应结束");
                }
            });

            //客户端不定时发送数据到服务端
            for (int i = 0; i < 10; i++) {
                requestStreamObserver.onNext(HelloProto.HelloRequest.newBuilder().setName("双流式通信" + i).build());
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
