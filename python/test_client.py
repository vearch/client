# -*- coding: utf-8 -*-

import os
import sys
import numpy as np
sys.path.append(os.path.join(os.getcwd(), 'vearchpb'))
import client
import data_model_pb2 as dpb
import router_grpc_pb2 as rpb


TotalNum = 10
Address = "localhost:9002"
DBName = 'ts_db'
SpaceName = 'ts_space'


def println(*args):
    print(*args)
    

def random_request(fields):
    request = {}
    for field in fields:
        k, v = field.name, field.data_type
        if v == dpb.FieldType.INT:
            request[k] = np.random.randint(TotalNum, dtype=np.int32)
        elif v == dpb.FieldType.LONG:
            request[k] = np.random.randint(TotalNum)
        elif v == dpb.FieldType.FLOAT:
            request[k] = np.random.randn()
        elif v == dpb.FieldType.DOUBLE:
            request[k] = np.random.randn()
        elif v == dpb.FieldType.STRING:
            request[k] = str(np.random.randn())
        elif v == dpb.FieldType.VECTOR:
            dimension = field.vector_meta_info.dimension
            request[k] = [abs(n) for n in np.random.randn(dimension)]
    return request


def test_conn():
    c = client.Client(Address, DBName, SpaceName)
    c.init()
    c.close()


def test_add():
    c = client.Client(Address, DBName, SpaceName)
    c.init()
    for i in range(TotalNum):
        request = random_request(c.table.table_meta_info.field_meta_info)
        request['_id'] = str(i)
        res = c.add(request)
        println(res)
        println(request)
        println('The result of get is {}'.format(c.get([str(i)])))
    c.close()


def test_update():
    c = client.Client(Address, DBName, SpaceName)
    c.init()
    fields = c.table.table_meta_info.field_meta_info
    field_name = ''
    for field in fields:
        if field.data_type == dpb.FieldType.STRING:
            field_name = field.name
            break
    for i in range(TotalNum):
        request = random_request(fields)
        request['_id'] = str(i)
        request[field_name] = 'update' + request[field_name]
        c.update(request)
        println('The result of get is {}'.format(c.get([str(i)])))
    c.close


def test_get():
    c = client.Client(Address, DBName, SpaceName)
    c.init()
    for i in range(TotalNum):
        res = c.get([str(i)])
        println(res)
    c.close()


def test_search():
    c = client.Client(Address, DBName, SpaceName)
    c.init()
    
    vector_name = ''
    for k, v in c.field_map.items():
        if v == dpb.FieldType.VECTOR:
            vector_name = k
            break

    for i in range(TotalNum):
        request = random_request(c.table.table_meta_info.field_meta_info)
        request['_id'] = str(i)
        vec_field = rpb.VectorQuery(name=vector_name,
                value=np.array(request[vector_name], dtype=np.float32).tobytes(),
                min_score=0.0,
                max_score=100.0)
        
        res = c.search(10, vec_fields=[vec_field], fields=request.keys())
        println('\n', res)
    c.close()


def test_msearch():
    c = client.Client(Address, DBName, SpaceName)
    c.init()
    vector_name = ''
    for k, v in c.field_map.items():
        if v == dpb.FieldType.VECTOR:
            vector_name = k
            break
    msearch_request = rpb.MSearchRequest(head=c.head)
    for i in range(TotalNum):
        request = random_request(c.table.table_meta_info.field_meta_info)
        vec_field = rpb.VectorQuery(name=vector_name,
                value=np.array(request[vector_name], dtype=np.float32).tobytes(),
                min_score=0.0,
                max_score=1000.0)
        search_request = rpb.SearchRequest(head=c.head, 
                req_num=TotalNum,
                topN=10,
                vec_fields=[vec_field],
                fields=['_id'])
        msearch_request.search_requests.append(search_request)
    res = c.msearch(msearch_request)
    println(res)
    c.close()


def test_delete():
    c = client.Client(Address, DBName, SpaceName)
    c.init()
    for i in range(TotalNum):
        res = c.get([str(i)])
        #  println(res)
    c.close()
