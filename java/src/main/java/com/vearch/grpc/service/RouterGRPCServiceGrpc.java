package com.vearch.grpc.service;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.31.1)",
    comments = "Source: router_grpc.proto")
public final class RouterGRPCServiceGrpc {

  private RouterGRPCServiceGrpc() {}

  public static final String SERVICE_NAME = "RouterGRPCService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.vearch.grpc.entity.GetRequest,
      com.vearch.grpc.entity.GetResponse> getGetMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Get",
      requestType = com.vearch.grpc.entity.GetRequest.class,
      responseType = com.vearch.grpc.entity.GetResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.vearch.grpc.entity.GetRequest,
      com.vearch.grpc.entity.GetResponse> getGetMethod() {
    io.grpc.MethodDescriptor<com.vearch.grpc.entity.GetRequest, com.vearch.grpc.entity.GetResponse> getGetMethod;
    if ((getGetMethod = RouterGRPCServiceGrpc.getGetMethod) == null) {
      synchronized (RouterGRPCServiceGrpc.class) {
        if ((getGetMethod = RouterGRPCServiceGrpc.getGetMethod) == null) {
          RouterGRPCServiceGrpc.getGetMethod = getGetMethod =
              io.grpc.MethodDescriptor.<com.vearch.grpc.entity.GetRequest, com.vearch.grpc.entity.GetResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Get"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.vearch.grpc.entity.GetRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.vearch.grpc.entity.GetResponse.getDefaultInstance()))
              .setSchemaDescriptor(new RouterGRPCServiceMethodDescriptorSupplier("Get"))
              .build();
        }
      }
    }
    return getGetMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.vearch.grpc.entity.AddRequest,
      com.vearch.grpc.entity.AddResponse> getAddMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Add",
      requestType = com.vearch.grpc.entity.AddRequest.class,
      responseType = com.vearch.grpc.entity.AddResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.vearch.grpc.entity.AddRequest,
      com.vearch.grpc.entity.AddResponse> getAddMethod() {
    io.grpc.MethodDescriptor<com.vearch.grpc.entity.AddRequest, com.vearch.grpc.entity.AddResponse> getAddMethod;
    if ((getAddMethod = RouterGRPCServiceGrpc.getAddMethod) == null) {
      synchronized (RouterGRPCServiceGrpc.class) {
        if ((getAddMethod = RouterGRPCServiceGrpc.getAddMethod) == null) {
          RouterGRPCServiceGrpc.getAddMethod = getAddMethod =
              io.grpc.MethodDescriptor.<com.vearch.grpc.entity.AddRequest, com.vearch.grpc.entity.AddResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Add"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.vearch.grpc.entity.AddRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.vearch.grpc.entity.AddResponse.getDefaultInstance()))
              .setSchemaDescriptor(new RouterGRPCServiceMethodDescriptorSupplier("Add"))
              .build();
        }
      }
    }
    return getAddMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.vearch.grpc.entity.DeleteRequest,
      com.vearch.grpc.entity.DeleteResponse> getDeleteMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Delete",
      requestType = com.vearch.grpc.entity.DeleteRequest.class,
      responseType = com.vearch.grpc.entity.DeleteResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.vearch.grpc.entity.DeleteRequest,
      com.vearch.grpc.entity.DeleteResponse> getDeleteMethod() {
    io.grpc.MethodDescriptor<com.vearch.grpc.entity.DeleteRequest, com.vearch.grpc.entity.DeleteResponse> getDeleteMethod;
    if ((getDeleteMethod = RouterGRPCServiceGrpc.getDeleteMethod) == null) {
      synchronized (RouterGRPCServiceGrpc.class) {
        if ((getDeleteMethod = RouterGRPCServiceGrpc.getDeleteMethod) == null) {
          RouterGRPCServiceGrpc.getDeleteMethod = getDeleteMethod =
              io.grpc.MethodDescriptor.<com.vearch.grpc.entity.DeleteRequest, com.vearch.grpc.entity.DeleteResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Delete"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.vearch.grpc.entity.DeleteRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.vearch.grpc.entity.DeleteResponse.getDefaultInstance()))
              .setSchemaDescriptor(new RouterGRPCServiceMethodDescriptorSupplier("Delete"))
              .build();
        }
      }
    }
    return getDeleteMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.vearch.grpc.entity.UpdateRequest,
      com.vearch.grpc.entity.UpdateResponse> getUpdateMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Update",
      requestType = com.vearch.grpc.entity.UpdateRequest.class,
      responseType = com.vearch.grpc.entity.UpdateResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.vearch.grpc.entity.UpdateRequest,
      com.vearch.grpc.entity.UpdateResponse> getUpdateMethod() {
    io.grpc.MethodDescriptor<com.vearch.grpc.entity.UpdateRequest, com.vearch.grpc.entity.UpdateResponse> getUpdateMethod;
    if ((getUpdateMethod = RouterGRPCServiceGrpc.getUpdateMethod) == null) {
      synchronized (RouterGRPCServiceGrpc.class) {
        if ((getUpdateMethod = RouterGRPCServiceGrpc.getUpdateMethod) == null) {
          RouterGRPCServiceGrpc.getUpdateMethod = getUpdateMethod =
              io.grpc.MethodDescriptor.<com.vearch.grpc.entity.UpdateRequest, com.vearch.grpc.entity.UpdateResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Update"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.vearch.grpc.entity.UpdateRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.vearch.grpc.entity.UpdateResponse.getDefaultInstance()))
              .setSchemaDescriptor(new RouterGRPCServiceMethodDescriptorSupplier("Update"))
              .build();
        }
      }
    }
    return getUpdateMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.vearch.grpc.entity.SearchRequest,
      com.vearch.grpc.entity.SearchResponse> getSearchMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Search",
      requestType = com.vearch.grpc.entity.SearchRequest.class,
      responseType = com.vearch.grpc.entity.SearchResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.vearch.grpc.entity.SearchRequest,
      com.vearch.grpc.entity.SearchResponse> getSearchMethod() {
    io.grpc.MethodDescriptor<com.vearch.grpc.entity.SearchRequest, com.vearch.grpc.entity.SearchResponse> getSearchMethod;
    if ((getSearchMethod = RouterGRPCServiceGrpc.getSearchMethod) == null) {
      synchronized (RouterGRPCServiceGrpc.class) {
        if ((getSearchMethod = RouterGRPCServiceGrpc.getSearchMethod) == null) {
          RouterGRPCServiceGrpc.getSearchMethod = getSearchMethod =
              io.grpc.MethodDescriptor.<com.vearch.grpc.entity.SearchRequest, com.vearch.grpc.entity.SearchResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Search"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.vearch.grpc.entity.SearchRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.vearch.grpc.entity.SearchResponse.getDefaultInstance()))
              .setSchemaDescriptor(new RouterGRPCServiceMethodDescriptorSupplier("Search"))
              .build();
        }
      }
    }
    return getSearchMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.vearch.grpc.entity.BulkRequest,
      com.vearch.grpc.entity.BulkResponse> getBulkMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Bulk",
      requestType = com.vearch.grpc.entity.BulkRequest.class,
      responseType = com.vearch.grpc.entity.BulkResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.vearch.grpc.entity.BulkRequest,
      com.vearch.grpc.entity.BulkResponse> getBulkMethod() {
    io.grpc.MethodDescriptor<com.vearch.grpc.entity.BulkRequest, com.vearch.grpc.entity.BulkResponse> getBulkMethod;
    if ((getBulkMethod = RouterGRPCServiceGrpc.getBulkMethod) == null) {
      synchronized (RouterGRPCServiceGrpc.class) {
        if ((getBulkMethod = RouterGRPCServiceGrpc.getBulkMethod) == null) {
          RouterGRPCServiceGrpc.getBulkMethod = getBulkMethod =
              io.grpc.MethodDescriptor.<com.vearch.grpc.entity.BulkRequest, com.vearch.grpc.entity.BulkResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Bulk"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.vearch.grpc.entity.BulkRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.vearch.grpc.entity.BulkResponse.getDefaultInstance()))
              .setSchemaDescriptor(new RouterGRPCServiceMethodDescriptorSupplier("Bulk"))
              .build();
        }
      }
    }
    return getBulkMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.vearch.grpc.entity.MSearchRequest,
      com.vearch.grpc.entity.SearchResponse> getMSearchMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "MSearch",
      requestType = com.vearch.grpc.entity.MSearchRequest.class,
      responseType = com.vearch.grpc.entity.SearchResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.vearch.grpc.entity.MSearchRequest,
      com.vearch.grpc.entity.SearchResponse> getMSearchMethod() {
    io.grpc.MethodDescriptor<com.vearch.grpc.entity.MSearchRequest, com.vearch.grpc.entity.SearchResponse> getMSearchMethod;
    if ((getMSearchMethod = RouterGRPCServiceGrpc.getMSearchMethod) == null) {
      synchronized (RouterGRPCServiceGrpc.class) {
        if ((getMSearchMethod = RouterGRPCServiceGrpc.getMSearchMethod) == null) {
          RouterGRPCServiceGrpc.getMSearchMethod = getMSearchMethod =
              io.grpc.MethodDescriptor.<com.vearch.grpc.entity.MSearchRequest, com.vearch.grpc.entity.SearchResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "MSearch"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.vearch.grpc.entity.MSearchRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.vearch.grpc.entity.SearchResponse.getDefaultInstance()))
              .setSchemaDescriptor(new RouterGRPCServiceMethodDescriptorSupplier("MSearch"))
              .build();
        }
      }
    }
    return getMSearchMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.vearch.grpc.entity.RequestHead,
      com.vearch.grpc.entity.Table> getSpaceMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Space",
      requestType = com.vearch.grpc.entity.RequestHead.class,
      responseType = com.vearch.grpc.entity.Table.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.vearch.grpc.entity.RequestHead,
      com.vearch.grpc.entity.Table> getSpaceMethod() {
    io.grpc.MethodDescriptor<com.vearch.grpc.entity.RequestHead, com.vearch.grpc.entity.Table> getSpaceMethod;
    if ((getSpaceMethod = RouterGRPCServiceGrpc.getSpaceMethod) == null) {
      synchronized (RouterGRPCServiceGrpc.class) {
        if ((getSpaceMethod = RouterGRPCServiceGrpc.getSpaceMethod) == null) {
          RouterGRPCServiceGrpc.getSpaceMethod = getSpaceMethod =
              io.grpc.MethodDescriptor.<com.vearch.grpc.entity.RequestHead, com.vearch.grpc.entity.Table>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Space"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.vearch.grpc.entity.RequestHead.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.vearch.grpc.entity.Table.getDefaultInstance()))
              .setSchemaDescriptor(new RouterGRPCServiceMethodDescriptorSupplier("Space"))
              .build();
        }
      }
    }
    return getSpaceMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static RouterGRPCServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<RouterGRPCServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<RouterGRPCServiceStub>() {
        @Override
        public RouterGRPCServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new RouterGRPCServiceStub(channel, callOptions);
        }
      };
    return RouterGRPCServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static RouterGRPCServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<RouterGRPCServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<RouterGRPCServiceBlockingStub>() {
        @Override
        public RouterGRPCServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new RouterGRPCServiceBlockingStub(channel, callOptions);
        }
      };
    return RouterGRPCServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static RouterGRPCServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<RouterGRPCServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<RouterGRPCServiceFutureStub>() {
        @Override
        public RouterGRPCServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new RouterGRPCServiceFutureStub(channel, callOptions);
        }
      };
    return RouterGRPCServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class RouterGRPCServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void get(com.vearch.grpc.entity.GetRequest request,
        io.grpc.stub.StreamObserver<com.vearch.grpc.entity.GetResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetMethod(), responseObserver);
    }

    /**
     */
    public void add(com.vearch.grpc.entity.AddRequest request,
        io.grpc.stub.StreamObserver<com.vearch.grpc.entity.AddResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getAddMethod(), responseObserver);
    }

    /**
     */
    public void delete(com.vearch.grpc.entity.DeleteRequest request,
        io.grpc.stub.StreamObserver<com.vearch.grpc.entity.DeleteResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getDeleteMethod(), responseObserver);
    }

