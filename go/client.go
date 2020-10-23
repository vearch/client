package client

import (
	"bytes"
	"context"
	"errors"
	"fmt"
	"math"
	"reflect"
	"strings"
	"time"

	"github.com/vearch/client/go/utils/vbytes"
	. "github.com/vearch/client/go/vearchpb"
	"google.golang.org/grpc"
	"google.golang.org/grpc/balancer/roundrobin"
	"google.golang.org/grpc/resolver"
	"google.golang.org/grpc/resolver/manual"
)

var (
	vearchScheme = "vearch-lb"
	ErrResponse  = errors.New("Response or Response.Head or Response.Head.Err can not be nil")
)

const (
	String      = "string"
	Int32       = "int32"
	Int64       = "int64"
	Float32     = "float32"
	Float64     = "float64"
	ArrayFloat  = "[]float32"
	ArrayUint8  = "[]uint8"
	ArrayString = "[]string"
)

type vearchClient struct {
	Client *grpc.ClientConn
	Vc     RouterGRPCServiceClient
	head   *RequestHead
}

func New(dbName, spaceName string, addrs ...string) (*vearchClient, error) {
	lisAddrs := make([]resolver.Address, len(addrs))
	for _, addr := range addrs {
		lisAddrs = append(lisAddrs, resolver.Address{Addr: addr})
	}
	r := manual.NewBuilderWithScheme(vearchScheme)
	r.InitialState(resolver.State{Addresses: lisAddrs})
	ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()
	client, err := grpc.DialContext(ctx, vearchScheme+":///",
		grpc.WithInsecure(),
		grpc.WithDefaultServiceConfig(fmt.Sprintf(`{"LoadBalancingPolicy": "%s", "RetryPolicy":	{"MaxAttempts":2, "InitialBackoff": "0.1s", "MaxBackoff": "1s", "BackoffMultiplier": 2.0, "RetryableStatusCodes": ["UNAVAILABLE"]}}`, roundrobin.Name)),
		grpc.WithResolvers(r),
		grpc.WithBlock())
	if err != nil {
		return nil, fmt.Errorf("Dial failed. Err: [%v]", err)
	}
	return new(client, dbName, spaceName), nil
}

func new(client *grpc.ClientConn, dbName, spaceName string) *vearchClient {
	vc := NewRouterGRPCServiceClient(client)
	head := &RequestHead{DbName: dbName, SpaceName: spaceName}
	return &vearchClient{Client: client, Vc: vc, head: head}
}

func (vc *vearchClient) SetClientType(clientType string) {
	vc.head.ClientType = clientType
}

func (vc *vearchClient) SetParams(params map[string]string) {
	vc.head.Params = params
}

func (vc *vearchClient) Close() {
	if vc.Client != nil {
		vc.Client.Close()
	}
}

type responseV interface {
	GetHead() *ResponseHead
}

func judgeResponse(res responseV, err error) error {
	if err != nil {
		return fmt.Errorf("request failed, %s", err.Error())
	}
	head := res.GetHead()
	if head == nil || head.Err == nil {
		return ErrResponse
	}
	if head.Err.Code != ErrorEnum_SUCCESS {
		return fmt.Errorf("request return failed, %s", head.Err.Msg)
	}
	return nil
}

func (vc *vearchClient) Add(ctx context.Context, doc *Document, opts ...grpc.CallOption) (string, error) {
	requestArgs := &AddRequest{Head: vc.head, Doc: doc}
	res, err := vc.Vc.Add(ctx, requestArgs, opts...)
	if err := judgeResponse(res, err); err != nil {
		return "", err
	}
	return res.PrimaryKey, nil
}

func (vc *vearchClient) Update(ctx context.Context, doc *Document, opts ...grpc.CallOption) error {
	requestArgs := &UpdateRequest{Head: vc.head, Doc: doc}
	res, err := vc.Vc.Update(ctx, requestArgs, opts...)
	if err := judgeResponse(res, err); err != nil {
		return err
	}
	return nil
}

func (vc *vearchClient) Get(ctx context.Context, keys []string, opts ...grpc.CallOption) ([]*Item, error) {
	requestArgs := &GetRequest{Head: vc.head, PrimaryKeys: keys}
	res, err := vc.Vc.Get(ctx, requestArgs, opts...)
	if err := judgeResponse(res, err); err != nil {
		return nil, err
	}
	if res.Items == nil || len(res.Items) != len(keys) {
		return nil, fmt.Errorf("request result wrong, the length of result is nil or not equal input[%d]", len(keys))
	}
	return res.GetItems(), nil
}

