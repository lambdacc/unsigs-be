### Create or update offer

- Request

`Endpoint : /api/v1/last-transaction/{asset}`

    Here {asset} should be the concatenation of policyId and hex encoded asset name.
    Eg:
    If
    policyId = `1e82bbd44f7bd555a8bcc829bd4f27056e86412fbb549efdbf78f42d`
    assetName = `unsig00017`   (hex encoded = `756e7369673030303137`)
    then the path variable {asset} will be
    `1e82bbd44f7bd555a8bcc829bd4f27056e86412fbb549efdbf78f42d756e7369673030303137`


```
    [source,http,options="nowrap"]
    ----
    GET /api/v1/last-transaction/1e82bbd44f7bd555a8bcc829bd4f27056e86412fbb549efdbf78f42d756e7369673030303137 HTTP/1.1
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
    Content-Length: 156
    
    {
      "txHash" : "fcc578320e22d8dbba0ef6b43322db752f7d4d9a098a0bd42c17fb86f4d757c3",
      "txIndex" : 15,
      "blockHeight" : 3215295,
      "blockTime" : 1641481159
    }
    ----

```
