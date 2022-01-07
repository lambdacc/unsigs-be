### Create or update offer

- Request

`Endpoint : /api/v1/offers`
```
    [source,http,options="nowrap"]
    ----
    PUT /api/v1/offers HTTP/1.1
    Content-Type: application/json
    Accept: application/json
    Content-Length: 180
    Host: localhost:8080
    
    {
      "unsigId" : "unsig00106",
      "owner" : "c2542684-b519-4b45-a8cf-0d9ed939ad10",
      "amount" : 1020,
      "txHash" : "5c988a44-9a84-4e45-bb64-a5353dd8aacb",
      "txIndex" : 2018366034
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
    Content-Length: 196
    
    {
      "id" : 62241,
      "unsigId" : "unsig00106",
      "owner" : "c2542684-b519-4b45-a8cf-0d9ed939ad10",
      "amount" : 1020,
      "txHash" : "5c988a44-9a84-4e45-bb64-a5353dd8aacb",
      "txIndex" : 2018366034
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
    Content-Length: 988
    
    {
      "hasNextPage" : false,
      "totalPages" : 1,
      "listSize" : 2,
      "resultList" : [ {
        "unsigId" : "unsig00010",
        "owner" : "9073127f-875c-4b1e-86b1-617ed54fba2c",
        "amount" : 1020,
        "txHash" : "6bb6ae9d-e933-4227-b906-7c485140aa8b",
        "txIndex" : 1828177559,
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
        "unsigId" : "unsig00012",
        "owner" : "38e5b335-b4c3-44fb-adb4-f0a912ed4dd0",
        "amount" : 8888,
        "txHash" : "40f1fbff-d3f8-4f7d-a236-337ab509eb1e",
        "txIndex" : 937212804,
        "details" : {
          "index" : 12,
          "num_props" : 1,
          "properties" : {
            "multipliers" : [ 2 ],
            "colors" : [ "Red" ],
            "distributions" : [ "CDF" ],
            "rotations" : [ 0 ]
          },
          "unsigId" : "unsig00012"
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