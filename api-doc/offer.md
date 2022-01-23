### Create or update offer

- Request

`Endpoint : /api/v1/offers`

```
    [source,http,options="nowrap"]
    ----
    PUT /api/v1/offers HTTP/1.1
    Content-Type: application/json
    Accept: application/json
    Content-Length: 235
    Host: localhost:8080
    
    {
      "unsigId" : "unsig00015",
      "owner" : "bfbb741c-47c9-4caf-94cf-1ad58a41c6b7",
      "amount" : 1020,
      "txHash" : "40996f2a-04ff-47bc-92d4-006e61aa7109",
      "datumHash" : "22482892-7a50-4ede-8934-ccb2a40f86ec",
      "txIndex" : 467518258
    }
    ----
```

- Response

```
    [source,http,options="nowrap"]
    ----
    HTTP/1.1 202 Accepted
    Vary: Origin
    Vary: Access-Control-Request-Method
    Vary: Access-Control-Request-Headers
    Content-Type: application/json
    Content-Length: 251
    
    {
      "id" : 31120,
      "unsigId" : "unsig00015",
      "owner" : "bfbb741c-47c9-4caf-94cf-1ad58a41c6b7",
      "amount" : 1020,
      "txHash" : "40996f2a-04ff-47bc-92d4-006e61aa7109",
      "datumHash" : "22482892-7a50-4ede-8934-ccb2a40f86ec",
      "txIndex" : 467518258
    }
    ----


```

### Paginated listing of offers

- Request

`Endpoint : /api/v1/offers`

```
    [source,http,options="nowrap"]
    ----
    GET /api/v1/offers HTTP/1.1
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
    Content-Length: 1126
    
    {
      "hasNextPage" : false,
      "totalPages" : 1,
      "listSize" : 2,
      "resultList" : [ {
        "txHash" : "1b6d311f-0990-4c0c-b425-84b81923f305",
        "txIndex" : 1527416338,
        "datumHash" : "e4ed706c-0767-4bd3-9962-6016e7cf45f9",
        "unsigId" : "unsig00019",
        "owner" : "e8dcff5d-c94e-4821-9bd4-8c3491c7e278",
        "amount" : 1020,
        "details" : {
          "index" : 19,
          "num_props" : 2,
          "properties" : {
            "multipliers" : [ 0.5, 1 ],
            "colors" : [ "Blue", "Red" ],
            "distributions" : [ "CDF", "CDF" ],
            "rotations" : [ 0, 0 ]
          },
          "unsigId" : "unsig00019"
        }
      }, {
        "txHash" : "98395c83-a765-488b-a614-a120219b609f",
        "txIndex" : 484243479,
        "datumHash" : "8b760570-49d9-4832-bd94-5e72dd75e191",
        "unsigId" : "unsig00015",
        "owner" : "b6eff8f1-072e-4c4e-ae1f-4c492c65e4f6",
        "amount" : 8888,
        "details" : {
          "index" : 15,
          "num_props" : 1,
          "properties" : {
            "multipliers" : [ 4 ],
            "colors" : [ "Red" ],
            "distributions" : [ "CDF" ],
            "rotations" : [ 0 ]
          },
          "unsigId" : "unsig00015"
        }
      } ]
    }
    ----
```

### Delete an offer

- Request

`Endpoint : /api/v1/offers`

```
    [source,http,options="nowrap"]
    ----
    DELETE /api/v1/offers HTTP/1.1
    Content-Type: application/json
    Accept: application/json
    Content-Length: 102
    Host: localhost:8080
    
    {
      "unsigId" : "unsig00101",
      "owner" : "8830eabd-bd42-494a-a5d5-a091602a95ae",
      "amount" : 45678
    }
    ----

```

- Response

```
   [source,http,options="nowrap"]
    ----
    HTTP/1.1 202 Accepted
    Vary: Origin
    Vary: Access-Control-Request-Method
    Vary: Access-Control-Request-Headers
    Content-Type: application/json
    Content-Length: 30
    
    {
      "unsigId" : "unsig00101"
    }
    ----
```

### List offers by unsig ids

- Request

`Endpoint : /api/v1/offers/find`

