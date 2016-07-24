package com.finalhints;

import java.util.HashMap;
import java.util.Map;

public class RequestBuilder {

    Request request = null;

    public RequestBuilder(Request request) {
	this.request = request;
    }

    public Map<String, String> getHeaders() {
	return new HashMap<>();
    }

    public void getRequestBody() {
    }

    public String getRequestURL() {
	return request.getUrl();
    }

    public String getRequestMethod() {
	return request.getRequestMethod().toString();
    }

}
