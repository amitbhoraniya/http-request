package com.finalhints;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.finalhints.Request.RequestType;

public class HttpClient {

	private static List<RequestListener> globalListeners = new ArrayList<RequestListener>();
	private List<RequestListener> localListeners = new ArrayList<RequestListener>();
	private Request request;
	private Response response;

	HttpClient(Request request) {
		this.request = request;
		response = new Response();
	}

	public Response execute() {
		beforeRequest();

		URL obj;
		try {

			// Create connection to URL
			String urlParams = urlEncodeUTF8(request.getUrlParams());
			if (urlParams.isEmpty())
				obj = new URL(request.getUrl());
			else
				obj = new URL(request.getUrl() + "?" + urlParams);

			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// Set Request Method
			con.setRequestMethod(request.getRequestMethod().toString());

			// Set Headers
			Map<String, String> headers = request.getHeaders();
			for (Entry<String, String> header : headers.entrySet()) {
				con.setRequestProperty(header.getKey(), header.getValue());
			}

			// Request Body
			if (request.getRequestType().equals(RequestType.Form_Data) && !request.getFormParams().isEmpty()) {

				// For Form Data
				String twoHyphens = "--";
				String boundary = "*****" + Long.toString(System.currentTimeMillis()) + "*****";
				String lineEnd = "\r\n";
				int bytesRead, bytesAvailable, bufferSize;
				byte[] buffer;
				int maxBufferSize = 1 * 1024 * 1024;

				con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
				con.setRequestProperty("Connection", "Keep-Alive");
				con.setDoOutput(true);
				DataOutputStream dos = new DataOutputStream(con.getOutputStream());
				Map<String, Object> params = request.getFormParams();
				Iterator<String> keys = params.keySet().iterator();
				while (keys.hasNext()) {
					String key = keys.next();
					Object value = params.get(key);

					dos.writeBytes(twoHyphens + boundary + lineEnd);
					if (value instanceof File) {
						File file = (File) value;
						FileInputStream fis = new FileInputStream(file);
						dos.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"; filename=\""
								+ file.getName() + "\"" + lineEnd);
						dos.writeBytes("Content-Type: " + URLConnection.guessContentTypeFromStream(fis) + lineEnd);
						dos.writeBytes("Content-Transfer-Encoding: binary" + lineEnd);
						dos.writeBytes(lineEnd);
						bytesAvailable = fis.available();
						bufferSize = Math.min(bytesAvailable, maxBufferSize);
						buffer = new byte[bufferSize];
						bytesRead = fis.read(buffer, 0, bufferSize);
						while (bytesRead > 0) {
							dos.write(buffer, 0, bufferSize);
							bytesAvailable = fis.available();
							bufferSize = Math.min(bytesAvailable, maxBufferSize);
							bytesRead = fis.read(buffer, 0, bufferSize);
						}
						fis.close();
					} else {
						dos.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"" + lineEnd);
						dos.writeBytes("Content-Type: text/plain" + lineEnd);
						dos.writeBytes(lineEnd);
						dos.writeBytes(String.valueOf(value));
					}
					dos.writeBytes(lineEnd);
				}
				dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
				dos.flush();
				dos.close();
			} else if (request.getRequestType().equals(RequestType.Form_Url_Encoded)
					&& !request.getFormParams().isEmpty()) {

				con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

				// For Form_URL_Encoded Request
				con.setDoOutput(true);
				DataOutputStream out = new DataOutputStream(con.getOutputStream());
				out.writeBytes(urlEncodeUTF8(request.getFormParams()));
				out.flush();
				out.close();

			} else if (request.getRequestType().equals(RequestType.Raw) && request.getBody() != null
					&& !request.getBody().isEmpty()) {
				con.setRequestProperty("Content-Type", request.getContentType());

				// For RAW Request Body
				con.setDoOutput(true);
				DataOutputStream out = new DataOutputStream(con.getOutputStream());
				out.writeBytes(request.getBody());
				out.flush();
				out.close();
			}
			response.setStatusCode(con.getResponseCode());
			response.setResponseHeader(con.getHeaderFields());
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer buffer = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				buffer.append(inputLine);
			}
			in.close();
			response.setResponseBody(buffer.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		onResponse();
		return response;
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

	public void registerListener(RequestListener listener) {
		localListeners.add(listener);
	}

	public void deregisterListener(RequestListener listener) {
		localListeners.remove(listener);
	}

	public static void regiterDefaultListener(RequestListener listener) {
		globalListeners.add(listener);
	}

	public static void deregisterDefaultListener(RequestListener listener) {
		globalListeners.remove(listener);
	}

	private void beforeRequest() {
		for (RequestListener requestListener : localListeners) {
			requestListener.beforeRequest(request);
		}
		for (RequestListener requestListener : globalListeners) {
			requestListener.beforeRequest(request);
		}
	}

	private void onResponse() {
		for (RequestListener requestListener : globalListeners) {
			requestListener.onResponse(response);
		}
		for (RequestListener requestListener : localListeners) {
			requestListener.onResponse(response);
		}
	}
}
