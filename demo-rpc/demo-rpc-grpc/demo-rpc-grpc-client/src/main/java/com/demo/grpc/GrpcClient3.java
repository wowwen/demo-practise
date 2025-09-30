package com.demo.grpc;

import com.demo.grpc.api.HelloProto;
import com.demo.grpc.api.HelloServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.Iterator;

/**
 * @author juven
 * @date 2025/9/28 15:52
 * @description 服务端流式通信的客户端代码
 */
public class GrpcClient3 {
    public static void main(String[] args) {
        //1 创建通信的管道
        final ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 9000).usePlaintext().build();
        try {
            //2 获取代理对象stub
            final HelloServiceGrpc.HelloServiceBlockingStub blockingStub = HelloServiceGrpc.newBlockingStub(managedChannel);
            //3 完成RPC调用
            final HelloProto.HelloRequest.Builder builder = HelloProto.HelloRequest.newBuilder();
            builder.setName("服务端流式通信");
            final HelloProto.HelloRequest helloRequest = builder.build();

            final Iterator<HelloProto.HelloResponse> responseIterator = blockingStub.c2ss(helloRequest);
            while (responseIterator.hasNext()){
                final HelloProto.HelloResponse helloResponse = responseIterator.next();
                System.out.println("服务端流式通信响应：" + helloResponse.getResult());
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //防止异常，最后关闭通道
            managedChannel.shutdown();
        }

    }
}
