[![Build Status](https://travis-ci.org/amitbhoraniya/http-request.svg?branch=master)](https://travis-ci.org/amitbhoraniya/http-request)
[![Dependency Status](https://www.versioneye.com/user/projects/57a441491dadcb004272d6b5/badge.svg)](https://www.versioneye.com/user/projects/57a441491dadcb004272d6b5)

# Http Request
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

## Pass Query Parameters

```java
Request request = new Request("http://httpbin.org/get");
request.args("search","amit").args("type","people");

//will convert to http://httpbin.org/get?search=amit&type=people

Response response = new HttpClient(request).execute();
```


## Send POST Request

```java
Request request = new Request("http://httpbin.org/post", RequestMethod.POST);
Response response = new HttpClient(request).execute();
```

## Post Request with Form Data

```java
Request request = new Request("http://httpbin.org/post", RequestMethod.POST);

//Form-Data
request.form("key1", "value1").form("key2", "value2");

Response response = new HttpClient(request);.execute();
```

## GZip Compression

```java
Request request = new Request("http://httpbin.org/gzip");
request.acceptGzipEncoding().uncompress(true);
Response response = new HttpClient(request).execute();
```

## Trust Certificates

```java
Request request = new Request("https://httpbin.org/get");
//To trust all certificate
request.trustCert(true);
Response response = new HttpClient(request).execute();
```
