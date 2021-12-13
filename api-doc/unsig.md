### Get an unsig by id

- Request

`Endpoint : /api/v1/unsigs/{unsigId}`

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
    Content-Length: 248
    
    {
      "unsigId" : "unsig00000",
      "details" : {
        "index" : 0,
        "num_props" : 0,
        "properties" : {
          "multipliers" : [ ],
          "colors" : [ ],
          "distributions" : [ ],
          "rotations" : [ ]
        },
        "unsigId" : "unsig00000"
      }
    }
    ----

```

### Paginated listing of unsigs

- Request

`Endpoint : /api/v1/unsigs`

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
    Content-Length: 3023
    
    {
      "hasNextPage" : true,
      "totalPages" : 3112,
      "listSize" : 10,
      "resultList" : [ {
        "unsigId" : "unsig00000",
        "details" : {
          "index" : 0,
          "num_props" : 0,
          "properties" : {
            "multipliers" : [ ],
            "colors" : [ ],
            "distributions" : [ ],
            "rotations" : [ ]
          },
          "unsigId" : "unsig00000"
        }
      }, {
        "unsigId" : "unsig00009",
        "details" : {
          "index" : 9,
          "num_props" : 1,
          "properties" : {
            "multipliers" : [ 1 ],
            "colors" : [ "Red" ],
            "distributions" : [ "CDF" ],
            "rotations" : [ 0 ]
          },
          "unsigId" : "unsig00009"
        }
      } ]
    }
    ----

```