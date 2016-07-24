# http-request
Simple Http Library to make request and access the response.

1. Create a Request Object
2. Create instance of HttpClient using request instance.
3. Send Request

## Send GET Request

```java
Request request = new Request("http://httpbin.org/get");
HttpClient client = new HttpClient(request);
Response response = client.execute();
```

## Send POST Request

```java
Request request = new Request("http://httpbin.org/put", RequestMethod.POST);
request.form("key1", "value1");
HttpClient client = new HttpClient(request);
Response response = client.execute();
```
