package java.com.util;

import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.7.0)",
    comments = "Source: TokenMessage.proto")
public final class TokenHandlerGrpc {

  private TokenHandlerGrpc() {}

  public static final String SERVICE_NAME = "TokenHandler";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<java.com.util.TokenMessageOuterClass.TokenMessage,
      java.com.util.TokenMessageOuterClass.TokenResponse> METHOD_SEND_TOKEN =
      io.grpc.MethodDescriptor.<java.com.util.TokenMessageOuterClass.TokenMessage, java.com.util.TokenMessageOuterClass.TokenResponse>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "TokenHandler", "SendToken"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              java.com.util.TokenMessageOuterClass.TokenMessage.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              java.com.util.TokenMessageOuterClass.TokenResponse.getDefaultInstance()))
          .setSchemaDescriptor(new TokenHandlerMethodDescriptorSupplier("SendToken"))
          .build();

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static TokenHandlerStub newStub(io.grpc.Channel channel) {
    return new TokenHandlerStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static TokenHandlerBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new TokenHandlerBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static TokenHandlerFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new TokenHandlerFutureStub(channel);
  }

  /**
   */
  public static abstract class TokenHandlerImplBase implements io.grpc.BindableService {

    /**
     */
    public void sendToken(java.com.util.TokenMessageOuterClass.TokenMessage request,
        io.grpc.stub.StreamObserver<java.com.util.TokenMessageOuterClass.TokenResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_SEND_TOKEN, responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_SEND_TOKEN,
            asyncUnaryCall(
              new MethodHandlers<
                java.com.util.TokenMessageOuterClass.TokenMessage,
                java.com.util.TokenMessageOuterClass.TokenResponse>(
                  this, METHODID_SEND_TOKEN)))
          .build();
    }
  }

  /**
   */
  public static final class TokenHandlerStub extends io.grpc.stub.AbstractStub<TokenHandlerStub> {
    private TokenHandlerStub(io.grpc.Channel channel) {
      super(channel);
    }

    private TokenHandlerStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TokenHandlerStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new TokenHandlerStub(channel, callOptions);
    }

    /**
     */
    public void sendToken(java.com.util.TokenMessageOuterClass.TokenMessage request,
        io.grpc.stub.StreamObserver<java.com.util.TokenMessageOuterClass.TokenResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_SEND_TOKEN, getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class TokenHandlerBlockingStub extends io.grpc.stub.AbstractStub<TokenHandlerBlockingStub> {
    private TokenHandlerBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private TokenHandlerBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TokenHandlerBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new TokenHandlerBlockingStub(channel, callOptions);
    }

    /**
     */
    public java.com.util.TokenMessageOuterClass.TokenResponse sendToken(java.com.util.TokenMessageOuterClass.TokenMessage request) {
      return blockingUnaryCall(
          getChannel(), METHOD_SEND_TOKEN, getCallOptions(), request);
    }
  }

  /**
   */
  public static final class TokenHandlerFutureStub extends io.grpc.stub.AbstractStub<TokenHandlerFutureStub> {
    private TokenHandlerFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private TokenHandlerFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TokenHandlerFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new TokenHandlerFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<java.com.util.TokenMessageOuterClass.TokenResponse> sendToken(
        java.com.util.TokenMessageOuterClass.TokenMessage request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_SEND_TOKEN, getCallOptions()), request);
    }
  }

  private static final int METHODID_SEND_TOKEN = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final TokenHandlerImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(TokenHandlerImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_SEND_TOKEN:
          serviceImpl.sendToken((java.com.util.TokenMessageOuterClass.TokenMessage) request,
              (io.grpc.stub.StreamObserver<java.com.util.TokenMessageOuterClass.TokenResponse>) responseObserver);
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
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class TokenHandlerBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    TokenHandlerBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return java.com.util.TokenMessageOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("TokenHandler");
    }
  }

  private static final class TokenHandlerFileDescriptorSupplier
      extends TokenHandlerBaseDescriptorSupplier {
    TokenHandlerFileDescriptorSupplier() {}
  }

  private static final class TokenHandlerMethodDescriptorSupplier
      extends TokenHandlerBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    TokenHandlerMethodDescriptorSupplier(String methodName) {
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
      synchronized (TokenHandlerGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new TokenHandlerFileDescriptorSupplier())
              .addMethod(METHOD_SEND_TOKEN)
              .build();
        }
      }
    }
    return result;
  }
}
