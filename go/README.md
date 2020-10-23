# Vearch Go Client


> MASTER  address is [127.0.0.1](http://127.0.0.1:8817/)， MASTER  api_port is [8817](http://127.0.0.1:8817/)，ROUTER address is [127.0.0.1](http://127.0.0.1:8817/)，ROUTER port is 9001， ROUTER rpc_port is 9002.


## Create DB

```shell
# request
curl -H "content-type: application/json" -XPUT -d'
{
	"name":"ts_db"
}
' {{MASTER}}/db/_create
```



## Create Space

```shell
curl -H "content-type: application/json" -XPUT -d'
{
	"name": "ts_space",
	"partition_num": 1,
	"replica_num": 1,
	"engine": {
		"name": "gamma",
		"index_size": 10000,
		"id_type": "string",
		"retrieval_type": "IVFPQ",
		"retrieval_param": {
			"metric_type":"InnerProduct",
			"ncentroids": 2048,
			"nsubvector": 32
		}
	},
	"properties": {
		"field1": {
			"type": "string",
			"index": true
		},
		"field2": {
			"type": "string",
			"index": true
		},
		"field3": {
			"type": "integer",
			"index": true
		},
		"vector": {
			"type": "vector",
			"dimension": 128,
			"store_type": "MemoryOnly",
			"format": "normalization"
		}
	}
}
' {{MASTER}}/space/test_vector_db/_create
```

## Demo
```go

var (
	address   = "127.0.0.1:9002"
	dbName    = "ts_db"
	spaceName = "ts_space"
)

// TestDoc The struct of test doc
type TestDoc struct {
	// ID the primary key, default null str.
	ID     string    `json:"_id"`
	Field1 string    `json:"field1"`
	Field2 string    `json:"field2"`
	Field3 int32     `json:"field3"`
	Vector []float32 `json:"vector"`
}

func test() {
	// init a doc
	doc := &TestDoc{}
	doc.ID = "1"
	doc.Field1 = "field1"
	doc.Field2 = "field2"
	doc.Field3 = 123
	doc.Vector = make([]float32, 128)
	for j := range doc.Vector {
		doc.Vector[j] = float32(math.Abs(float64(rand.Float32())))
	}
	client.Normalization(doc.Vector)

	//make a connection
	vc, err := client.New(dbName, spaceName, address)
	if err != nil {
		panic(fmt.Sprintf("conn failed, %s", err.Error()))
	}
	defer vc.Close()

	// change a doc to pb
	d, err := client.MakeDocument(doc)
	if err != nil {
		panic(fmt.Sprintf("MakeDocument failed, %s", err.Error()))
	}

	// add a doc
	ctx := context.Background()
	res, err := vc.Add(ctx, d)
	if err != nil {
		panic(fmt.Sprintf("Add failed, case: [%v], res: [%v], err: [%v]", doc, res, err))
	}

	// Get a doc by id
	items, err := vc.Get(ctx, []string{doc.ID})
	if err != nil {
		panic(fmt.Sprintf("Get failed, case: [%v] , err: [%v]", doc.ID, err))
	}
	getDoc := &TestDoc{}
	err = client.DocToStruct(items[0].Doc, getDoc)

	// Search result by feature
	vectorQuery := &vearchpb.VectorQuery{}
	for _, field := range d.Fields {
		if field.Type == vearchpb.FieldType_VECTOR {
			vectorQuery.Name = field.Name
			vectorQuery.Value = field.Value
			vectorQuery.MinScore = 0.0
			vectorQuery.MaxScore = 1.0
			vectorQuery.Boost = 1
			break
		}
	}
	// query
	query := &vearchpb.SearchRequest{}
	query.ReqNum = 1
	query.TopN = 10
	query.VecFields = []*vearchpb.VectorQuery{vectorQuery}
	query.Fields = []string{"field1", "field2"}

	query.RetrievalParams = `{"nprobe" : 10, "metric_type" : "InnerProduct"}`
	query.MultiVectorRank = 1

	resp, err := vc.Search(ctx, query)
	if err != nil {
		panic(fmt.Sprintf("request failed, response: %v, err: %s", resp, err.Error()))
	}

	// Delete a doc by id
	items, err = vc.Delete(ctx, []string{doc.ID})
	if err != nil {
		panic(fmt.Sprintf("delete failed, case: [%v] , err: [%v]", doc.ID, err))
	}
}
```

