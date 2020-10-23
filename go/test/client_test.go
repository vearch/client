package main

import (
	"context"
	"fmt"
	"math"
	"math/rand"
	"os"
	"strconv"
	"testing"
	"time"

	client "github.com/vearch/client/go"
	"github.com/vearch/client/go/vearchpb"
)

var (
	randNum    = 0
	batchNum   = 50
	instertNum = 100
	address    = "127.0.0.1:9002"
	dbName     = "ts_db"
	spaceName  = "ts_space"
	testCase   = make([]*TestDoc, 0)
)

func TestMain(m *testing.M) {
	rand.Seed(time.Now().Unix())
	randNum = instertNum * rand.Intn(10000)
	testCase = makeTestDoc(instertNum)
	os.Exit(m.Run())
}

// TestDoc The struct of test doc
type TestDoc struct {
	// ID the primary key, default null str.
	ID        string    `json:"_id"`
	Sku       string    `json:"sku"`
	Url       string    `json:"url"`
	Cid1      int32     `json:"cid1"`
	StringTag []string  `json:"string_tag"`
	Vector    []float32 `json:"vector"`
}

// equal Determine if two test dcc are equal
func (t *TestDoc) equal(s *TestDoc) bool {
	if t == nil || s == nil {
		return false
	}
	if t.ID != s.ID || t.Sku != s.Sku || t.Url != s.Url || t.Cid1 != s.Cid1 {
		return false
	}
	if t.StringTag == nil || s.StringTag == nil || len(t.StringTag) != len(s.StringTag) {
		return false
	}
	for i := range t.StringTag {
		if t.StringTag[i] != s.StringTag[i] {
			return false
		}
	}

	if t.Vector == nil || s.Vector == nil || len(t.Vector) != len(s.Vector) {
		return false
	}
	for i := range t.Vector {
		if t.Vector[i] != s.Vector[i] {
			return false
		}
	}
	return true
}

// makeTestDoc Randomly generate num docs
func makeTestDoc(num int) []*TestDoc {
	docs := make([]*TestDoc, num)
	for i := range docs {
		doc := &TestDoc{}
		key := strconv.FormatInt(int64(i+randNum), 10)
		doc.ID = key
		doc.Sku = fmt.Sprintf("sku%d", i)
		doc.Url = fmt.Sprintf("url%d", i)
		doc.StringTag = []string{doc.Sku, doc.Url}
		doc.Cid1 = int32(i)
		doc.Vector = make([]float32, 128)
		for j := range doc.Vector {
			doc.Vector[j] = float32(math.Abs(float64(rand.Float32())))
		}
		client.Normalization(doc.Vector)
		docs[i] = doc
	}
	return docs
}

// TestAdd test create doc
func TestAdd(t *testing.T) {
	vc, err := client.New(dbName, spaceName, address)
	if err != nil {
		t.Fatalf("conn failed, %s", err.Error())
	}
	defer vc.Close()

	ctx := context.Background()
	for _, c := range testCase {
		doc, err := client.MakeDocument(c)
		if err != nil {
			t.Fatalf("MakeDocument failed, %s", err.Error())
		}

		res, err := vc.Add(ctx, doc)
		if err != nil {
			t.Fatalf("Add failed, case: [%v], res: [%v], err: [%v]", c, res, err)
		} else {
			items, err := vc.Get(ctx, []string{c.ID})
			ct := &TestDoc{}
			client.DocToStruct(items[0].GetDoc(), ct)
			if err != nil || !c.equal(ct) {
				t.Fatalf("Add key[%s] failed, %v, %v", c.ID, c, ct)
			} else {
				// t.Logf("Add key [%s] success", res)
			}
		}
	}
}

// TestUpdate test update doc
func TestUpdate(t *testing.T) {
	vc, err := client.New(dbName, spaceName, address)
	if err != nil {
		t.Fatalf("conn failed, %s", err.Error())
	}
	defer vc.Close()

	ctx := context.Background()
	for _, c := range testCase {
		c.Sku = fmt.Sprintf("update-%s", c.Sku)
		doc, err := client.MakeDocument(c)
		if err != nil {
			t.Fatalf("MakeDocument failed, %s", err.Error())
		}
		err = vc.Update(ctx, doc)
		if err != nil {
			t.Fatalf("Update failed, case: [%v], err: [%v]", c, err)
		}
		items, err := vc.Get(ctx, []string{c.ID})
		ct := &TestDoc{}
		client.DocToStruct(items[0].GetDoc(), ct)
		if err != nil || !c.equal(ct) {
			t.Fatalf("update key[%s] failed, %v, %v", c.ID, c, ct)
		}

	}
}

