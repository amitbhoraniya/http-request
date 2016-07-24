package com.finalhints;

public interface RequestListener {
    void beforeRequest(Request request);

    void onResponse(Response response);
}
