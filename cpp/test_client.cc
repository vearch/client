#include <iostream>
#include <memory>
#include <string>
#include <vector>
#include <map>
#include <random>
#include <cstdlib>
#include <ctime>
#include <sstream>
#include <unistd.h>

#include "client.h"

std::map<int, std::string> field_type_map {
  { 0 , "INT" },
  { 1 , "LONG" },
  { 2 , "FLOAT" },
  { 3 , "DOUBLE" },
  { 4 , "STRING" },
  { 5 , "VECTOR" },
  { 6 , "BOOL" },
  { 7 , "GEOPOINT" },
  { 8 , "DATE" }
};

std::map<int, std::string> value_type_map {
  { 0 , "FLOAT" },
  { 1 , "UINT8" }
};

int MakeDocument(const Table &table, int idx, Document &doc) {
  Field *field;
  size_t field_size = table.table_meta_info().field_meta_info_size();
  doc.set_p_key("test_" + std::to_string(idx));
  for (size_t i = 0; i < table.table_meta_info().field_meta_info_size(); i++) {
    field = doc.add_fields();
    field->set_name(table.table_meta_info().field_meta_info(i).name());
    if (field_type_map[table.table_meta_info().field_meta_info(i).data_type()] == "INT") {
      int value = rand();
      std::string real_value((char *)&value, sizeof(int));
      field->set_value(real_value);
    }
    if (field_type_map[table.table_meta_info().field_meta_info(i).data_type()] == "FLOAT") {
      double value = drand48();
      std::string real_value((char *)&value, sizeof(float));
      field->set_value(real_value);
    }
    if (field_type_map[table.table_meta_info().field_meta_info(i).data_type()] == "DOUBLE") {
      double value = drand48();
      std::string real_value((char *)&value, sizeof(double));
      field->set_value(real_value);
    }
    if (field_type_map[table.table_meta_info().field_meta_info(i).data_type()] == "STRING") {
      //int value = rand();
      field->set_value("url_" + std::to_string(idx));
    }
    if (field_type_map[table.table_meta_info().field_meta_info(i).data_type()] == "VECTOR") {
      int d = table.table_meta_info().field_meta_info(i).vector_meta_info().dimension();
      if (table.table_meta_info().field_meta_info(i).vector_meta_info().value_type() == 0) {
        std::vector<float> values(d);
        for (int j = 0; j < d; j++) {
          values[j] = drand48();
        }
        std::string value((char *)values.data(), d * sizeof(float));
        field->set_value(value);
      } else if (
          table.table_meta_info().field_meta_info(i).vector_meta_info().value_type() == 1) {
        std::vector<uint8_t> values(d);
        for (int j = 0; j < d; j++) {
          values[j] = rand();
        }
        std::string value((char *)values.data(), d * sizeof(uint8_t));
        field->set_value(value);
      }
    }
    field->set_type(table.table_meta_info().field_meta_info(i).data_type());
  }
  return 0;
}

int MakeSearchRequest(const Table &table, const RequestHead &header, SearchRequest &request) {
  request.mutable_head()->CopyFrom(header);

  request.set_req_num(1);
  request.set_topn(10);
  //request.set_is_brute_search(1);

  //request.set_multi_vector_rank();
  request.set_has_rank(1);
  request.set_parallel_based_on_query(0);
  request.set_l2_sqrt(0);
  request.set_ivf_flat(0);
  request.set_is_vector_value(1);
  VectorQuery *vec_field;
  for (size_t i = 0; i < table.table_meta_info().field_meta_info_size(); i++) {
    if (table.table_meta_info().field_meta_info(i).data_type() == VECTOR) {
      vec_field = request.add_vec_fields();
      vec_field->set_name(table.table_meta_info().field_meta_info(i).name());
      //set as default value
      vec_field->set_min_score(-10000);
      vec_field->set_max_score(10000);
      vec_field->set_boost(0);
      vec_field->set_has_boost(0);
      int d = table.table_meta_info().field_meta_info(i).vector_meta_info().dimension();
      if (table.table_meta_info().field_meta_info(i).vector_meta_info().value_type() == 0) {
        std::vector<float> values(d);
        for (int j = 0; j < d; j++) {
          values[j] = drand48();
        }
        std::string value((char *)values.data(), d * sizeof(float));
        vec_field->set_value(value);
      } else if (
          table.table_meta_info().field_meta_info(i).vector_meta_info().value_type() == 1) {        
        std::vector<uint8_t> values(d);
        for (int j = 0; j < d; j++) {
          values[j] = rand();
        }
        std::string value((char *)values.data(), d * sizeof(uint8_t));
        vec_field->set_value(value);
      }
    }
  }
  return 0;
}

template <class Type>
Type StringToNum(const char *str)
{
    Type num;
    memcpy((char *)&num, str, sizeof(Type));
    return num;
}

int PrintValue(const std::string &value, int type, int get_from_db = 0) {
  if (type == VECTOR) {
    int d;
    std::vector<float> values;
    if (get_from_db) {
      d = value.size() / sizeof(float) - 1;
      values.resize(d);
      memcpy((char *)values.data(), value.c_str() + sizeof(int), value.size() - sizeof(int));
    } else {
      d = value.size() / sizeof(float);
      values.resize(d);
      memcpy((char *)values.data(), value.c_str(), value.size());
    }
    std::string data;
    data += "[";
    for (int i = 0; i < d; i++) {
      data += std::to_string(values[i]);
      if (i != d - 1) data += ",";
    }
    data += "]";
    std::cout << data << std::endl;
  } else if (type == INT) {
    std::cout << StringToNum<int>(value.c_str()) << std::endl;
  } else if (type == LONG) {
    std::cout << StringToNum<long>(value.c_str()) << std::endl;
  } else if (type == FLOAT) {
    std::cout << StringToNum<float>(value.c_str()) << std::endl;
  } else if (type == DOUBLE) {
    std::cout << StringToNum<double>(value.c_str()) << std::endl;
  } else if (type == STRING) {
    std::cout << value << std::endl;
  } else {
    std::cout << "Wrong type: " << type << std::endl;
  }
  return 0;
}