// TestGetExist test Get doc
func TestGetExist(t *testing.T) {
	testCase := makeTestDoc(instertNum)
	vc, err := client.New(dbName, spaceName, address)
	if err != nil {
		t.Fatalf("conn failed, %s", err.Error())
	}
	defer vc.Close()

	ctx := context.Background()
	for i := range testCase {
		key := strconv.FormatInt(int64(i+randNum), 10)
		if err != nil {
			t.Fatalf("MakeDocument failed, %s", err.Error())
		}

		items, err := vc.Get(ctx, []string{key})
		if err != nil {
			t.Fatalf("Get failed, case: [%v], err: [%v]", key, err)
		} else {
			for _, item := range items {
				if item.GetErr().GetCode() != vearchpb.ErrorEnum_SUCCESS {
					t.Fatalf("key[%s] should be exist, but not found, item: %v", key, item)
				}
				doc := &TestDoc{}
				err = client.DocToStruct(item.Doc, doc)
				// t.Logf("key: [%s], doc: %v, err: %v", key, doc, err)
			}
		}
	}
}

// TestGetNoExist test Get doc
func TestGetNoExist(t *testing.T) {
	vc, err := client.New(dbName, spaceName, address)
	if err != nil {
		t.Fatalf("conn failed, %s", err.Error())
	}
	defer vc.Close()

	ctx := context.Background()

	for i := range testCase {
		key := strconv.FormatInt(int64(instertNum+i+randNum), 10)
		items, err := vc.Get(ctx, []string{key})
		if err != nil {
			t.Fatalf("Get failed, case: [%v] , err: [%v]", key, err)
		} else {
			for _, item := range items {
				if item.Err.Code != vearchpb.ErrorEnum_DOCUMENT_NOT_EXIST {
					t.Fatalf("key[%s] should be not exist, but found, item: %v", key, item)
				}
			}
		}
	}
}

func TestSearch(t *testing.T) {
	vc, err := client.New(dbName, spaceName, address)
	if err != nil {
		t.Fatalf("conn failed, %s", err.Error())
	}
	defer vc.Close()

	ctx := context.Background()

	queries := makeSearchRequests(testCase)
	num := math.Min(1000.0, float64(instertNum))
	for i := 0; i < int(num); i++ {
		query := queries[i]
		resp, err := vc.Search(ctx, query)
		if err != nil {
			t.Errorf("request failed, response: %v, err: %s", resp, err.Error())
		} else {
			// t.Logf("request success, response: %s", resp)
		}
	}
}

func TestMSearch(t *testing.T) {
	testCase := makeTestDoc(instertNum)
	vc, err := client.New(dbName, spaceName, address)
	if err != nil {
		t.Fatalf("conn failed, %s", err.Error())
	}
	defer vc.Close()

	ctx := context.Background()

	queries := makeSearchRequests(testCase)
	for i := 0; i < int(instertNum/batchNum); i++ {
		begin, end := i*batchNum, (i+1)*batchNum
		query := &vearchpb.MSearchRequest{}
		query.SearchRequests = queries[begin:end]

		resp, err := vc.MSearch(ctx, query)
		if err != nil {
			t.Errorf("request failed, response: %v, err: %s", resp, err.Error())
		} else {
			// t.Logf("request success, response: %s", resp)
		}
	}
}

