package com.finalhints;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.net.HttpURLConnection;

import org.junit.Test;

import com.finalhints.Request.RequestType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class HttpRequestTest {

	@Test
	public void getRequestTest() {
		Request request = new Request("http://httpbin.org/get");
		HttpClient client = new HttpClient(request);
		Response response = client.execute();
		assertEquals("Verify status code", HttpURLConnection.HTTP_OK, response.getStatusCode());
		assertNotNull("Verify response body", response.getResponseBody());
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

	@Test
	public void urlEncodedRequestTest() {
		Request request = new Request("http://httpbin.org/post", RequestMethod.POST);
		request.args("p", 1).form("key1", "value1").requestType(RequestType.Form_Url_Encoded);
		HttpClient client = new HttpClient(request);
		Response response = client.execute();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		System.out.println(gson.toJson(new Gson().fromJson(response.getResponseBody(), Object.class)));
	}

	@Test
	public void rawRequestTest() {
		Request request = new Request("http://httpbin.org/post", RequestMethod.POST);
		request.body("{'key11':'value22'}").contentType("application/json");
		HttpClient client = new HttpClient(request);
		Response response = client.execute();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		System.out.println(gson.toJson(new Gson().fromJson(response.getResponseBody(), Object.class)));
	}

	@Test
	public void fileUploadTest() {
		Request request = new Request("http://httpbin.org/post", RequestMethod.POST);
		request.args("p1", "v1").form("file", new File("sample.test")).form("f1", "value1");
		HttpClient client = new HttpClient(request);
		Response response = client.execute();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		System.out.println(gson.toJson(new Gson().fromJson(response.getResponseBody(), Object.class)));
	}
}
