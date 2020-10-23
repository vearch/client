# Vearch Python Client



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

## Run
```bash
pip install -r requirements.txt
bash generate_python.sh
```

## Demo
[test_client.py](./test_client.py)