```
    [source,http,options="nowrap"]
    ----
    POST /api/v1/offers/find HTTP/1.1
    Content-Type: application/json
    Accept: application/json
    Content-Length: 58
    Host: localhost:8080
    
    [ "unsig00018", "unsig00019", "unsig00016", "unsig00012" ]
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
    Content-Length: 1686
    
    {
      "hasNextPage" : false,
      "totalPages" : 1,
      "listSize" : 3,
      "resultList" : [ {
        "txHash" : "1fddc43d-68c2-476a-a1b8-992654893921",
        "txIndex" : 12349073,
        "datumHash" : "78739e29-44cd-47ff-aad0-f474b2bac9a5",
        "unsigId" : "unsig00016",
        "owner" : "3b6cc813-cc81-4754-9f68-7fdb66087fab",
        "amount" : 3030,
        "details" : {
          "index" : 16,
          "num_props" : 2,
          "properties" : {
            "multipliers" : [ 0.5, 1 ],
            "colors" : [ "Blue", "Green" ],
            "distributions" : [ "CDF", "CDF" ],
            "rotations" : [ 0, 0 ]
          },
          "unsigId" : "unsig00016"
        }
      }, {
        "txHash" : "02bdb0d2-4f6c-4b06-90ab-6fe9c9839c3e",
        "txIndex" : 1136370661,
        "datumHash" : "4b3d3c86-33fd-44eb-af75-cb75bb09b3b0",
        "unsigId" : "unsig00018",
        "owner" : "cfacafd0-7d61-400b-acbb-67f00304b1d3",
        "amount" : 1020,
        "details" : {
          "index" : 18,
          "num_props" : 2,
          "properties" : {
            "multipliers" : [ 0.5, 1 ],
            "colors" : [ "Blue", "Green" ],
            "distributions" : [ "CDF", "CDF" ],
            "rotations" : [ 0, 180 ]
          },
          "unsigId" : "unsig00018"
        }
      }, {
        "txHash" : "36111ead-a5d9-4018-94b3-c5d066f51697",
        "txIndex" : 1180481274,
        "datumHash" : "1a91f680-8518-4133-9b02-63c990d4082d",
        "unsigId" : "unsig00019",
        "owner" : "b2c9256f-560a-4e65-ac75-5c8c77ba4d6c",
        "amount" : 2020,
        "details" : {
          "index" : 19,
          "num_props" : 2,
          "properties" : {
            "multipliers" : [ 0.5, 1 ],
            "colors" : [ "Blue", "Red" ],
            "distributions" : [ "CDF", "CDF" ],
            "rotations" : [ 0, 0 ]
          },
          "unsigId" : "unsig00019"
        }
      } ]
    }
    ----

```

### Paginated listing of offers filtered by the owner

`This is similar to paginated listing of offers above. As addition to that, a new query parameter 'owner' has to be added`

- Request

`Endpoint : /api/v1/offers`

```
    [source,http,options="nowrap"]
    ----
    GET /api/v1/offers?pageNo=0&pageSize=10&owner=5d6decc3-014a-42a0-975e-ecabac7cde22 HTTP/1.1
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
    Content-Length: 1129
    
    {
      "hasNextPage" : false,
      "totalPages" : 1,
      "listSize" : 2,
      "resultList" : [ {
        "txHash" : "db348ea1-ead3-4355-88ed-c0dd3f403499",
        "txIndex" : 1518820475,
        "datumHash" : "21b405d0-6603-492c-9ab7-3e7cd41be558",
        "unsigId" : "unsig00010",
        "owner" : "5d6decc3-014a-42a0-975e-ecabac7cde22",
        "amount" : 300,
        "details" : {
          "index" : 10,
          "num_props" : 1,
          "properties" : {
            "multipliers" : [ 2 ],
            "colors" : [ "Blue" ],
            "distributions" : [ "CDF" ],
            "rotations" : [ 0 ]
          },
          "unsigId" : "unsig00010"
        }
      }, {
        "txHash" : "c15dd0fb-3819-4c55-bad2-829e27e2bf42",
        "txIndex" : 2048643780,
        "datumHash" : "3c99184e-99cf-4c1b-a08c-acf9fb85697d",
        "unsigId" : "unsig00017",
        "owner" : "5d6decc3-014a-42a0-975e-ecabac7cde22",
        "amount" : 100,
        "details" : {
          "index" : 17,
          "num_props" : 2,
          "properties" : {
            "multipliers" : [ 0.5, 1 ],
            "colors" : [ "Blue", "Green" ],
            "distributions" : [ "CDF", "CDF" ],
            "rotations" : [ 0, 90 ]
          },
          "unsigId" : "unsig00017"
        }
      } ]
    }
    ----
```
