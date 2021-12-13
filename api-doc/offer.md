### Create or update offer

- Request

`Endpoint : /api/v1/offers`
```
    [source,http,options="nowrap"]
    ----
    PUT /api/v1/offers HTTP/1.1
    Content-Type: application/json
    Accept: application/json
    Content-Length: 116
    Host: localhost:8080
    
    {
    "unsigId" : 2091739706789499382,
    "owner" : "3364e31b-1e40-40a8-b4a2-0201a4c93ecf",
    "amount" : 10202020
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
    Content-Length: 129
    
    {
      "id" : 1,
      "unsigId" : 2091739706789499382,
      "owner" : "3364e31b-1e40-40a8-b4a2-0201a4c93ecf",
      "amount" : 10202020
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
    Content-Length: 856
    
    {
      "hasNextPage" : false,
      "totalPages" : 1,
      "listSize" : 2,
      "resultList" : [ {
        "unsigId" : "unsig00013",
        "owner" : "1b89ffa6-d241-4c77-9498-9108f6227a48",
        "amount" : 10202020,
        "details" : {
          "index" : 13,
          "num_props" : 1,
          "properties" : {
            "multipliers" : [ 4 ],
            "colors" : [ "Blue" ],
            "distributions" : [ "CDF" ],
            "rotations" : [ 0 ]
          },
          "unsigId" : "unsig00013"
        }
      }, {
        "unsigId" : "unsig00016",
        "owner" : "b2120150-8a45-4437-bfac-4a0da37f113c",
        "amount" : 10202020,
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
      } ]
    }
    ----
```