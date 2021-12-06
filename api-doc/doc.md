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

### List offers

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
    Content-Length: 469
    
    {
      "hasNextPage" : false,
      "totalPages" : 1,
      "listSize" : 3,
      "resultList" : [ {
        "unsigId" : 2091739706789499382,
        "owner" : "3364e31b-1e40-40a8-b4a2-0201a4c93ecf",
        "amount" : 10202020
      }, {
        "unsigId" : 4016346931292741094,
        "owner" : "b352e3df-c37a-42b3-8f50-33e646f8c66f",
        "amount" : 10202020
      }, {
        "unsigId" : 1070367792716756242,
        "owner" : "0d14176c-06e4-4b17-a1f2-f40a3b7ab271",
        "amount" : 10202020
      } ]
    }
    ----


```