func (vc *vearchClient) Delete(ctx context.Context, keys []string, opts ...grpc.CallOption) ([]*Item, error) {
	requestArgs := &DeleteRequest{Head: vc.head, PrimaryKeys: keys}
	res, err := vc.Vc.Delete(ctx, requestArgs, opts...)
	if err := judgeResponse(res, err); err != nil {
		return nil, err
	}
	if res.Items == nil || len(res.Items) != len(keys) {
		return nil, fmt.Errorf("request result wrong, the length of result is nil or not equal input[%d]", len(keys))
	}
	return res.GetItems(), nil
}

func (vc *vearchClient) Bulk(ctx context.Context, docs []*Document, opts ...grpc.CallOption) ([]*Item, error) {
	requestArgs := &BulkRequest{Head: vc.head, Docs: docs}
	res, err := vc.Vc.Bulk(ctx, requestArgs, opts...)
	if err := judgeResponse(res, err); err != nil {
		return nil, err
	}
	if res.Items == nil || len(res.Items) != len(docs) {
		return nil, fmt.Errorf("request result wrong, the length of result is nil or not equal input[%d]", len(docs))
	}
	return res.Items, nil
}

func (vc *vearchClient) Search(ctx context.Context, in *SearchRequest, opts ...grpc.CallOption) (*SearchResult, error) {
	in.Head = vc.head
	res, err := vc.Vc.Search(ctx, in, opts...)
	if err := judgeResponse(res, err); err != nil {
		return nil, err
	}
	return res.GetResults()[0], nil
}

func (vc *vearchClient) MSearch(ctx context.Context, in *MSearchRequest, opts ...grpc.CallOption) ([]*SearchResult, error) {
	in.Head = vc.head
	for _, q := range in.SearchRequests {
		q.Head = vc.head
	}
	res, err := vc.Vc.MSearch(ctx, in, opts...)
	if err := judgeResponse(res, err); err != nil {
		return nil, err
	}
	return res.GetResults(), nil
}

/******************util***************************/

func MakeDocument(doc interface{}) (*Document, error) {
	var (
		err        error  = nil
		primaryKey string = ""
	)
	docType := reflect.TypeOf(doc).Elem()
	docValue := reflect.ValueOf(doc).Elem()
	fields := make([]*Field, 0)
	for i := 0; i < docType.NumField(); i++ {
		var field *Field
		f := docType.Field(i)
		name := f.Tag.Get("json")
		if name == "_id" {
			primaryKey = docValue.Field(i).String()
			continue
		}
		switch f.Type.String() {
		case String:
			field = MakeStringField(name, docValue.Field(i).String())
		case Int32:
			field = MakeInt32Field(name, int32(docValue.Field(i).Int()))
		case Int64:
			field = MakeInt64Field(name, docValue.Field(i).Int())
		case Float32:
			field = MakeFloat32Field(name, float32(docValue.Field(i).Float()))
		case Float64:
			field = MakeFloat64Field(name, docValue.Field(i).Float())
		case ArrayFloat:
			field, err = MakeFloatVectorField(name, docValue.Field(i).Interface().([]float32))
		case ArrayUint8:
			field, err = MakeBinaryVectorField(name, docValue.Field(i).Interface().([]uint8))
		case ArrayString:
			field, err = MakeStringVectorField(name, docValue.Field(i).Interface().([]string))
		default:
			return nil, fmt.Errorf("type[%s] not found", f.Type.String())
		}
		fields = append(fields, field)
	}
	document := &Document{PKey: primaryKey, Fields: fields}
	return document, err
}

