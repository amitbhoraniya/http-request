package com.finalhints;

import java.util.HashMap;
import java.util.Map;

public class Request {

    private String url;
    private RequestMethod requestMethod = RequestMethod.GET;
    static final String DEFAULT_CONTENT_TYPE = "application/x-www-form-urlencoded";
    private String contentType;
    private String requestBody = null;
    private Map<String, Object> urlParams = new HashMap<String, Object>();
    private Map<String, Object> formParams = new HashMap<String, Object>();
    private Map<String, String> headers = new HashMap<String, String>();

    public Request(String url) {
	this.url = url;
	contentType = DEFAULT_CONTENT_TYPE;
    }

    public Request(String url, RequestMethod requestMethod) {
	this(url);
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

    public Map<String, Object> getUrlParams() {
	return urlParams;
    }

    public void setUrlParams(Map<String, Object> urlParams) {
	this.urlParams = urlParams;
    }

    public Request args(String key, Object value) {
	urlParams.put(key, value);
	return this;
    }

    public Request form(String string, Object value) {
	formParams.put(string, value);
	return this;
    }

    public Request header(String key, String value) {
	headers.put(key, value);
	return this;
    }

    public String getContentType() {
	return contentType;
    }

    public void setContentType(String contentType) {
	this.contentType = contentType;
    }
}
