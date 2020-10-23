/*
 *
 * Copyright 2015 gRPC authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

#include <string>
#include <vector>

#include <grpcpp/grpcpp.h>

#include "router_grpc.grpc.pb.h"

using grpc::Channel;
using grpc::ChannelArguments;
using grpc::ClientContext;
using grpc::Status;

class VearchClient {
 public:
  VearchClient(std::shared_ptr<Channel> channel)
      : stub_(RouterGRPCService::NewStub(channel)) {}

  int Init(std::string &db_name, std::string &space_name) {
    db_name_ = db_name;
    space_name_ = space_name;
    SetRequestHead();
    return 0;
  }

  int SetRequestHead() {
    header_.set_db_name(db_name_);
    header_.set_space_name(space_name_);
    return 0;
  }

  int ParseResponseHead(const ResponseHead &header) {
    ErrorEnum code = header.err().code();
    return code;
  }
  
  // Assembles the client's payload, sends it and presents the response back
  // from the server.
  Status Space() {
    ClientContext context;
    Status status = stub_->Space(&context, header_, &table_);
    return status;
  }

  GetResponse Get(const std::vector<std::string> &request_data) {
    GetRequest request;
    request.mutable_head()->CopyFrom(header_);
    for(size_t i = 0; i < request_data.size(); i ++) {
      request.add_primary_keys(request_data[i]);
    }
    GetResponse response;
    ClientContext context;

    stub_->Get(&context, request, &response);
    return response;
  }

  AddResponse Add(Document &doc) {
    AddRequest request;
    request.mutable_doc()->CopyFrom(doc);
    request.mutable_head()->CopyFrom(header_);
    AddResponse response;
    ClientContext context;
    stub_->Add(&context, request, &response);
    return response;
  }

  DeleteResponse Delete(const std::vector<std::string> &request_data) {
    DeleteRequest request;
    request.mutable_head()->CopyFrom(header_);
    for(size_t i = 0; i < request_data.size(); i ++) {
      request.add_primary_keys(request_data[i]);
    }
    DeleteResponse response;
    ClientContext context;

    Status status = stub_->Delete(&context, request, &response);

    return response;
  }

  UpdateResponse Update(Document &doc) {
    UpdateRequest request;
    request.mutable_doc()->CopyFrom(doc);
    request.mutable_head()->CopyFrom(header_);
    UpdateResponse response;
    ClientContext context;
    Status status = stub_->Update(&context, request, &response);

    return response;
  }

  BulkResponse Bulk(const std::vector<Document> &docs) {
    BulkRequest request;
    request.mutable_head()->CopyFrom(header_);
    for (size_t i = 0; i < docs.size(); i++) {
      request.add_docs()->CopyFrom(docs[i]);
    }
    BulkResponse response;
    ClientContext context;

    Status status = stub_->Bulk(&context, request, &response);

    return response;
  }

  SearchResponse Search(SearchRequest &request) {
    SearchResponse response;
    ClientContext context;

    Status status = stub_->Search(&context, request, &response);
    return response;
  }

  SearchResponse MSearch(const std::vector<SearchRequest> &requests) {
    MSearchRequest request;
    request.mutable_head()->CopyFrom(header_);
    for (size_t i = 0; i < requests.size(); i++) {
      request.add_search_requests()->CopyFrom(requests[i]);
    }
    SearchResponse response;
    ClientContext context;

    Status status = stub_->MSearch(&context, request, &response);
    return response;
  }

  const Table &GetTable() { return table_; }
  const RequestHead &GetHeader() { return header_; }
 private:
  std::unique_ptr<RouterGRPCService::Stub> stub_;
  Table table_;
  std::string db_name_;
  std::string space_name_;
  RequestHead header_;
};