    /**
     */
    public void update(com.vearch.grpc.entity.UpdateRequest request,
        io.grpc.stub.StreamObserver<com.vearch.grpc.entity.UpdateResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getUpdateMethod(), responseObserver);
    }

    /**
     */
    public void search(com.vearch.grpc.entity.SearchRequest request,
        io.grpc.stub.StreamObserver<com.vearch.grpc.entity.SearchResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getSearchMethod(), responseObserver);
    }

    /**
     */
    public void bulk(com.vearch.grpc.entity.BulkRequest request,
        io.grpc.stub.StreamObserver<com.vearch.grpc.entity.BulkResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getBulkMethod(), responseObserver);
    }

    /**
     */
    public void mSearch(com.vearch.grpc.entity.MSearchRequest request,
        io.grpc.stub.StreamObserver<com.vearch.grpc.entity.SearchResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getMSearchMethod(), responseObserver);
    }

    /**
     */
    public void space(com.vearch.grpc.entity.RequestHead request,
        io.grpc.stub.StreamObserver<com.vearch.grpc.entity.Table> responseObserver) {
      asyncUnimplementedUnaryCall(getSpaceMethod(), responseObserver);
    }

    @Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getGetMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.vearch.grpc.entity.GetRequest,
                com.vearch.grpc.entity.GetResponse>(
                  this, METHODID_GET)))
          .addMethod(
            getAddMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.vearch.grpc.entity.AddRequest,
                com.vearch.grpc.entity.AddResponse>(
                  this, METHODID_ADD)))
          .addMethod(
            getDeleteMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.vearch.grpc.entity.DeleteRequest,
                com.vearch.grpc.entity.DeleteResponse>(
                  this, METHODID_DELETE)))
          .addMethod(
            getUpdateMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.vearch.grpc.entity.UpdateRequest,
                com.vearch.grpc.entity.UpdateResponse>(
                  this, METHODID_UPDATE)))
          .addMethod(
            getSearchMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.vearch.grpc.entity.SearchRequest,
                com.vearch.grpc.entity.SearchResponse>(
                  this, METHODID_SEARCH)))
          .addMethod(
            getBulkMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.vearch.grpc.entity.BulkRequest,
                com.vearch.grpc.entity.BulkResponse>(
                  this, METHODID_BULK)))
          .addMethod(
            getMSearchMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.vearch.grpc.entity.MSearchRequest,
                com.vearch.grpc.entity.SearchResponse>(
                  this, METHODID_MSEARCH)))
          .addMethod(
            getSpaceMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.vearch.grpc.entity.RequestHead,
                com.vearch.grpc.entity.Table>(
                  this, METHODID_SPACE)))
          .build();
    }
  }

  /**
   */
  public static final class RouterGRPCServiceStub extends io.grpc.stub.AbstractAsyncStub<RouterGRPCServiceStub> {
    private RouterGRPCServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected RouterGRPCServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new RouterGRPCServiceStub(channel, callOptions);
    }

    /**
     */
    public void get(com.vearch.grpc.entity.GetRequest request,
        io.grpc.stub.StreamObserver<com.vearch.grpc.entity.GetResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void add(com.vearch.grpc.entity.AddRequest request,
        io.grpc.stub.StreamObserver<com.vearch.grpc.entity.AddResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getAddMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void delete(com.vearch.grpc.entity.DeleteRequest request,
        io.grpc.stub.StreamObserver<com.vearch.grpc.entity.DeleteResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getDeleteMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void update(com.vearch.grpc.entity.UpdateRequest request,
        io.grpc.stub.StreamObserver<com.vearch.grpc.entity.UpdateResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getUpdateMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void search(com.vearch.grpc.entity.SearchRequest request,
        io.grpc.stub.StreamObserver<com.vearch.grpc.entity.SearchResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSearchMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void bulk(com.vearch.grpc.entity.BulkRequest request,
        io.grpc.stub.StreamObserver<com.vearch.grpc.entity.BulkResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getBulkMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void mSearch(com.vearch.grpc.entity.MSearchRequest request,
        io.grpc.stub.StreamObserver<com.vearch.grpc.entity.SearchResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getMSearchMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void space(com.vearch.grpc.entity.RequestHead request,
        io.grpc.stub.StreamObserver<com.vearch.grpc.entity.Table> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSpaceMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class RouterGRPCServiceBlockingStub extends io.grpc.stub.AbstractBlockingStub<RouterGRPCServiceBlockingStub> {
    private RouterGRPCServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected RouterGRPCServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new RouterGRPCServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.vearch.grpc.entity.GetResponse get(com.vearch.grpc.entity.GetRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.vearch.grpc.entity.AddResponse add(com.vearch.grpc.entity.AddRequest request) {
      return blockingUnaryCall(
          getChannel(), getAddMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.vearch.grpc.entity.DeleteResponse delete(com.vearch.grpc.entity.DeleteRequest request) {
      return blockingUnaryCall(
          getChannel(), getDeleteMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.vearch.grpc.entity.UpdateResponse update(com.vearch.grpc.entity.UpdateRequest request) {
      return blockingUnaryCall(
          getChannel(), getUpdateMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.vearch.grpc.entity.SearchResponse search(com.vearch.grpc.entity.SearchRequest request) {
      return blockingUnaryCall(
          getChannel(), getSearchMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.vearch.grpc.entity.BulkResponse bulk(com.vearch.grpc.entity.BulkRequest request) {
      return blockingUnaryCall(
          getChannel(), getBulkMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.vearch.grpc.entity.SearchResponse mSearch(com.vearch.grpc.entity.MSearchRequest request) {
      return blockingUnaryCall(
          getChannel(), getMSearchMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.vearch.grpc.entity.Table space(com.vearch.grpc.entity.RequestHead request) {
      return blockingUnaryCall(
          getChannel(), getSpaceMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class RouterGRPCServiceFutureStub extends io.grpc.stub.AbstractFutureStub<RouterGRPCServiceFutureStub> {
    private RouterGRPCServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected RouterGRPCServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new RouterGRPCServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.vearch.grpc.entity.GetResponse> get(
        com.vearch.grpc.entity.GetRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.vearch.grpc.entity.AddResponse> add(
        com.vearch.grpc.entity.AddRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getAddMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.vearch.grpc.entity.DeleteResponse> delete(
        com.vearch.grpc.entity.DeleteRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getDeleteMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.vearch.grpc.entity.UpdateResponse> update(
        com.vearch.grpc.entity.UpdateRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getUpdateMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.vearch.grpc.entity.SearchResponse> search(
        com.vearch.grpc.entity.SearchRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getSearchMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.vearch.grpc.entity.BulkResponse> bulk(
        com.vearch.grpc.entity.BulkRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getBulkMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.vearch.grpc.entity.SearchResponse> mSearch(
        com.vearch.grpc.entity.MSearchRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getMSearchMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.vearch.grpc.entity.Table> space(
        com.vearch.grpc.entity.RequestHead request) {
      return futureUnaryCall(
          getChannel().newCall(getSpaceMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET = 0;
  private static final int METHODID_ADD = 1;
  private static final int METHODID_DELETE = 2;
  private static final int METHODID_UPDATE = 3;
  private static final int METHODID_SEARCH = 4;
  private static final int METHODID_BULK = 5;
  private static final int METHODID_MSEARCH = 6;
  private static final int METHODID_SPACE = 7;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final RouterGRPCServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(RouterGRPCServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET:
          serviceImpl.get((com.vearch.grpc.entity.GetRequest) request,
              (io.grpc.stub.StreamObserver<com.vearch.grpc.entity.GetResponse>) responseObserver);
          break;
        case METHODID_ADD:
          serviceImpl.add((com.vearch.grpc.entity.AddRequest) request,
              (io.grpc.stub.StreamObserver<com.vearch.grpc.entity.AddResponse>) responseObserver);
          break;
        case METHODID_DELETE:
          serviceImpl.delete((com.vearch.grpc.entity.DeleteRequest) request,
              (io.grpc.stub.StreamObserver<com.vearch.grpc.entity.DeleteResponse>) responseObserver);
          break;
        case METHODID_UPDATE:
          serviceImpl.update((com.vearch.grpc.entity.UpdateRequest) request,
              (io.grpc.stub.StreamObserver<com.vearch.grpc.entity.UpdateResponse>) responseObserver);
          break;
        case METHODID_SEARCH:
          serviceImpl.search((com.vearch.grpc.entity.SearchRequest) request,
              (io.grpc.stub.StreamObserver<com.vearch.grpc.entity.SearchResponse>) responseObserver);
          break;
        case METHODID_BULK:
          serviceImpl.bulk((com.vearch.grpc.entity.BulkRequest) request,
              (io.grpc.stub.StreamObserver<com.vearch.grpc.entity.BulkResponse>) responseObserver);
          break;
        case METHODID_MSEARCH:
          serviceImpl.mSearch((com.vearch.grpc.entity.MSearchRequest) request,
              (io.grpc.stub.StreamObserver<com.vearch.grpc.entity.SearchResponse>) responseObserver);
          break;
        case METHODID_SPACE:
          serviceImpl.space((com.vearch.grpc.entity.RequestHead) request,
              (io.grpc.stub.StreamObserver<com.vearch.grpc.entity.Table>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @Override
    @SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class RouterGRPCServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    RouterGRPCServiceBaseDescriptorSupplier() {}

    @Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.vearch.grpc.entity.RouterGrpc.getDescriptor();
    }

    @Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("RouterGRPCService");
    }
  }

  private static final class RouterGRPCServiceFileDescriptorSupplier
      extends RouterGRPCServiceBaseDescriptorSupplier {
    RouterGRPCServiceFileDescriptorSupplier() {}
  }

  private static final class RouterGRPCServiceMethodDescriptorSupplier
      extends RouterGRPCServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    RouterGRPCServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (RouterGRPCServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new RouterGRPCServiceFileDescriptorSupplier())
              .addMethod(getGetMethod())
              .addMethod(getAddMethod())
              .addMethod(getDeleteMethod())
              .addMethod(getUpdateMethod())
              .addMethod(getSearchMethod())
              .addMethod(getBulkMethod())
              .addMethod(getMSearchMethod())
              .addMethod(getSpaceMethod())
              .build();
        }
      }
    }
    return result;
  }
}
