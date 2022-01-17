### Find utxo at an address for a given asset 
`Here {asset} should be the concatenation of policyId and hex encoded asset name.`

- Request

`Endpoint : /api/v1/utxo`

```
   [source,http,options="nowrap"]
    ----
    GET /api/v1/utxo?address=addr_test1wp9m8xkpt2tmy7madqldspgzgug8f2p3pwhz589cq75685slenwf4&unsigAsset=57fca08abbaddee36da742a839f7d83a7e1d2419f1507fcbf391652256414e494c&datumHash=2da471fa0e129914c1b7b0766242570dd2a92297b424e79280c79cc6f7206774 HTTP/1.1
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
    Content-Length: 691
    
    {
      "txHash" : "548fa548c1b658718ddfe059fa9137eb36b99226b930ffaf0a31ab621a20c481",
      "outputIndex" : 0,
      "amount" : [ {
        "unit" : "lovelace",
        "quantity" : "154677385522"
      }, {
        "unit" : "57fca08abbaddee36da742a839f7d83a7e1d2419f1507fcbf391652256414e494c",
        "quantity" : "7135515692"
      }, {
        "unit" : "d311d3488cc4fef19d05634adce8534977a3bc6fc18136ad65df1d4f702028",
        "quantity" : "1"
      } ],
      "block" : "f9b546a8dbbd22feae2cd9addebf5943a328fc269c0cff541dc7b027b6b022e4",
      "dataHash" : "2da471fa0e129914c1b7b0766242570dd2a92297b424e79280c79cc6f7206774",
      "asset" : "57fca08abbaddee36da742a839f7d83a7e1d2419f1507fcbf391652256414e494c",
      "assetQuantity" : "7135515692"
    }
    ----
```

- Error response

`If the datum hash does not match response status code will be `

 ```422
 ```


### Find last transaction
=======

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
