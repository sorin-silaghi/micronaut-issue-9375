There are two versions of the API:
 - Version 1
   - POST /common
 - Version 2
   - POST /common
   - POST /new

CORS is enabled in application.yaml as well as API versioning.

Making an OPTIONS request to the `/common` endpoint is successful:
```
curl -H "Origin: http://example.com" \
     -H "Access-Control-Request-Method: POST" \
     -H "Access-Control-Request-Headers: X-Requested-With" \
     -X OPTIONS --verbose http://localhost:8080/common
```
Result of the request:
```
    *   Trying 127.0.0.1:8080...
    * Connected to localhost (127.0.0.1) port 8080 (#0)
    > OPTIONS /common HTTP/1.1
    > Host: localhost:8080
    > User-Agent: curl/7.81.0
    > Accept: */*
    > Origin: http://example.com
    > Access-Control-Request-Method: POST
    > Access-Control-Request-Headers: X-Requested-With
    > 
    * Mark bundle as not supporting multiuse
    < HTTP/1.1 200 OK
    < Access-Control-Allow-Methods: POST
    < Access-Control-Allow-Headers: X-Requested-With
    < Access-Control-Max-Age: 1800
    < Access-Control-Allow-Origin: http://example.com
    < Vary: Origin
    < Access-Control-Allow-Credentials: true
    < connection: keep-alive
    < transfer-encoding: chunked
    < 
    * Connection #0 to host localhost left intact
```

Making an OPTIONS request to the `/new` endpoint fails with 404:
```
curl -H "Origin: http://example.com" \
     -H "Access-Control-Request-Method: POST" \
     -H "Access-Control-Request-Headers: X-Requested-With" \
     -X OPTIONS --verbose http://localhost:8080/new
```
Result of the request:
```
    *   Trying 127.0.0.1:8080...
    * Connected to localhost (127.0.0.1) port 8080 (#0)
    > OPTIONS /new HTTP/1.1
    > Host: localhost:8080
    > User-Agent: curl/7.81.0
    > Accept: */*
    > Origin: http://example.com
    > Access-Control-Request-Method: POST
    > Access-Control-Request-Headers: X-Requested-With
    > 
    * Mark bundle as not supporting multiuse
    < HTTP/1.1 404 Not Found
    < Content-Type: application/json
    < Access-Control-Allow-Methods: POST
    < Access-Control-Allow-Headers: X-Requested-With
    < Access-Control-Max-Age: 1800
    < Access-Control-Allow-Origin: http://example.com
    < Vary: Origin
    < Access-Control-Allow-Credentials: true
    < content-length: 129
    < connection: keep-alive
    < 
    * Connection #0 to host localhost left intact
    {"message":"Not Found","_links":{"self":{"href":"/new","templated":false}},"_embedded":{"errors":[{"message":"Page Not Found"}]}}
```
