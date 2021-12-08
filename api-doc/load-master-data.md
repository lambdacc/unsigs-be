### Load master data
### The server on start up loads the master data. So this is for reference.

- Request

`Endpoint : /api/v1/load-data`
```
    [source,http,options="nowrap"]
    ----
    POST /api/v1/load-data HTTP/1.1
    Content-Type: application/json
    Accept: application/json
    Host: localhost:8080
    
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
    Content-Length: 4
    
    true
    ----

```
    