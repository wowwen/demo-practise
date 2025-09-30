package com.demo.grpc.boot.server.impl;

import com.demo.grpc.boot.api.HelloProto;
import com.demo.grpc.boot.api.HelloServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

/**
 * @author juven
 * @date 2025/9/30 12:12
 * @description
 */
@GrpcService
public class HelloServiceImpl extends HelloServiceGrpc.HelloServiceImplBase {
    @Override
    public void hello(HelloProto.HelloRequest request, StreamObserver<HelloProto.HelloResponse> responseObserver) {
        final String name = request.getName();
        System.out.println("名称：" + name);

        responseObserver.onNext(HelloProto.HelloResponse.newBuilder().setResult("名称响应").build());
        responseObserver.onCompleted();
    }
}
