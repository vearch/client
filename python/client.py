from __future__ import print_function

import sys
import grpc
import json
import logging
import numpy as np
from typing import Dict, List, Any, Union, Optional
sys.path.append('./vearchpb')
import data_model_pb2
import errors_pb2 as epb
import router_grpc_pb2 as rpb
import router_grpc_pb2_grpc as pb


class DocKeyNotFoundError(Exception):
    """The input data is not suit for table"""
    pass


class ParamError(Exception):
    """raise an Exception when param wrong"""
    pass


class RequestError(Exception):
    """raise an Exception when interface returned error"""
    pass


class Client(object):

    def __init__(self, address: str, db_name: str, space_name: str, *args, **kwargs):
        self.target = address
        self.field_map = {}
        self.db_name = db_name
        self.space_name = space_name
        self.head = rpb.RequestHead(db_name=db_name, space_name=space_name)

    def init(self):
        '''initialize channel and stub '''
        
        logging.info('Client init')
        self.channel = grpc.insecure_channel(self.target)
        self.stub = pb.RouterGRPCServiceStub(self.channel) 
        self.table = self.stub.Space(self.head)
        logging.info('The content of space[%s] in DB[%s] is %s', self.space_name, self.db_name, self.table)
        for field_info in self.table.table_meta_info.field_meta_info:
            self.field_map[field_info.name] = field_info.data_type
        logging.info('The field map is %s', self.field_map)

    def close(self):
        if self.channel:
            self.channel.close()
    
    def _make_field(self, request: Dict[str, Any]):
        doc = data_model_pb2.Document()
        for k, v in request.items():
            if k == '_id':
                doc.p_key = v
                continue
            if k not in self.field_map:
                raise DocKeyNotFoundError('key[{}] not in table.'.format(k))
            if self.field_map[k] == data_model_pb2.FieldType.INT:
                field = make_int32_field(k, v)
            elif self.field_map[k] == data_model_pb2.FieldType.LONG:
                field = make_int64_field(k, v)
            elif self.field_map[k] == data_model_pb2.FieldType.FLOAT:
                field = make_float32_field(k, v)
            elif self.field_map[k] == data_model_pb2.FieldType.DOUBLE:
                field = make_float64_field(k, v)
            elif self.field_map[k] == data_model_pb2.FieldType.STRING:
                field = make_string_field(k, v)
            elif self.field_map[k] == data_model_pb2.FieldType.VECTOR:
                field = make_float_array(k, v)
            else:
                raise NotImplementedError('The type[{}] not implemented!'.format(self.field_map[k]))
            doc.fields.append(field)

        return doc

    def _make_json(self, doc):
        res = {'_id': doc.p_key}
        for field in doc.fields:
            if field.type == data_model_pb2.FieldType.STRING:
                res[field.name] = bytes.decode(field.value)
                continue
            if field.type == data_model_pb2.FieldType.INT:
                dt = np.int32
                res[field.name] = np.frombuffer(field.value, dtype=dt)[0]
            elif field.type == data_model_pb2.FieldType.LONG:
                dt = np.int64
                res[field.name] = np.frombuffer(field.value, dtype=dt)[0]
            elif field.type == data_model_pb2.FieldType.FLOAT:
                dt = np.float32
                res[field.name] = np.frombuffer(field.value, dtype=dt)[0]
            elif field.type == data_model_pb2.FieldType.DOUBLE:
                dt = np.float64
                res[field.name] = np.frombuffer(field.value, dtype=dt)[0]
            elif field.type == data_model_pb2.FieldType.VECTOR:
                dt = np.float32
                res[field.name] = np.frombuffer(field.value, dtype=dt, offset=4)
            else:
                raise NotImplementedError('The type[{}] not implemented!'.format(field.type))

        return res

    def add(self, request: Dict[str, Any]):
        doc = self._make_field(request)
        request = rpb.AddRequest(head=self.head, doc=doc)
        response = self.stub.Add(request)
        logging.debug('The result of add is {}'.format(response))
        if response.head.err.code != epb.ErrorEnum.SUCCESS:
            raise RequestError(response.head.err.msg)
        return response.primary_key

    def update(self, request):
        doc = self._make_field(request)
        request = rpb.UpdateRequest(head=self.head, doc=doc)
        response = self.stub.Update(request)
        logging.debug('The result of update is {}'.format(response))
        if response.head.err.code != epb.ErrorEnum.SUCCESS:
            raise RequestError(response.head.err.msg)

    def get(self, request):
        request = rpb.GetRequest(head=self.head, primary_keys=request)
        response = self.stub.Get(request)
        logging.debug('The result of get is {}'.format(response))
        if response.head.err.code != epb.ErrorEnum.SUCCESS:
            raise RequestError(response.head.err.msg)
        return [self._make_json(item.doc) for item in response.items]

    def delete(self, request):
        request = rpb.DeleteRequest(head=self.head, primary_keys=request)
        response = self.stub.Delete(request)
        if response.head.err.code != epb.ErrorEnum.SUCCESS:
            raise RequestError(response.head.err.msg)
        res = {item.doc.p_key: item.err.msg for item in response.items}
        return res

    def bulk(self, request: List[Dict[str, Any]]):
        docs = [self._make_field(r) for r in request]
        request = rpb.BulkRequest(head=self.head, docs=docs)
        response = self.stub.Bulk(request)
        if response.head.err.code != epb.ErrorEnum.SUCCESS:
            raise RequestError(response.head.err.msg)
        res = {item.doc.p_key: item.err.msg for item in response.items}
        return res

    def search(self,
            top_n: int,
            vec_fields: List[Any],
            fields: List[str] = None,
            range_filters: List[Any] = None,
            term_filters: List[Any] = None,
            **kwargs):
        if top_n <= 0:
            raise ParamError("top_n is the num returned by search, needed large than zero.")
        search_request = rpb.SearchRequest(head=self.head, 
                req_num=1,
                topN=top_n,
                vec_fields=vec_fields,
                fields=fields,
                range_filters=range_filters,
                term_filters=term_filters,
                **kwargs)
        response = self.stub.Search(search_request)
        if response.head.err.code != epb.ErrorEnum.SUCCESS:
            raise RequestError(response.head.err.msg)
        return self._parse_search_result(response.results[0])

    def msearch(self, request):
        response = self.stub.MSearch(request)
        if response.head.err.code != epb.ErrorEnum.SUCCESS:
            raise RequestError(response.head.err.msg)
        return [self._parse_search_result(r) for r in response.results]

    def _parse_search_result(self, result):
        res = dict(total_hits=result.total_hits,
                max_score=result.max_score,
                max_took=result.max_took,
                max_took_id=result.max_took_id)
        res['result_items'] = []
        for item in result.result_items:
            res['result_items'].append(dict(score=item.score,
                p_key=item.p_key,
                source=json.loads(item.source)))
        return res


