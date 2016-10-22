package com.finalhints.http.client;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.net.HttpURLConnection;

import org.testng.annotations.Test;

import com.finalhints.http.client.HttpClient;
import com.finalhints.http.client.Request;
import com.finalhints.http.client.RequestMethod;
import com.finalhints.http.client.Response;
import com.finalhints.http.client.Request.RequestType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class HttpRequestTest {

	Gson gson = new GsonBuilder().setPrettyPrinting().create();
	@Test(testName = "Verify Get Request")
	public void verify_GET_Request() {
		Request request = new Request("http://httpbin.org/get");
		HttpClient client = new HttpClient(request);
		Response response = client.execute();
		assertEquals(response.getStatusCode(), HttpURLConnection.HTTP_OK,
				"Verify status code");
		assertNotNull(response.getResponseBody(), "Verify response body");
	}

	@Test(testName = "Verify Query Parameter")
	public void verify_Query_Parameter() {
		Request request = new Request("http://httpbin.org/get");
		request.args("search", "Amit");
		HttpClient client = new HttpClient(request);
		Response response = client.execute();
		assertEquals(response.getStatusCode(), HttpURLConnection.HTTP_OK,
				"Verify status code");
		String responseBody = response.getResponseBody();
		JsonObject resObject = gson.fromJson(responseBody, JsonObject.class);
		JsonObject args = resObject.get("args").getAsJsonObject();
		assertEquals(args.get("search").getAsString(), "Amit",
				"Query Parameter Verified");
	}

	@Test(testName = "Verify POST Reuqest")
	public void verify_POST_Request() {
		Request request = new Request("http://httpbin.org/post", RequestMethod.POST);
		request.form("search", "Amit");
		Response response = new HttpClient(request).execute();
		assertEquals(response.getStatusCode(), HttpURLConnection.HTTP_OK,
				"Verify status code");
		String responseBody = response.getResponseBody();

		// verify Form Parameter
		JsonObject resObject = gson.fromJson(responseBody, JsonObject.class);
		JsonObject args = resObject.get("form").getAsJsonObject();
		assertEquals(args.get("search").getAsString(), "Amit", "Post Parameter Verified");

		// verify Header Parameter
		JsonObject headers = resObject.get("headers").getAsJsonObject();
		assertEquals(headers.get("Content-Type").getAsString(),
				"application/x-www-form-urlencoded",
				"Default Request Type should be URL Encoded");
	}

	@Test(testName = "Verify POST Reuqest with Form Data")
	public void verify_POST_Request_1() {
		Request request = new Request("http://httpbin.org/post", RequestMethod.POST);
		request.form("search", "Amit").requestType(RequestType.Form_Data);
		Response response = new HttpClient(request).execute();
		assertEquals(response.getStatusCode(), HttpURLConnection.HTTP_OK,
				"Verify status code");
		String responseBody = response.getResponseBody();

		// verify Form Parameter
		JsonObject resObject = gson.fromJson(responseBody, JsonObject.class);
		JsonObject args = resObject.get("form").getAsJsonObject();
		assertEquals(args.get("search").getAsString(), "Amit", "Post Parameter Verified");

		// verify Header Parameter
		JsonObject headers = resObject.get("headers").getAsJsonObject();
		assertEquals(
				headers.get("Content-Type").getAsString().contains("multipart/form-data"),
				true, "Request Type should be Form Data");
	}

	@Test(testName = "Verify POST Reuqest with raw Body")
	public void verify_POST_Request_2() {
		JsonObject req = new JsonObject();
		req.addProperty("key", "value");

		Request request = new Request("http://httpbin.org/post", RequestMethod.POST);
		request.body(req.toString()).contentType("application/json");
		Response response = new HttpClient(request).execute();

		assertEquals(response.getStatusCode(), HttpURLConnection.HTTP_OK,
				"Verify status code");
		String responseBody = response.getResponseBody();

		// verify Form Parameter
		JsonObject resObject = gson.fromJson(responseBody, JsonObject.class);
		assertEquals(resObject.get("data").getAsString(), req.toString(),
				"Post Parameter Verified");

		// verify Header Parameter
		JsonObject headers = resObject.get("headers").getAsJsonObject();
		assertEquals(headers.get("Content-Type").getAsString(), "application/json",
				"Request Type should be application/json");
	}
}
