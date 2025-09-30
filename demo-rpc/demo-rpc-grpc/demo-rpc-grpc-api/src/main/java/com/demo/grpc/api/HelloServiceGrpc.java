package com.demo.grpc.api;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * The greeting service definition.
 * </pre>
 */
@io.grpc.stub.annotations.GrpcGenerated
public final class HelloServiceGrpc {

  private HelloServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "HelloService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.demo.grpc.api.HelloProto.HelloRequest,
      com.demo.grpc.api.HelloProto.HelloResponse> getHelloMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "hello",
      requestType = com.demo.grpc.api.HelloProto.HelloRequest.class,
      responseType = com.demo.grpc.api.HelloProto.HelloResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.demo.grpc.api.HelloProto.HelloRequest,
      com.demo.grpc.api.HelloProto.HelloResponse> getHelloMethod() {
    io.grpc.MethodDescriptor<com.demo.grpc.api.HelloProto.HelloRequest, com.demo.grpc.api.HelloProto.HelloResponse> getHelloMethod;
    if ((getHelloMethod = HelloServiceGrpc.getHelloMethod) == null) {
      synchronized (HelloServiceGrpc.class) {
        if ((getHelloMethod = HelloServiceGrpc.getHelloMethod) == null) {
          HelloServiceGrpc.getHelloMethod = getHelloMethod =
              io.grpc.MethodDescriptor.<com.demo.grpc.api.HelloProto.HelloRequest, com.demo.grpc.api.HelloProto.HelloResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "hello"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.demo.grpc.api.HelloProto.HelloRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.demo.grpc.api.HelloProto.HelloResponse.getDefaultInstance()))
              .setSchemaDescriptor(new HelloServiceMethodDescriptorSupplier("hello"))
              .build();
        }
      }
    }
    return getHelloMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.demo.grpc.api.HelloProto.HelloRequestList,
      com.demo.grpc.api.HelloProto.HelloResponseList> getHelloListMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "helloList",
      requestType = com.demo.grpc.api.HelloProto.HelloRequestList.class,
      responseType = com.demo.grpc.api.HelloProto.HelloResponseList.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.demo.grpc.api.HelloProto.HelloRequestList,
      com.demo.grpc.api.HelloProto.HelloResponseList> getHelloListMethod() {
    io.grpc.MethodDescriptor<com.demo.grpc.api.HelloProto.HelloRequestList, com.demo.grpc.api.HelloProto.HelloResponseList> getHelloListMethod;
    if ((getHelloListMethod = HelloServiceGrpc.getHelloListMethod) == null) {
      synchronized (HelloServiceGrpc.class) {
        if ((getHelloListMethod = HelloServiceGrpc.getHelloListMethod) == null) {
          HelloServiceGrpc.getHelloListMethod = getHelloListMethod =
              io.grpc.MethodDescriptor.<com.demo.grpc.api.HelloProto.HelloRequestList, com.demo.grpc.api.HelloProto.HelloResponseList>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "helloList"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.demo.grpc.api.HelloProto.HelloRequestList.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.demo.grpc.api.HelloProto.HelloResponseList.getDefaultInstance()))
              .setSchemaDescriptor(new HelloServiceMethodDescriptorSupplier("helloList"))
              .build();
        }
      }
    }
    return getHelloListMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.demo.grpc.api.HelloProto.HelloRequest,
      com.demo.grpc.api.HelloProto.HelloResponse> getC2ssMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "c2ss",
      requestType = com.demo.grpc.api.HelloProto.HelloRequest.class,
      responseType = com.demo.grpc.api.HelloProto.HelloResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<com.demo.grpc.api.HelloProto.HelloRequest,
      com.demo.grpc.api.HelloProto.HelloResponse> getC2ssMethod() {
    io.grpc.MethodDescriptor<com.demo.grpc.api.HelloProto.HelloRequest, com.demo.grpc.api.HelloProto.HelloResponse> getC2ssMethod;
    if ((getC2ssMethod = HelloServiceGrpc.getC2ssMethod) == null) {
      synchronized (HelloServiceGrpc.class) {
        if ((getC2ssMethod = HelloServiceGrpc.getC2ssMethod) == null) {
          HelloServiceGrpc.getC2ssMethod = getC2ssMethod =
              io.grpc.MethodDescriptor.<com.demo.grpc.api.HelloProto.HelloRequest, com.demo.grpc.api.HelloProto.HelloResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "c2ss"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.demo.grpc.api.HelloProto.HelloRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.demo.grpc.api.HelloProto.HelloResponse.getDefaultInstance()))
              .setSchemaDescriptor(new HelloServiceMethodDescriptorSupplier("c2ss"))
              .build();
        }
      }
    }
    return getC2ssMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.demo.grpc.api.HelloProto.HelloRequest,
      com.demo.grpc.api.HelloProto.HelloResponse> getCs2sMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "cs2s",
      requestType = com.demo.grpc.api.HelloProto.HelloRequest.class,
      responseType = com.demo.grpc.api.HelloProto.HelloResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.CLIENT_STREAMING)
  public static io.grpc.MethodDescriptor<com.demo.grpc.api.HelloProto.HelloRequest,
      com.demo.grpc.api.HelloProto.HelloResponse> getCs2sMethod() {
    io.grpc.MethodDescriptor<com.demo.grpc.api.HelloProto.HelloRequest, com.demo.grpc.api.HelloProto.HelloResponse> getCs2sMethod;
    if ((getCs2sMethod = HelloServiceGrpc.getCs2sMethod) == null) {
      synchronized (HelloServiceGrpc.class) {
        if ((getCs2sMethod = HelloServiceGrpc.getCs2sMethod) == null) {
          HelloServiceGrpc.getCs2sMethod = getCs2sMethod =
              io.grpc.MethodDescriptor.<com.demo.grpc.api.HelloProto.HelloRequest, com.demo.grpc.api.HelloProto.HelloResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.CLIENT_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "cs2s"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.demo.grpc.api.HelloProto.HelloRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.demo.grpc.api.HelloProto.HelloResponse.getDefaultInstance()))
              .setSchemaDescriptor(new HelloServiceMethodDescriptorSupplier("cs2s"))
              .build();
        }
      }
    }
    return getCs2sMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.demo.grpc.api.HelloProto.HelloRequest,
      com.demo.grpc.api.HelloProto.HelloResponse> getCs2ssMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "cs2ss",
      requestType = com.demo.grpc.api.HelloProto.HelloRequest.class,
      responseType = com.demo.grpc.api.HelloProto.HelloResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
  public static io.grpc.MethodDescriptor<com.demo.grpc.api.HelloProto.HelloRequest,
      com.demo.grpc.api.HelloProto.HelloResponse> getCs2ssMethod() {
    io.grpc.MethodDescriptor<com.demo.grpc.api.HelloProto.HelloRequest, com.demo.grpc.api.HelloProto.HelloResponse> getCs2ssMethod;
    if ((getCs2ssMethod = HelloServiceGrpc.getCs2ssMethod) == null) {
      synchronized (HelloServiceGrpc.class) {
        if ((getCs2ssMethod = HelloServiceGrpc.getCs2ssMethod) == null) {
          HelloServiceGrpc.getCs2ssMethod = getCs2ssMethod =
              io.grpc.MethodDescriptor.<com.demo.grpc.api.HelloProto.HelloRequest, com.demo.grpc.api.HelloProto.HelloResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "cs2ss"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.demo.grpc.api.HelloProto.HelloRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.demo.grpc.api.HelloProto.HelloResponse.getDefaultInstance()))
              .setSchemaDescriptor(new HelloServiceMethodDescriptorSupplier("cs2ss"))
              .build();
        }
      }
    }
    return getCs2ssMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.demo.grpc.api.HelloProto.HelloRequest,
      com.demo.grpc.api.HelloProto.HelloResponse> getFutureTestMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "futureTest",
      requestType = com.demo.grpc.api.HelloProto.HelloRequest.class,
      responseType = com.demo.grpc.api.HelloProto.HelloResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.demo.grpc.api.HelloProto.HelloRequest,
      com.demo.grpc.api.HelloProto.HelloResponse> getFutureTestMethod() {
    io.grpc.MethodDescriptor<com.demo.grpc.api.HelloProto.HelloRequest, com.demo.grpc.api.HelloProto.HelloResponse> getFutureTestMethod;
    if ((getFutureTestMethod = HelloServiceGrpc.getFutureTestMethod) == null) {
      synchronized (HelloServiceGrpc.class) {
        if ((getFutureTestMethod = HelloServiceGrpc.getFutureTestMethod) == null) {
          HelloServiceGrpc.getFutureTestMethod = getFutureTestMethod =
              io.grpc.MethodDescriptor.<com.demo.grpc.api.HelloProto.HelloRequest, com.demo.grpc.api.HelloProto.HelloResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "futureTest"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.demo.grpc.api.HelloProto.HelloRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.demo.grpc.api.HelloProto.HelloResponse.getDefaultInstance()))
              .setSchemaDescriptor(new HelloServiceMethodDescriptorSupplier("futureTest"))
              .build();
        }
      }
    }
    return getFutureTestMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static HelloServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<HelloServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<HelloServiceStub>() {
        @java.lang.Override
        public HelloServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new HelloServiceStub(channel, callOptions);
        }
      };
    return HelloServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports all types of calls on the service
   */
  public static HelloServiceBlockingV2Stub newBlockingV2Stub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<HelloServiceBlockingV2Stub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<HelloServiceBlockingV2Stub>() {
        @java.lang.Override
        public HelloServiceBlockingV2Stub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new HelloServiceBlockingV2Stub(channel, callOptions);
        }
      };
    return HelloServiceBlockingV2Stub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static HelloServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<HelloServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<HelloServiceBlockingStub>() {
        @java.lang.Override
        public HelloServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new HelloServiceBlockingStub(channel, callOptions);
        }
      };
    return HelloServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static HelloServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<HelloServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<HelloServiceFutureStub>() {
        @java.lang.Override
        public HelloServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new HelloServiceFutureStub(channel, callOptions);
        }
      };
    return HelloServiceFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   * The greeting service definition.
   * </pre>
   */
  public interface AsyncService {

    /**
     * <pre>
     * Sends a greeting
     * </pre>
     */
    default void hello(com.demo.grpc.api.HelloProto.HelloRequest request,
        io.grpc.stub.StreamObserver<com.demo.grpc.api.HelloProto.HelloResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getHelloMethod(), responseObserver);
    }

    /**
     * <pre>
     *传递多个请求参数List
     * </pre>
     */
    default void helloList(com.demo.grpc.api.HelloProto.HelloRequestList request,
        io.grpc.stub.StreamObserver<com.demo.grpc.api.HelloProto.HelloResponseList> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getHelloListMethod(), responseObserver);
    }

    /**
     * <pre>
     *服务端流式rpc（服务端不定时返回多个响应消息）
     * </pre>
     */
    default void c2ss(com.demo.grpc.api.HelloProto.HelloRequest request,
        io.grpc.stub.StreamObserver<com.demo.grpc.api.HelloProto.HelloResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getC2ssMethod(), responseObserver);
    }

    /**
     * <pre>
     *客户端流式RPC（客户端不定时发送request，服务端响应一个响应）
     * </pre>
     */
    default io.grpc.stub.StreamObserver<com.demo.grpc.api.HelloProto.HelloRequest> cs2s(
        io.grpc.stub.StreamObserver<com.demo.grpc.api.HelloProto.HelloResponse> responseObserver) {
      return io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall(getCs2sMethod(), responseObserver);
    }

    /**
     * <pre>
     *双端流式RPC（客户端给服务端发多个message，服务端也给客户端响应多个message）
     * </pre>
     */
    default io.grpc.stub.StreamObserver<com.demo.grpc.api.HelloProto.HelloRequest> cs2ss(
        io.grpc.stub.StreamObserver<com.demo.grpc.api.HelloProto.HelloResponse> responseObserver) {
      return io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall(getCs2ssMethod(), responseObserver);
    }

    /**
     * <pre>
     *只能用于一元RPC得FutureStub
     * </pre>
     */
    default void futureTest(com.demo.grpc.api.HelloProto.HelloRequest request,
        io.grpc.stub.StreamObserver<com.demo.grpc.api.HelloProto.HelloResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getFutureTestMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service HelloService.
   * <pre>
   * The greeting service definition.
   * </pre>
   */
  public static abstract class HelloServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return HelloServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service HelloService.
   * <pre>
   * The greeting service definition.
   * </pre>
   */
  public static final class HelloServiceStub
      extends io.grpc.stub.AbstractAsyncStub<HelloServiceStub> {
    private HelloServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected HelloServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new HelloServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     * Sends a greeting
     * </pre>
     */
    public void hello(com.demo.grpc.api.HelloProto.HelloRequest request,
        io.grpc.stub.StreamObserver<com.demo.grpc.api.HelloProto.HelloResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getHelloMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     *传递多个请求参数List
     * </pre>
     */
    public void helloList(com.demo.grpc.api.HelloProto.HelloRequestList request,
        io.grpc.stub.StreamObserver<com.demo.grpc.api.HelloProto.HelloResponseList> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getHelloListMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     *服务端流式rpc（服务端不定时返回多个响应消息）
     * </pre>
     */
    public void c2ss(com.demo.grpc.api.HelloProto.HelloRequest request,
        io.grpc.stub.StreamObserver<com.demo.grpc.api.HelloProto.HelloResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncServerStreamingCall(
          getChannel().newCall(getC2ssMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     *客户端流式RPC（客户端不定时发送request，服务端响应一个响应）
     * </pre>
     */
    public io.grpc.stub.StreamObserver<com.demo.grpc.api.HelloProto.HelloRequest> cs2s(
        io.grpc.stub.StreamObserver<com.demo.grpc.api.HelloProto.HelloResponse> responseObserver) {
      return io.grpc.stub.ClientCalls.asyncClientStreamingCall(
          getChannel().newCall(getCs2sMethod(), getCallOptions()), responseObserver);
    }

    /**
     * <pre>
     *双端流式RPC（客户端给服务端发多个message，服务端也给客户端响应多个message）
     * </pre>
     */
    public io.grpc.stub.StreamObserver<com.demo.grpc.api.HelloProto.HelloRequest> cs2ss(
        io.grpc.stub.StreamObserver<com.demo.grpc.api.HelloProto.HelloResponse> responseObserver) {
      return io.grpc.stub.ClientCalls.asyncBidiStreamingCall(
          getChannel().newCall(getCs2ssMethod(), getCallOptions()), responseObserver);
    }

    /**
     * <pre>
     *只能用于一元RPC得FutureStub
     * </pre>
     */
    public void futureTest(com.demo.grpc.api.HelloProto.HelloRequest request,
        io.grpc.stub.StreamObserver<com.demo.grpc.api.HelloProto.HelloResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getFutureTestMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service HelloService.
   * <pre>
   * The greeting service definition.
   * </pre>
   */
  public static final class HelloServiceBlockingV2Stub
      extends io.grpc.stub.AbstractBlockingStub<HelloServiceBlockingV2Stub> {
    private HelloServiceBlockingV2Stub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected HelloServiceBlockingV2Stub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new HelloServiceBlockingV2Stub(channel, callOptions);
    }

    /**
     * <pre>
     * Sends a greeting
     * </pre>
     */
    public com.demo.grpc.api.HelloProto.HelloResponse hello(com.demo.grpc.api.HelloProto.HelloRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getHelloMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     *传递多个请求参数List
     * </pre>
     */
    public com.demo.grpc.api.HelloProto.HelloResponseList helloList(com.demo.grpc.api.HelloProto.HelloRequestList request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getHelloListMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     *服务端流式rpc（服务端不定时返回多个响应消息）
     * </pre>
     */
    @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/10918")
    public io.grpc.stub.BlockingClientCall<?, com.demo.grpc.api.HelloProto.HelloResponse>
        c2ss(com.demo.grpc.api.HelloProto.HelloRequest request) {
      return io.grpc.stub.ClientCalls.blockingV2ServerStreamingCall(
          getChannel(), getC2ssMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     *客户端流式RPC（客户端不定时发送request，服务端响应一个响应）
     * </pre>
     */
    @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/10918")
    public io.grpc.stub.BlockingClientCall<com.demo.grpc.api.HelloProto.HelloRequest, com.demo.grpc.api.HelloProto.HelloResponse>
        cs2s() {
      return io.grpc.stub.ClientCalls.blockingClientStreamingCall(
          getChannel(), getCs2sMethod(), getCallOptions());
    }

    /**
     * <pre>
     *双端流式RPC（客户端给服务端发多个message，服务端也给客户端响应多个message）
     * </pre>
     */
    @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/10918")
    public io.grpc.stub.BlockingClientCall<com.demo.grpc.api.HelloProto.HelloRequest, com.demo.grpc.api.HelloProto.HelloResponse>
        cs2ss() {
      return io.grpc.stub.ClientCalls.blockingBidiStreamingCall(
          getChannel(), getCs2ssMethod(), getCallOptions());
    }

    /**
     * <pre>
     *只能用于一元RPC得FutureStub
     * </pre>
     */
    public com.demo.grpc.api.HelloProto.HelloResponse futureTest(com.demo.grpc.api.HelloProto.HelloRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getFutureTestMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do limited synchronous rpc calls to service HelloService.
   * <pre>
   * The greeting service definition.
   * </pre>
   */
  public static final class HelloServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<HelloServiceBlockingStub> {
    private HelloServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected HelloServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new HelloServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * Sends a greeting
     * </pre>
     */
    public com.demo.grpc.api.HelloProto.HelloResponse hello(com.demo.grpc.api.HelloProto.HelloRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getHelloMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     *传递多个请求参数List
     * </pre>
     */
    public com.demo.grpc.api.HelloProto.HelloResponseList helloList(com.demo.grpc.api.HelloProto.HelloRequestList request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getHelloListMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     *服务端流式rpc（服务端不定时返回多个响应消息）
     * </pre>
     */
    public java.util.Iterator<com.demo.grpc.api.HelloProto.HelloResponse> c2ss(
        com.demo.grpc.api.HelloProto.HelloRequest request) {
      return io.grpc.stub.ClientCalls.blockingServerStreamingCall(
          getChannel(), getC2ssMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     *只能用于一元RPC得FutureStub
     * </pre>
     */
    public com.demo.grpc.api.HelloProto.HelloResponse futureTest(com.demo.grpc.api.HelloProto.HelloRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getFutureTestMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service HelloService.
   * <pre>
   * The greeting service definition.
   * </pre>
   */
  public static final class HelloServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<HelloServiceFutureStub> {
    private HelloServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected HelloServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new HelloServiceFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * Sends a greeting
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.demo.grpc.api.HelloProto.HelloResponse> hello(
        com.demo.grpc.api.HelloProto.HelloRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getHelloMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     *传递多个请求参数List
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.demo.grpc.api.HelloProto.HelloResponseList> helloList(
        com.demo.grpc.api.HelloProto.HelloRequestList request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getHelloListMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     *只能用于一元RPC得FutureStub
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.demo.grpc.api.HelloProto.HelloResponse> futureTest(
        com.demo.grpc.api.HelloProto.HelloRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getFutureTestMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_HELLO = 0;
  private static final int METHODID_HELLO_LIST = 1;
  private static final int METHODID_C2SS = 2;
  private static final int METHODID_FUTURE_TEST = 3;
  private static final int METHODID_CS2S = 4;
  private static final int METHODID_CS2SS = 5;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_HELLO:
          serviceImpl.hello((com.demo.grpc.api.HelloProto.HelloRequest) request,
              (io.grpc.stub.StreamObserver<com.demo.grpc.api.HelloProto.HelloResponse>) responseObserver);
          break;
        case METHODID_HELLO_LIST:
          serviceImpl.helloList((com.demo.grpc.api.HelloProto.HelloRequestList) request,
              (io.grpc.stub.StreamObserver<com.demo.grpc.api.HelloProto.HelloResponseList>) responseObserver);
          break;
        case METHODID_C2SS:
          serviceImpl.c2ss((com.demo.grpc.api.HelloProto.HelloRequest) request,
              (io.grpc.stub.StreamObserver<com.demo.grpc.api.HelloProto.HelloResponse>) responseObserver);
          break;
        case METHODID_FUTURE_TEST:
          serviceImpl.futureTest((com.demo.grpc.api.HelloProto.HelloRequest) request,
              (io.grpc.stub.StreamObserver<com.demo.grpc.api.HelloProto.HelloResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_CS2S:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.cs2s(
              (io.grpc.stub.StreamObserver<com.demo.grpc.api.HelloProto.HelloResponse>) responseObserver);
        case METHODID_CS2SS:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.cs2ss(
              (io.grpc.stub.StreamObserver<com.demo.grpc.api.HelloProto.HelloResponse>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getHelloMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.demo.grpc.api.HelloProto.HelloRequest,
              com.demo.grpc.api.HelloProto.HelloResponse>(
                service, METHODID_HELLO)))
        .addMethod(
          getHelloListMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.demo.grpc.api.HelloProto.HelloRequestList,
              com.demo.grpc.api.HelloProto.HelloResponseList>(
                service, METHODID_HELLO_LIST)))
        .addMethod(
          getC2ssMethod(),
          io.grpc.stub.ServerCalls.asyncServerStreamingCall(
            new MethodHandlers<
              com.demo.grpc.api.HelloProto.HelloRequest,
              com.demo.grpc.api.HelloProto.HelloResponse>(
                service, METHODID_C2SS)))
        .addMethod(
          getCs2sMethod(),
          io.grpc.stub.ServerCalls.asyncClientStreamingCall(
            new MethodHandlers<
              com.demo.grpc.api.HelloProto.HelloRequest,
              com.demo.grpc.api.HelloProto.HelloResponse>(
                service, METHODID_CS2S)))
        .addMethod(
          getCs2ssMethod(),
          io.grpc.stub.ServerCalls.asyncBidiStreamingCall(
            new MethodHandlers<
              com.demo.grpc.api.HelloProto.HelloRequest,
              com.demo.grpc.api.HelloProto.HelloResponse>(
                service, METHODID_CS2SS)))
        .addMethod(
          getFutureTestMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.demo.grpc.api.HelloProto.HelloRequest,
              com.demo.grpc.api.HelloProto.HelloResponse>(
                service, METHODID_FUTURE_TEST)))
        .build();
  }

  private static abstract class HelloServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    HelloServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.demo.grpc.api.HelloProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("HelloService");
    }
  }

  private static final class HelloServiceFileDescriptorSupplier
      extends HelloServiceBaseDescriptorSupplier {
    HelloServiceFileDescriptorSupplier() {}
  }

  private static final class HelloServiceMethodDescriptorSupplier
      extends HelloServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    HelloServiceMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (HelloServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new HelloServiceFileDescriptorSupplier())
              .addMethod(getHelloMethod())
              .addMethod(getHelloListMethod())
              .addMethod(getC2ssMethod())
              .addMethod(getCs2sMethod())
              .addMethod(getCs2ssMethod())
              .addMethod(getFutureTestMethod())
              .build();
        }
      }
    }
    return result;
  }
}
