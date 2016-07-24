package com.finalhints;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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

    public String getRequestBody() {
	return urlEncodeUTF8(request.getFormParams());
    }

    public String getRequestURL() {
	return request.getUrl();
    }

    public String getUrlParams() {
	return urlEncodeUTF8(request.getUrlParams());
    }

    public String getRequestMethod() {
	return request.getRequestMethod().toString();
    }

    static String urlEncodeUTF8(String s) {
	try {
	    return URLEncoder.encode(s, "UTF-8");
	} catch (UnsupportedEncodingException e) {
	    throw new UnsupportedOperationException(e);
	}
    }

    static String urlEncodeUTF8(Map<?, ?> map) {
	StringBuilder sb = new StringBuilder();
	for (Map.Entry<?, ?> entry : map.entrySet()) {
	    if (sb.length() > 0) {
		sb.append("&");
	    }
	    sb.append(String.format("%s=%s", urlEncodeUTF8(entry.getKey().toString()),
		    urlEncodeUTF8(entry.getValue().toString())));
	}
	return sb.toString();
    }
}
