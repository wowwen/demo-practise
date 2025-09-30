package com.demo.grpc;

import com.demo.grpc.api.HelloProto;
import com.demo.grpc.api.HelloServiceGrpc;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author juven
 * @date 2025/9/28 15:52
 * @description 客户端监听，异步方式处理服务端流式RPC的开发
 */
public class GrpcClient7 {
    public static void main(String[] args) {
        //1 创建通信的管道
        final ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 9000).usePlaintext().build();
        try {
            final HelloServiceGrpc.HelloServiceFutureStub futureStub = HelloServiceGrpc.newFutureStub(managedChannel);
            final ListenableFuture<HelloProto.HelloResponse> future = futureStub.futureTest(HelloProto.HelloRequest.newBuilder().setName("Future").build());
//            sync(future);

//            async(future);

            asyncDealData(future);

            System.out.println("后续操作");
            //异步操作得话，不能太快关闭通道
            managedChannel.awaitTermination(5, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //防止异常，最后关闭通道
            managedChannel.shutdown();
        }
    }

    private static void sync(ListenableFuture<HelloProto.HelloResponse> future) throws ExecutionException, InterruptedException {
        //同步操作
        final HelloProto.HelloResponse response = future.get();
        System.out.println(response.getResult());
    }

    private static void async(ListenableFuture<HelloProto.HelloResponse> future){
        future.addListener(() -> System.out.println("异步得Future响应"), Executors.newCachedThreadPool());
    }

    private static void asyncDealData(ListenableFuture<HelloProto.HelloResponse> future){
        Futures.addCallback(future, new FutureCallback<HelloProto.HelloResponse>() {
            @Override
            public void onSuccess(@NullableDecl HelloProto.HelloResponse result) {
                System.out.println("future获取到得数据：" + result.getResult());
            }

            @Override
            public void onFailure(Throwable t) {

            }
        }, Executors.newCachedThreadPool());
    }
}
