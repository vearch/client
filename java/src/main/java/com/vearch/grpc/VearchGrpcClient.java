package com.vearch.grpc;

import com.vearch.grpc.entity.*;
import com.vearch.grpc.service.RouterGRPCServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class VearchGrpcClient {

    private String ip;      //router ip
    private int port;       //router port

    private ManagedChannel channel;
    private RouterGRPCServiceGrpc.RouterGRPCServiceBlockingStub routerGRPCServiceBlockingStub;

    public VearchGrpcClient(String ip, int port){
        channel = ManagedChannelBuilder.forAddress(ip, port).usePlaintext().build();
        routerGRPCServiceBlockingStub = RouterGRPCServiceGrpc.newBlockingStub(channel);
    }

    public GetResponse get(GetRequest getRequest){
        return routerGRPCServiceBlockingStub.get(getRequest);
    }

    public AddResponse add(AddRequest addRequest){
        return routerGRPCServiceBlockingStub.add(addRequest);
    }

    public DeleteResponse delete(DeleteRequest deleteRequest){
        return routerGRPCServiceBlockingStub.delete(deleteRequest);
    }

    public UpdateResponse update(UpdateRequest updateRequest){
        return routerGRPCServiceBlockingStub.update(updateRequest);
    }

    public SearchResponse search(SearchRequest searchRequest){
        return routerGRPCServiceBlockingStub.search(searchRequest);
    }

    public BulkResponse bulk(BulkRequest bulkRequest){
        return routerGRPCServiceBlockingStub.bulk(bulkRequest);
    }

    public SearchResponse msearch(MSearchRequest mSearchRequest){
        return routerGRPCServiceBlockingStub.mSearch(mSearchRequest);
    }

    public Table space(RequestHead requestHead){
        return routerGRPCServiceBlockingStub.space(requestHead);
    }

    public void close(){
        if(!channel.isShutdown()){
            channel.shutdown();
        }
    }
}
