package com.finalhints;

import java.util.HashMap;
import java.util.Map;

public class Request {

    private String url;
    private RequestMethod requestMethod;
    private String requestBody = null;
    private Map<String, String> urlParams = new HashMap<String, String>();
    private Map<String, Object> formParams = new HashMap<String, Object>();
    private Map<String, String> headers = new HashMap<String, String>();

    public Request(String url) {
	this.url = url;
	requestMethod = RequestMethod.GET;
    }

    public Request(String url, RequestMethod requestMethod) {
	this.requestMethod = requestMethod;
    }

    public String getUrl() {
	return url;
    }

    public void setUrl(String url) {
	this.url = url;
    }

    public RequestMethod getRequestMethod() {
	return requestMethod;
    }

    public void setRequestMethod(RequestMethod requestMethod) {
	this.requestMethod = requestMethod;
    }

    public String getRequestBody() {
	return requestBody;
    }

    public void setRequestBody(String requestBody) {
	this.requestBody = requestBody;
    }

    public Map<String, String> getUrlParams() {
	return urlParams;
    }

    public void setUrlParams(Map<String, String> urlParams) {
	this.urlParams = urlParams;
    }

    public Map<String, Object> getFormParams() {
	return formParams;
    }

    public void setFormParams(Map<String, Object> formParams) {
	this.formParams = formParams;
    }

    public Map<String, String> getHeaders() {
	return headers;
    }

    public void setHeaders(Map<String, String> headers) {
	this.headers = headers;
    }
}
