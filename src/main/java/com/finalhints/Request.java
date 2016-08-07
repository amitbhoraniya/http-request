package com.finalhints;

import java.util.HashMap;
import java.util.Map;

public class Request {

	public enum RequestType {
		Form_Data, Form_Url_Encoded, Raw;
	}

	private String url;
	private boolean uncompress = false;
	private boolean trustCert = false;
	private RequestMethod requestMethod = RequestMethod.GET;
	private RequestType requestType = RequestType.Form_Url_Encoded;
	private ConnectionFactory connectionFactory = ConnectionFactory.DEFAULT_CONNECTION_FACTORY;
	static final String DEFAULT_CONTENT_TYPE = "text/plain; charset=UTF-8";
	private String contentType = DEFAULT_CONTENT_TYPE;

	private Map<String, Object> urlParams = new HashMap<String, Object>();
	private Map<String, Object> formParams = new HashMap<String, Object>();
	private Map<String, Object> cookie = new HashMap<String, Object>();
	private Map<String, String> headers = new HashMap<String, String>();
	private String body = null;

	public Request(String url) {
		this.url = url;
	}

	public Request(String url, RequestMethod requestMethod) {
		this(url);
		this.requestMethod = requestMethod;
	}

	/**
	 * To specify requestBody of http request. If requestBody is specified then
	 * form parameters will be ignored. And default contentType is
	 * "text/plain; charset=UTF-8". To specify custom contentType like
	 * "application/json" use {@link #contentType(String)} method. This method
	 * also sets requestType to Raw.
	 * 
	 * @param requestBody
	 * @return
	 */
	public Request body(String requestBody) {
		this.body = requestBody;
		requestType = RequestType.Raw;
		return this;
	}

	public String getContentType() {
		return contentType;
	}

	public Request contentType(String contentType) {
		this.contentType = contentType;
		return this;
	}

	public RequestType getRequestType() {
		return requestType;
	}

	/**
	 * Default {@link RequestType} is Form_Url_Encoded Use this method to
	 * specify it to Form_Data or Raw data.
	 * 
	 * @param requestType
	 * @return
	 */
	public Request requestType(RequestType requestType) {
		this.requestType = requestType;
		return this;
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

	public Map<String, Object> getUrlParams() {
		return urlParams;
	}

	public void setUrlParams(Map<String, Object> urlParams) {
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

	public Map<String, Object> getCookie() {
		return cookie;
	}

	public void setCookie(Map<String, Object> cookie) {
		this.cookie = cookie;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
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

	public Request cookie(String key, String value) {
		cookie.put(key, value);
		return this;
	}

	public Request acceptGzipEncoding() {
		headers.put("Accept-Encoding", "gzip");
		return this;
	}

	public Request trustCert(boolean trustAllCert) {
		this.trustCert = trustAllCert;
		return this;
	}

	public Request uncompress(boolean uncompress) {
		this.uncompress = uncompress;
		return this;
	}

	public boolean isUncompress() {
		return uncompress;
	}

	public boolean isTrustCert() {
		return trustCert;
	}

	public ConnectionFactory getConnectionFactory() {
		return connectionFactory;
	}

	public void setConnectionFactory(ConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}
}