func DocToStruct(doc *Document, res interface{}) (err error) {
	resType := reflect.TypeOf(res).Elem()
	resValue := reflect.ValueOf(res).Elem()
	for i := 0; i < resType.NumField(); i++ {
		f := resType.Field(i)
		name := f.Tag.Get("json")
		var value []byte
		var flag bool = false
		if name == "_id" {
			resValue.Field(i).SetString(doc.PKey)
			continue
		}
		for _, field := range doc.Fields {
			if name == field.Name {
				value = field.Value
				flag = true
				break
			}
		}
		if !flag {
			return fmt.Errorf("The field name[%s] of res not found.", name)
		}
		switch f.Type.String() {
		case String:
			resValue.Field(i).SetString(vbytes.ByteToString(value))
		case Int32:
			fv := reflect.ValueOf(vbytes.Bytes2Int32(value))
			resValue.Field(i).Set(fv)
		case Int64:
			fv := reflect.ValueOf(vbytes.Bytes2Int(value))
			resValue.Field(i).Set(fv)
		case Float32:
			fv := reflect.ValueOf(vbytes.ByteToFloat32(value))
			resValue.Field(i).Set(fv)
		case Float64:
			fv := reflect.ValueOf(vbytes.ByteToFloat64(value))
			resValue.Field(i).Set(fv)
		case ArrayFloat:
			vv, _, _ := vbytes.ByteToVector(value)
			fv := reflect.ValueOf(vv)
			resValue.Field(i).Set(fv)
		case ArrayUint8:
			vv, _ := vbytes.ByteToUInt8Array(value)
			fv := reflect.ValueOf(vv)
			resValue.Field(i).Set(fv)
		case ArrayString:
			as := string(value)
			fv := reflect.ValueOf(strings.Split(as, string([]byte{'\001'})))
			resValue.Field(i).Set(fv)
		default:
			return fmt.Errorf("type[%s] not found", f.Type.String())
		}
	}
	return err
}

func printDoc(doc *Document) {
	fmt.Println(doc.PKey)
	for _, field := range doc.Fields {
		switch field.Type {
		case FieldType_STRING:
			fmt.Println(vbytes.ByteToString(field.Value))
		case FieldType_INT:
			fmt.Println(vbytes.BytesToInt32(field.Value))
		case FieldType_VECTOR:
			fmt.Println(vbytes.ByteToFloat32Array(field.Value))
		}
	}
}

func MakeInt32Field(key string, value int32) *Field {
	bytes := vbytes.Int32ToByte(value)
	field := &Field{Name: key, Type: FieldType_INT, Value: bytes}
	return field
}

func MakeInt64Field(key string, value int64) *Field {
	bytes := vbytes.Int64ToByte(value)
	field := &Field{Name: key, Type: FieldType_LONG, Value: bytes}
	return field
}

func MakeStringField(key string, value string) *Field {
	bytes := vbytes.StringToByte(value)
	field := &Field{Name: key, Type: FieldType_STRING, Value: bytes}
	return field
}

func MakeFloat32Field(key string, value float32) *Field {
	bytes := vbytes.Float32ToByte(value)
	field := &Field{Name: key, Type: FieldType_FLOAT, Value: bytes}
	return field
}

func MakeFloat64Field(key string, value float64) *Field {
	bytes := vbytes.Float64ToByte(value)
	field := &Field{Name: key, Type: FieldType_DOUBLE, Value: bytes}
	return field
}

func MakeFloatVectorField(key string, value []float32) (*Field, error) {
	bytes, err := vbytes.VectorToByte(value, "")
	if err != nil {
		return nil, err
	}
	field := &Field{Name: key, Type: FieldType_VECTOR, Value: bytes}
	return field, nil
}

func MakeBinaryVectorField(key string, value []uint8) (*Field, error) {
	bytes, err := vbytes.VectorBinaryToByte(value, "")
	if err != nil {
		return nil, err
	}
	field := &Field{Name: key, Type: FieldType_VECTOR, Value: bytes}
	return field, nil
}

func MakeStringVectorField(key string, value []string) (*Field, error) {
	buffer := bytes.Buffer{}
	for i, s := range value {
		buffer.Write(vbytes.StringToByte(s))
		if i < len(value)-1 {
			buffer.WriteRune('\001')
		}
	}
	field := &Field{Name: key, Type: FieldType_STRING, Value: buffer.Bytes()}
	return field, nil
}

func Normalization(feature []float32) error {
	var sum float32

	for _, v := range feature {
		sum += v * v
	}
	if math.IsNaN(float64(sum)) || math.IsInf(float64(sum), 0) || sum == 0 {
		return fmt.Errorf("normalization err , sum value is:[%v]", sum)
	}

	sum = float32(math.Sqrt(float64(sum)))

	for i, v := range feature {
		feature[i] = v / sum
	}

	return nil
}
