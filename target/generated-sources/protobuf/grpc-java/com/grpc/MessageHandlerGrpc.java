package com.grpc;

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
public final class MessageHandlerGrpc {

  private MessageHandlerGrpc() {}

  public static final String SERVICE_NAME = "MessageHandler";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.grpc.TokenMessageOuterClass.TokenMessage,
      com.grpc.TokenMessageOuterClass.TokenResponse> METHOD_HANDLE_TOKEN =
      io.grpc.MethodDescriptor.<com.grpc.TokenMessageOuterClass.TokenMessage, com.grpc.TokenMessageOuterClass.TokenResponse>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "MessageHandler", "HandleToken"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.grpc.TokenMessageOuterClass.TokenMessage.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.grpc.TokenMessageOuterClass.TokenResponse.getDefaultInstance()))
          .setSchemaDescriptor(new MessageHandlerMethodDescriptorSupplier("HandleToken"))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.grpc.TokenMessageOuterClass.NewNodeMessage,
      com.grpc.TokenMessageOuterClass.NewNodeResponse> METHOD_ADD_NODE =
      io.grpc.MethodDescriptor.<com.grpc.TokenMessageOuterClass.NewNodeMessage, com.grpc.TokenMessageOuterClass.NewNodeResponse>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "MessageHandler", "AddNode"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.grpc.TokenMessageOuterClass.NewNodeMessage.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.grpc.TokenMessageOuterClass.NewNodeResponse.getDefaultInstance()))
          .setSchemaDescriptor(new MessageHandlerMethodDescriptorSupplier("AddNode"))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.grpc.TokenMessageOuterClass.RemoveNodeMessage,
      com.grpc.TokenMessageOuterClass.RemoveNodeResponse> METHOD_REMOVE_NODE =
      io.grpc.MethodDescriptor.<com.grpc.TokenMessageOuterClass.RemoveNodeMessage, com.grpc.TokenMessageOuterClass.RemoveNodeResponse>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "MessageHandler", "RemoveNode"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.grpc.TokenMessageOuterClass.RemoveNodeMessage.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.grpc.TokenMessageOuterClass.RemoveNodeResponse.getDefaultInstance()))
          .setSchemaDescriptor(new MessageHandlerMethodDescriptorSupplier("RemoveNode"))
          .build();

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static MessageHandlerStub newStub(io.grpc.Channel channel) {
    return new MessageHandlerStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static MessageHandlerBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new MessageHandlerBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static MessageHandlerFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new MessageHandlerFutureStub(channel);
  }

  /**
   */
  public static abstract class MessageHandlerImplBase implements io.grpc.BindableService {

    /**
     */
    public void handleToken(com.grpc.TokenMessageOuterClass.TokenMessage request,
        io.grpc.stub.StreamObserver<com.grpc.TokenMessageOuterClass.TokenResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_HANDLE_TOKEN, responseObserver);
    }

    /**
     */
    public void addNode(com.grpc.TokenMessageOuterClass.NewNodeMessage request,
        io.grpc.stub.StreamObserver<com.grpc.TokenMessageOuterClass.NewNodeResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_ADD_NODE, responseObserver);
    }

    /**
     */
    public void removeNode(com.grpc.TokenMessageOuterClass.RemoveNodeMessage request,
        io.grpc.stub.StreamObserver<com.grpc.TokenMessageOuterClass.RemoveNodeResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_REMOVE_NODE, responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_HANDLE_TOKEN,
            asyncUnaryCall(
              new MethodHandlers<
                com.grpc.TokenMessageOuterClass.TokenMessage,
                com.grpc.TokenMessageOuterClass.TokenResponse>(
                  this, METHODID_HANDLE_TOKEN)))
          .addMethod(
            METHOD_ADD_NODE,
            asyncUnaryCall(
              new MethodHandlers<
                com.grpc.TokenMessageOuterClass.NewNodeMessage,
                com.grpc.TokenMessageOuterClass.NewNodeResponse>(
                  this, METHODID_ADD_NODE)))
          .addMethod(
            METHOD_REMOVE_NODE,
            asyncUnaryCall(
              new MethodHandlers<
                com.grpc.TokenMessageOuterClass.RemoveNodeMessage,
                com.grpc.TokenMessageOuterClass.RemoveNodeResponse>(
                  this, METHODID_REMOVE_NODE)))
          .build();
    }
  }

  /**
   */
  public static final class MessageHandlerStub extends io.grpc.stub.AbstractStub<MessageHandlerStub> {
    private MessageHandlerStub(io.grpc.Channel channel) {
      super(channel);
    }

    private MessageHandlerStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MessageHandlerStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new MessageHandlerStub(channel, callOptions);
    }

    /**
     */
    public void handleToken(com.grpc.TokenMessageOuterClass.TokenMessage request,
        io.grpc.stub.StreamObserver<com.grpc.TokenMessageOuterClass.TokenResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_HANDLE_TOKEN, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void addNode(com.grpc.TokenMessageOuterClass.NewNodeMessage request,
        io.grpc.stub.StreamObserver<com.grpc.TokenMessageOuterClass.NewNodeResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_ADD_NODE, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void removeNode(com.grpc.TokenMessageOuterClass.RemoveNodeMessage request,
        io.grpc.stub.StreamObserver<com.grpc.TokenMessageOuterClass.RemoveNodeResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_REMOVE_NODE, getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class MessageHandlerBlockingStub extends io.grpc.stub.AbstractStub<MessageHandlerBlockingStub> {
    private MessageHandlerBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private MessageHandlerBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MessageHandlerBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new MessageHandlerBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.grpc.TokenMessageOuterClass.TokenResponse handleToken(com.grpc.TokenMessageOuterClass.TokenMessage request) {
      return blockingUnaryCall(
          getChannel(), METHOD_HANDLE_TOKEN, getCallOptions(), request);
    }

    /**
     */
    public com.grpc.TokenMessageOuterClass.NewNodeResponse addNode(com.grpc.TokenMessageOuterClass.NewNodeMessage request) {
      return blockingUnaryCall(
          getChannel(), METHOD_ADD_NODE, getCallOptions(), request);
    }

    /**
     */
    public com.grpc.TokenMessageOuterClass.RemoveNodeResponse removeNode(com.grpc.TokenMessageOuterClass.RemoveNodeMessage request) {
      return blockingUnaryCall(
          getChannel(), METHOD_REMOVE_NODE, getCallOptions(), request);
    }
  }

  /**
   */
  public static final class MessageHandlerFutureStub extends io.grpc.stub.AbstractStub<MessageHandlerFutureStub> {
    private MessageHandlerFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private MessageHandlerFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MessageHandlerFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new MessageHandlerFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.grpc.TokenMessageOuterClass.TokenResponse> handleToken(
        com.grpc.TokenMessageOuterClass.TokenMessage request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_HANDLE_TOKEN, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.grpc.TokenMessageOuterClass.NewNodeResponse> addNode(
        com.grpc.TokenMessageOuterClass.NewNodeMessage request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_ADD_NODE, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.grpc.TokenMessageOuterClass.RemoveNodeResponse> removeNode(
        com.grpc.TokenMessageOuterClass.RemoveNodeMessage request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_REMOVE_NODE, getCallOptions()), request);
    }
  }

  private static final int METHODID_HANDLE_TOKEN = 0;
  private static final int METHODID_ADD_NODE = 1;
  private static final int METHODID_REMOVE_NODE = 2;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final MessageHandlerImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(MessageHandlerImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_HANDLE_TOKEN:
          serviceImpl.handleToken((com.grpc.TokenMessageOuterClass.TokenMessage) request,
              (io.grpc.stub.StreamObserver<com.grpc.TokenMessageOuterClass.TokenResponse>) responseObserver);
          break;
        case METHODID_ADD_NODE:
          serviceImpl.addNode((com.grpc.TokenMessageOuterClass.NewNodeMessage) request,
              (io.grpc.stub.StreamObserver<com.grpc.TokenMessageOuterClass.NewNodeResponse>) responseObserver);
          break;
        case METHODID_REMOVE_NODE:
          serviceImpl.removeNode((com.grpc.TokenMessageOuterClass.RemoveNodeMessage) request,
              (io.grpc.stub.StreamObserver<com.grpc.TokenMessageOuterClass.RemoveNodeResponse>) responseObserver);
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

  private static abstract class MessageHandlerBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    MessageHandlerBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.grpc.TokenMessageOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("MessageHandler");
    }
  }

  private static final class MessageHandlerFileDescriptorSupplier
      extends MessageHandlerBaseDescriptorSupplier {
    MessageHandlerFileDescriptorSupplier() {}
  }

  private static final class MessageHandlerMethodDescriptorSupplier
      extends MessageHandlerBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    MessageHandlerMethodDescriptorSupplier(String methodName) {
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
      synchronized (MessageHandlerGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new MessageHandlerFileDescriptorSupplier())
              .addMethod(METHOD_HANDLE_TOKEN)
              .addMethod(METHOD_ADD_NODE)
              .addMethod(METHOD_REMOVE_NODE)
              .build();
        }
      }
    }
    return result;
  }
}