func makeSearchRequests(docs []*TestDoc) []*vearchpb.SearchRequest {
	queries := make([]*vearchpb.SearchRequest, 0)
	for _, d := range docs {
		doc, _ := client.MakeDocument(d)
		vectorQuery := &vearchpb.VectorQuery{}
		for _, field := range doc.Fields {
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
		query.Fields = []string{"sku", "url"}

		// range filter
		// rf := &vearchpb.RangeFilter{}
		// query.RangeFilters = []*vearchpb.RangeFilter{rf}
		// query.IsBruteSearch = 1
		// query.OnlineLogLevel = "debug"
		// query.HasRank = false
		// query.IsVectorValue = false
		// query.ParallelBasedOnQuery = false
		// query.L2Sqrt = false
		// query.IvfFlat = false
		// query.RetrievalParams = `{"nprobe" : 10, "metric_type" : "L2"}`
		query.RetrievalParams = `{"nprobe" : 10, "metric_type" : "InnerProduct"}`
		query.MultiVectorRank = 1
		queries = append(queries, query)
	}
	return queries
}

// TestDelete test Delete doc
func TestDeleteExist(t *testing.T) {
	vc, err := client.New(dbName, spaceName, address)
	if err != nil {
		t.Fatalf("conn failed, %s", err.Error())
	}
	defer vc.Close()

	ctx := context.Background()
	for i := range testCase {
		key := strconv.FormatInt(int64(i+randNum), 10)
		items, err := vc.Delete(ctx, []string{key})
		// fmt.Println(key)
		if err != nil {
			t.Fatalf("Delete failed, case: [%v], err: [%v]", key, err)
		} else {
			for _, item := range items {
				if item.GetErr().GetCode() != vearchpb.ErrorEnum_SUCCESS {
					t.Fatalf("key[%s] should be exist, but not found, item: %v", key, item)
				}
			}
		}
		items, err = vc.Get(ctx, []string{key})
		if err != nil {
			t.Fatalf("Delete Get failed, case: [%v], err: [%v]", key, err)
		} else {
			for _, item := range items {
				if item.GetErr().GetCode() != vearchpb.ErrorEnum_DOCUMENT_NOT_EXIST {
					t.Fatalf("key[%s] is delete, but found, item: %v", key, item)
				}
			}
		}
	}
}

// TestDelete test Delete doc
func TestDeleteNoExist(t *testing.T) {
	vc, err := client.New(dbName, spaceName, address)
	if err != nil {
		t.Fatalf("conn failed, %s", err.Error())
	}
	defer vc.Close()

	ctx := context.Background()
	for i := range testCase {
		key := strconv.FormatInt(int64(i+randNum), 10)
		// fmt.Println(key)
		items, err := vc.Delete(ctx, []string{key})
		if err != nil {
			t.Fatalf("Delete failed, case: [%v], err: [%v]", key, err)
		} else {
			// TODO: duplicate delete a same key return 200
			for _, item := range items {
				if item.GetErr().GetCode() != vearchpb.ErrorEnum_DOCUMENT_NOT_EXIST {
					t.Fatalf("key[%s] should not be exist, but found, item: %v", key, item)
				} else {
				}
			}
		}
	}
}

// TestBulk test Bulk doc
func TestBulk(t *testing.T) {
	vc, err := client.New(dbName, spaceName, address)
	if err != nil {
		t.Fatalf("conn failed, %s", err.Error())
	}
	defer vc.Close()

	ctx := context.Background()
	docs := make([]*vearchpb.Document, batchNum)
	for i, c := range testCase {
		c.ID = strconv.FormatInt(int64(instertNum+i+randNum), 10)
		doc, err := client.MakeDocument(c)
		if err != nil {
			t.Fatalf("MakeDocument failed, %s", err.Error())
		}
		docs[i%batchNum] = doc
		if i != 0 && i%batchNum == 0 {
			res, err := vc.Bulk(ctx, docs)
			if err != nil {
				t.Fatalf("Bulk failed, case: [%v], res: [%v], err: [%v]", c, res, err)
			} else {
				for _, item := range res {
					if item.GetErr().GetCode() != vearchpb.ErrorEnum_SUCCESS {
						t.Fatalf("Bulk response failed, %v", item)
					}
				}
			}

		}
	}
}

// TestGetBatch test get batch doc
func TestGetBatch(t *testing.T) {
	vc, err := client.New(dbName, spaceName, address)
	if err != nil {
		t.Fatalf("conn failed, %s", err.Error())
	}
	defer vc.Close()

	ctx := context.Background()
	keys := make([]string, batchNum)
	for i, c := range testCase {
		key := strconv.FormatInt(int64(instertNum+i+randNum), 10)
		keys[i%batchNum] = key
		if i != 0 && i%batchNum == 0 {
			res, err := vc.Get(ctx, keys)
			if err != nil {
				t.Fatalf("Delete batch failed, case: [%v], res: [%v], err: [%v]", c, res, err)
			} else {
				for _, item := range res {
					if item.GetErr().GetCode() != vearchpb.ErrorEnum_SUCCESS {
						t.Fatalf("delete batch response failed, %v", item)
					}
				}
			}

		}
	}
}

// TestDeleteBatch test Delete batch doc
func TestDeleteBatch(t *testing.T) {
	vc, err := client.New(dbName, spaceName, address)
	if err != nil {
		t.Fatalf("conn failed, %s", err.Error())
	}
	defer vc.Close()

	ctx := context.Background()
	keys := make([]string, batchNum)
	for i, c := range testCase {
		key := strconv.FormatInt(int64(instertNum+i+randNum), 10)
		keys[i%batchNum] = key
		if i != 0 && i%batchNum == 0 {
			res, err := vc.Delete(ctx, keys)
			if err != nil {
				t.Fatalf("Delete batch failed, case: [%v], res: [%v], err: [%v]", c, res, err)
			} else {
				for _, item := range res {
					if item.GetErr().GetCode() != vearchpb.ErrorEnum_SUCCESS {
						t.Fatalf("delete batch response failed, %v", item)
					}
				}
			}

		}
	}
}
