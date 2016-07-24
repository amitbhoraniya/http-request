package com.finalhints;

import org.junit.Test;

public class HttpRequestTest {

    @Test
    public void getRequestTest() {
	Request request = new Request("http://www.google.com");
	HttpClient client = new HttpClient(request);
	Response response = client.execute();
	System.out.println(response);
    }
}