def make_string_field(name: str, value: str):
    field = data_model_pb2.Field(name=name,
            type=data_model_pb2.STRING,
            value=bytes(value, encoding="utf8"))
    return field


def make_int32_field(name: str, value: int):
    field = data_model_pb2.Field(name=name,
            type=data_model_pb2.INT,
            value=np.int32(value).tobytes())
    return field


def make_int64_field(name: str, value: int):
    field = data_model_pb2.Field(name=name,
            type=data_model_pb2.LONG,
            value=np.int64(value).tobytes())
    return field


def make_float32_field(name: str, value: float):
    field = data_model_pb2.Field(name=name,
            type=data_model_pb2.FLOAT,
            value=np.float32(value).tobytes())
    return field


def make_float64_field(name: str, value: float):
    field = data_model_pb2.Field(name=name,
            type=data_model_pb2.DOUBLE,
            value=np.float64(value).tobytes())
    return field


def make_float_array(name: str, value: List[float]):
    field = data_model_pb2.Field(name=name,
            type=data_model_pb2.VECTOR,
            value=np.array(value, dtype=np.float32).tobytes())
    return field


def make_binary_array(name: str, value: bytes):
    field = data_model_pb2.Field(name=name,
            type=data_model_pb2.VECTOR,
            value=value)
    return field


def make_document(p_key: str, fields):
    doc = data_model_pb2.Document(p_key=p_key, fields=fields)
    return doc
