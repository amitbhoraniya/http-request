package com.finalhints.http.client;

public interface RequestListener {
	void beforeRequest(Request request);

	void onResponse(Response response);
	
	void onError(Response response);
}
