package com.demo.grpc.server;

import com.demo.grpc.api.HelloProto;
import com.demo.grpc.api.HelloServiceGrpc;
import com.google.protobuf.ProtocolStringList;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;

/**
 * @author juven
 * @date 2025/9/28 14:24
 * @description
 */
public class HelloServiceImpl extends HelloServiceGrpc.HelloServiceImplBase {
    /**
     * 通过观察者模式返回响应数据。因为grpc其他编程方式可能通过流多次的传输响应数据，通过返回值的方式显然不合适。
     *
     * @param request
     * @param responseObserver 响应数据观察者
     */
    @Override
    public void hello(HelloProto.HelloRequest request, StreamObserver<HelloProto.HelloResponse> responseObserver) {
        //1 接受client的请求参数
        final String name = request.getName();
        //2 业务处理
        System.out.println("服务端接受到参数：" + name);
        //3 封装响应
        //3.1 创建响应对象的构造者
        final HelloProto.HelloResponse.Builder builder = HelloProto.HelloResponse.newBuilder();
        //3.2 填充数据
        builder.setResult("服务端响应" + name);
        //3.3 封装响应
        final HelloProto.HelloResponse helloResponse = builder.build();

        responseObserver.onNext(helloResponse);
        responseObserver.onCompleted();
    }

    /**
     * 传输多个参数
     *
     * @param request          请求参数里面又多个参数形成的list
     * @param responseObserver
     */
    @Override
    public void helloList(HelloProto.HelloRequestList request, StreamObserver<HelloProto.HelloResponseList> responseObserver) {
        //对应repeated生成的list
        final ProtocolStringList nameList = request.getNameList();
        for (String name : nameList) {
            System.out.println("参数：" + name);
        }
        final HelloProto.HelloResponseList.Builder builder = HelloProto.HelloResponseList.newBuilder();
        builder.setResult("hello list result");
        final HelloProto.HelloResponseList build = builder.build();

        responseObserver.onNext(build);
        responseObserver.onCompleted();
    }

    /**
     * 服务端流式通信
     * 这里是当服务端传输完所有响应之后，客户端才收到onCompleted的信号显示响应信息
     *
     * @param request
     * @param responseObserver
     */
    @Override
    public void c2ss(HelloProto.HelloRequest request, StreamObserver<HelloProto.HelloResponse> responseObserver) {
        //1 获取请求参数
        final String name = request.getName();
        //2 业务处理
        System.out.println("服务端流式处理获取的参数：" + name);
        //3 根据业务处理，提供响应
        for (int i = 0; i < 10; i++) {
            final HelloProto.HelloResponse.Builder builder = HelloProto.HelloResponse.newBuilder();
            builder.setResult("服务端流式处理响应：" + i);
            final HelloProto.HelloResponse build = builder.build();

            responseObserver.onNext(build);
            //模拟不定时获取响应
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        responseObserver.onCompleted();
    }

    /**
     * 客户端流式通信.注意客户端只接收一个响应（响应没有用stream修饰），所以响应不能在onNext方法中返回(会导致有多个返回)。
     * 否则服务端会报io.grpc.StatusRuntimeException: INTERNAL: Too many responses
     *
     * @param responseObserver
     * @return
     */
    @Override
    public StreamObserver<HelloProto.HelloRequest> cs2s(StreamObserver<HelloProto.HelloResponse> responseObserver) {
        return new StreamObserver<HelloProto.HelloRequest>() {
            final ArrayList<String> list = new ArrayList<>();
            @Override
            public void onNext(HelloProto.HelloRequest helloRequest) {
                System.out.println("接收到客户端流式通信参数：" + helloRequest.getName());
                list.add(helloRequest.getName());
            }

            @Override
            public void onError(Throwable throwable) {
            }

            @Override
            public void onCompleted() {
                HelloProto.HelloResponse.Builder builder = HelloProto.HelloResponse.newBuilder();
                builder.setResult("收到" + list.size());
                HelloProto.HelloResponse helloResponse = builder.build();

                responseObserver.onNext(helloResponse);
                responseObserver.onCompleted();
                System.out.println("接收完客户端流式通信参数个数：" + list.size());
            }
        };
    }

    /**
     * 双端流RPC通信
     * @param responseObserver
     * @return
     */
    @Override
    public StreamObserver<HelloProto.HelloRequest> cs2ss(StreamObserver<HelloProto.HelloResponse> responseObserver) {
        return new StreamObserver<HelloProto.HelloRequest>() {
            final ArrayList<String> list = new ArrayList<>();
            @Override
            public void onNext(HelloProto.HelloRequest helloRequest) {
                System.out.println("接收到客户端流式通信参数：" + helloRequest.getName());
                list.add(helloRequest.getName());

                HelloProto.HelloResponse.Builder builder = HelloProto.HelloResponse.newBuilder();
                builder.setResult("收到" + list.size());
                HelloProto.HelloResponse helloResponse = builder.build();
                responseObserver.onNext(helloResponse);
            }

            @Override
            public void onError(Throwable throwable) {
            }

            @Override
            public void onCompleted() {
                System.out.println("客户端流式通信参数个数， 服务端响应结束：" + list.size());
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public void futureTest(HelloProto.HelloRequest request, StreamObserver<HelloProto.HelloResponse> responseObserver) {
        final String name = request.getName();
        System.out.println("future参数：" + name);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        responseObserver.onNext(HelloProto.HelloResponse.newBuilder().setResult("future响应").build());
        responseObserver.onCompleted();
    }
}
