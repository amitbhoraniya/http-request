package com.finalhints.http.client;

import java.util.List;
import java.util.Map;

public class Response {
	private int statusCode;
	private String responseBody;
	private Map<String, List<String>> responseHeader;
	private Exception exception = null;

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getResponseBody() {
		return responseBody;
	}

	public void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}

	public Map<String, List<String>> getResponseHeader() {
		return responseHeader;
	}

	public void setResponseHeader(Map<String, List<String>> map) {
		this.responseHeader = map;
	}

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

	@Override
	public String toString() {
		return "Response [statusCode=" + statusCode + ", responseBody="
				+ responseBody + ", responseHeader=" + responseHeader + "]";
	}

}
