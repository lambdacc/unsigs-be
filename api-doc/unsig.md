### Get an unsig by id  

- Request

`Endpoint : /api/v1/unsigs`
```
    [source,http,options="nowrap"]
    ----
    GET /api/v1/unsigs/unsig09523 HTTP/1.1
    Content-Type: application/json
    Accept: application/json
    Host: localhost:8080
    
    ----

```

- Response

```
    [source,http,options="nowrap"]
    ----
    HTTP/1.1 200 OK
    Vary: Origin
    Vary: Access-Control-Request-Method
    Vary: Access-Control-Request-Headers
    Content-Type: application/json
    Content-Length: 838
    
    {
      "unsigId" : "unsig09523",
      "details" : {
        "files" : [ {
          "src" : "ipfs://QmQHFfDt7cAy2xuFhj85GHuENjb7ZhCQ3avyxuW2bkdBJ7",
          "mediatype" : "image/png"
        } ],
        "image" : "ipfs://QmZVfjXKHTiuBPqBdBQyDZU2EbHuGQudD9rcMRdwjqtNN1",
        "title" : "unsig_09523",
        "series" : "unsigned_algorithms",
        "unsigs" : {
          "index" : 9523,
          "num_props" : 4,
          "properties" : {
            "colors" : [ "Green", "Red", "Blue", "Blue" ],
            "rotations" : [ "0", "0", "180", "270" ],
            "multipliers" : [ "1", "2", "4", "4" ],
            "distributions" : [ "CDF", "CDF", "CDF", "CDF" ]
          }
        },
        "source_key" : [ "721", "0e14267a8020229adc0184dd25fa3174c3f7d6caadcb4425c70e7c04", "unsig00000", "files", "code" ],
        "source_tx_id" : "e4a90da18935e73f7fd6ffaa688b35b011a1a8a710b47bdb5d7103a05afc0197"
      }
    }
    ----


```

### Paginated listing of unsigs

- Request

`Endpoint : /api/v1/offers`
```
    [source,http,options="nowrap"]
    ----
    GET /api/v1/unsigs HTTP/1.1
    Content-Type: application/json
    Accept: application/json
    Host: localhost:8080
    
    ----
```

- Response

```
    [source,http,options="nowrap"]
    ----
    HTTP/1.1 200 OK
    Vary: Origin
    Vary: Access-Control-Request-Method
    Vary: Access-Control-Request-Headers
    Content-Type: application/json
    Content-Length: 9099
    
    {
      "hasNextPage" : true,
      "totalPages" : 250,
      "listSize" : 10,
      "resultList" : [ {
        "unisgId" : "unsig09523",
        "details" : {
          "files" : [ {
            "src" : "ipfs://QmQHFfDt7cAy2xuFhj85GHuENjb7ZhCQ3avyxuW2bkdBJ7",
            "mediatype" : "image/png"
          } ],
          "image" : "ipfs://QmZVfjXKHTiuBPqBdBQyDZU2EbHuGQudD9rcMRdwjqtNN1",
          "title" : "unsig_09523",
          "series" : "unsigned_algorithms",
          "unsigs" : {
            "index" : 9523,
            "num_props" : 4,
            "properties" : {
              "colors" : [ "Green", "Red", "Blue", "Blue" ],
              "rotations" : [ "0", "0", "180", "270" ],
              "multipliers" : [ "1", "2", "4", "4" ],
              "distributions" : [ "CDF", "CDF", "CDF", "CDF" ]
            }
          },
          "source_key" : [ "721", "0e14267a8020229adc0184dd25fa3174c3f7d6caadcb4425c70e7c04", "unsig00000", "files", "code" ],
          "source_tx_id" : "e4a90da18935e73f7fd6ffaa688b35b011a1a8a710b47bdb5d7103a05afc0197"
        }
      }, {
        "unisgId" : "unsig02977",
        "details" : {
          "files" : [ {
            "src" : "ipfs://QmPV3tPKTDqJjhm4ir52S5xvgnpECiHrJ8cm9fVX8HSPmC",
            "mediatype" : "image/png"
          } ],
          "image" : "ipfs://QmYoZeL4sV4iKwzUD8ybVQjtedZ5ouQwLjzRxn8k3yedKc",
          "title" : "unsig_02977",
          "series" : "unsigned_algorithms",
          "unsigs" : {
            "index" : 2977,
            "num_props" : 4,
            "properties" : {
              "colors" : [ "Blue", "Red", "Red", "Green" ],
              "rotations" : [ "0", "270", "180", "90" ],
              "multipliers" : [ "0.5", "2", "4", "4" ],
              "distributions" : [ "CDF", "CDF", "CDF", "CDF" ]
            }
          },
          "source_key" : [ "721", "0e14267a8020229adc0184dd25fa3174c3f7d6caadcb4425c70e7c04", "unsig00000", "files", "code" ],
          "source_tx_id" : "e4a90da18935e73f7fd6ffaa688b35b011a1a8a710b47bdb5d7103a05afc0197"
        }
      } ]
    }
    ----


```