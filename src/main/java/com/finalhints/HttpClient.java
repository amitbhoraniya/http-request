package com.finalhints;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class HttpClient {

    private static List<RequestListener> globalListeners = new ArrayList<RequestListener>();
    private List<RequestListener> localListeners = new ArrayList<RequestListener>();
    private Request request;
    private Response response;

    HttpClient(Request request) {
	this.request = request;
	response = new Response();
    }

    public Response execute() {
	beforeRequest();
	RequestBuilder builder = new RequestBuilder(request);
	URL obj;
	try {
	    obj = new URL(builder.getRequestURL());
	    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	    con.setRequestMethod(builder.getRequestMethod());
	    Map<String, String> headers = builder.getHeaders();
	    for (Entry<String, String> header : headers.entrySet()) {
		con.setRequestProperty(header.getKey(), header.getValue());
	    }
	    response.setStatusCode(con.getResponseCode());
	    response.setResponseHeader(con.getHeaderFields());
	    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
	    String inputLine;
	    StringBuffer buffer = new StringBuffer();
	    while ((inputLine = in.readLine()) != null) {
		buffer.append(inputLine);
	    }
	    in.close();
	    response.setResponseBody(buffer.toString());

	} catch (IOException e) {
	    e.printStackTrace();
	}
	onResponse();
	return response;
    }

    public void registerListener(RequestListener listener) {
	localListeners.add(listener);
    }

    public void deregisterListener(RequestListener listener) {
	localListeners.remove(listener);
    }

    public static void regiterDefaultListener(RequestListener listener) {
	globalListeners.add(listener);
    }

    public static void deregisterDefaultListener(RequestListener listener) {
	globalListeners.remove(listener);
    }

    private void beforeRequest() {
	for (RequestListener requestListener : localListeners) {
	    requestListener.beforeRequest(request);
	}
	for (RequestListener requestListener : globalListeners) {
	    requestListener.beforeRequest(request);
	}
    }

    private void onResponse() {
	for (RequestListener requestListener : globalListeners) {
	    requestListener.onResponse(response);
	}
	for (RequestListener requestListener : localListeners) {
	    requestListener.onResponse(response);
	}
    }
}
