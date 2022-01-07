### Get an unsig by id

- Request

`Endpoint : /api/v1/unsigs/{unsigId}`

```
    [source,http,options="nowrap"]
    ----
    GET /api/v1/unsigs/unsig00000 HTTP/1.1
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
    Content-Length: 273
    
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
      },
      "offerDetails" : null
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


### List unsigs by ids


- Request

`Endpoint : /api/v1/unsigs/find`

```
    [source,http,options="nowrap"]
    ----
    POST /api/v1/unsigs/find HTTP/1.1
    Content-Type: application/json
    Accept: application/json
    Content-Length: 352
    Host: localhost:8080
    
    [ "unsig00030", "unsig00031", "unsig00032", "unsig00033", "unsig00034", "unsig00035", "unsig00036", "unsig00037", "unsig00038", "unsig00039", "unsig00040", "unsig00041", "unsig00042", "unsig00043", "unsig00044", "unsig00060", "unsig00061", "unsig00062", "unsig00063", "unsig00064", "unsig00065", "unsig00066", "unsig00067", "unsig00068", "unsig00069" ]
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
    Content-Length: 9392
    
    {
      "hasNextPage" : false,
      "totalPages" : 1,
      "listSize" : 25,
      "resultList" : [ {
        "unsigId" : "unsig00030",
        "details" : {
          "index" : 30,
          "num_props" : 2,
          "properties" : {
            "multipliers" : [ 0.5, 4 ],
            "colors" : [ "Blue", "Green" ],
            "distributions" : [ "CDF", "CDF" ],
            "rotations" : [ 0, 180 ]
          },
          "unsigId" : "unsig00030"
        },
        "offerDetails" : {
          "unsigId" : "unsig00030",
          "owner" : "5c5bd1b3-3088-4a99-afab-a2c381dc09b5",
          "amount" : 2296166838706248127,
          "txHash" : "324c3b41-c2ac-48f5-a09a-2ff797dfea43",
          "txIndex" : 1764895417
        }
      }, {
        "unsigId" : "unsig00068",
        "details" : {
          "index" : 68,
          "num_props" : 2,
          "properties" : {
            "multipliers" : [ 0.5, 2 ],
            "colors" : [ "Green", "Red" ],
            "distributions" : [ "CDF", "CDF" ],
            "rotations" : [ 0, 180 ]
          },
          "unsigId" : "unsig00068"
        },
        "offerDetails" : null
      }, 
      ...
      ...
      ...
      {
        "unsigId" : "unsig00069",
        "details" : {
          "index" : 69,
          "num_props" : 2,
          "properties" : {
            "multipliers" : [ 0.5, 4 ],
            "colors" : [ "Green", "Blue" ],
            "distributions" : [ "CDF", "CDF" ],
            "rotations" : [ 0, 0 ]
          },
          "unsigId" : "unsig00069"
        },
        "offerDetails" : {
          "unsigId" : "unsig00069",
          "owner" : "6cb69424-8ae9-40b0-bb39-7ccb4bedd818",
          "amount" : 1634258377742717101,
          "txHash" : "964a876b-e91a-4d28-9436-466a7815193b",
          "txIndex" : 1692115815
        }
      } ]
    }
    ----

```