int PrintField(const Field &field, int get_from_db = 0, int white_space = 0) {
  for (int i = 0; i < white_space; i++) {
    std::cout << " ";
  }
  std::cout << "\"" << field.name() << "\" : ";
  PrintValue(field.value(), field.type(), get_from_db);
  return 0;
}

int PrintDocument(const Document &doc, int get_from_db = 0) {
  std::cout << "{" << std::endl;
  for (size_t i = 0; i < doc.fields_size(); i++) {
    PrintField(doc.fields(i), get_from_db, 4);
  }
  std::cout << "}" << std::endl;
  return 0;
}

int PrintSearchResult(const SearchResult &result) {
  std::cout << "{" << std::endl;
  std::cout << "    \"total_hits\" : " << result.total_hits() << std::endl;
  std::cout << "    \"max_score\" : " << result.max_score() << std::endl;
  std::cout << "    \"max_took_id\" : " << result.max_took_id() << std::endl;
  std::cout << "    {" << std::endl;
  std::cout << "        \"total\" : " << result.status().total() << std::endl;
  std::cout << "        \"failed\" : " << result.status().failed() << std::endl;
  std::cout << "        \"successful\" : " << result.status().successful() << std::endl;
  std::cout << "        \"msg\" : " << result.status().msg() << std::endl;
  std::cout << "    }" << std::endl;
  for (size_t i = 0; i < result.result_items_size(); i++) {
    std::cout << "    {" << std::endl;
    std::cout << "        \"score\" : " << result.result_items(i).score() << std::endl;
    std::cout << "        \"extra\" : " << result.result_items(i).extra() << std::endl;
    std::cout << "        \"_id\" : " << result.result_items(i).p_key() << std::endl;
    std::cout << "        \"source\" : " << result.result_items(i).source() << std::endl;
    for (size_t j = 0; j < result.result_items(i).fields_size(); j++) {
      std::cout << "        {" << std::endl;
      PrintField(result.result_items(i).fields(j), 1, 12);
      std::cout << "        }" << std::endl;
    }
    std::cout << "    }" << std::endl;
  }
  std::cout << "}" << std::endl;
  return 0;
}

int main(int argc, char** argv) {
  // Instantiate the client. It requires a channel, out of which the actual RPCs
  // are created. This channel models a connection to an endpoint (in this case,
  // localhost at port 9002). We indicate that the channel isn't authenticated
  // (use of InsecureChannelCredentials()).
  ChannelArguments args;
  // Set the load balancing policy for the channel.
  args.SetLoadBalancingPolicyName("round_robin");
  VearchClient client(grpc::CreateCustomChannel(
      "localhost:9002", grpc::InsecureChannelCredentials(), args));
  std::string db_name = "ts_db";
  std::string space_name = "ts_space";
  client.Init(db_name, space_name);
  client.Space();
  srand(time(0));
  int add_num = 10000;
  int get_num = 10;
  int batch_add_num = 10;
  int batch_search_num = 10;

  // add 
  std::cout << "add" << std::endl;
  for (int i = 0; i < add_num; i++) {
    Document doc;
    MakeDocument(client.GetTable(), i, doc);
    client.Add(doc);
  }

  // get
  std::cout << "get" << std::endl;
  std::vector<std::string> keys(get_num);
  for (int i = 0; i < get_num; i++)
    keys[i] = "test_" + std::to_string(i);
  GetResponse get_response = client.Get(keys);
  for (size_t i = 0; i < get_response.items_size(); i++) {
     PrintDocument(get_response.items(i).doc());
  }

  // update
  std::cout << "update" << std::endl;
  Document doc;
  MakeDocument(client.GetTable(), 0, doc);
  UpdateResponse update_response = client.Update(doc);

  // get
  std::cout << "get" << std::endl;
  get_response = client.Get(keys);
  for (size_t i = 0; i < get_response.items_size(); i++) {
     PrintDocument(get_response.items(i).doc());
  }

  // bulk
  std::cout << "bulk" << std::endl;
  std::vector<Document> docs(batch_add_num);
  for (int i = 0; i < batch_add_num; i++) {
    Document doc;
    MakeDocument(client.GetTable(), i + add_num, doc);
    docs[i] = doc;
  }
  BulkResponse bulk_response = client.Bulk(docs);

  // delete
  std::cout << "delete" << std::endl;
  DeleteResponse delete_response = client.Delete(keys);

  // get
  std::cout << "get" << std::endl;
  get_response = client.Get(keys);
  for (size_t i = 0; i < get_response.items_size(); i++) {
     PrintDocument(get_response.items(i).doc());
  }
  // search
  std::cout << "search" << std::endl;
  SearchRequest search_request;
  MakeSearchRequest(client.GetTable(), client.GetHeader(), search_request);
  SearchResponse result = client.Search(search_request);
  for (size_t i = 0; i < result.results_size(); i++) {
    PrintSearchResult(result.results(i));
  }

  std::cout << "msearch" << std::endl;
  std::vector<SearchRequest> requests(batch_search_num);
  for (int i = 0; i < batch_search_num; i++) {
    MakeSearchRequest(client.GetTable(), client.GetHeader(), search_request);
    requests[i] = search_request;
  }
  SearchResponse mresult = client.MSearch(requests);
  for (size_t i = 0; i < mresult.results_size(); i++) {
    PrintSearchResult(mresult.results(i));
  }
  return 0;
}
