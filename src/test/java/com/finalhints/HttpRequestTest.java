package com.finalhints;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class HttpRequestTest {

    @Test
    public void getRequestTest() {
	Request request = new Request("http://httpbin.org/get");
	HttpClient client = new HttpClient(request);
	Response response = client.execute();
	Gson gson = new GsonBuilder().setPrettyPrinting().create();
	System.out.println(gson.toJson(new Gson().fromJson(response.getResponseBody(), Object.class)));
    }

    @Test
    public void postRequestTest() {
	Request request = new Request("http://httpbin.org/put", RequestMethod.PUT);
	request.args("p", 1).form("key1", "value1");
	HttpClient client = new HttpClient(request);
	Response response = client.execute();
	Gson gson = new GsonBuilder().setPrettyPrinting().create();
	System.out.println(gson.toJson(new Gson().fromJson(response.getResponseBody(), Object.class)));
    }

    @Test
    public void deleteRequestTest() {
	Request request = new Request("http://httpbin.org/delete", RequestMethod.DELETE);
	request.args("p", 1).form("key1", "value1");
	HttpClient client = new HttpClient(request);
	Response response = client.execute();
	Gson gson = new GsonBuilder().setPrettyPrinting().create();
	System.out.println(gson.toJson(new Gson().fromJson(response.getResponseBody(), Object.class)));
    }
}
