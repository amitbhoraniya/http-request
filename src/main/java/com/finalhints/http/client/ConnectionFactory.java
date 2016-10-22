package com.finalhints.http.client;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public interface ConnectionFactory {
	ConnectionFactory DEFAULT_CONNECTION_FACTORY = new ConnectionFactory() {
		@Override
		public HttpURLConnection openConnection(URL url) throws IOException {
			return (HttpURLConnection) url.openConnection();
		}
	};
	HttpURLConnection openConnection(URL url) throws IOException;
}
