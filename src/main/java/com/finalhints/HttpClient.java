package com.finalhints;

import static com.finalhints.HttpUtils.urlEncodeUTF8;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.finalhints.Request.RequestType;
public class HttpClient {

	private static List<RequestListener> globalListeners = new ArrayList<RequestListener>();
	private static SSLSocketFactory TRUSTED_FACTORY;
	private List<RequestListener> localListeners = new ArrayList<RequestListener>();
	private Request request;
	private Response response;

	public HttpClient() {
	}

	public HttpClient(Request request) {
		this.request = request;
		response = new Response();
	}

	public HttpClient request(Request request) {
		this.request = request;
		response = new Response();
		return this;
	}

	public Response execute() {
		beforeRequest();

		URL obj;
		HttpURLConnection con = null;
		try {
			// Create connection to URL
			String urlParams = urlEncodeUTF8(request.getUrlParams());
			if (urlParams.isEmpty())
				obj = new URL(request.getUrl());
			else
				obj = new URL(request.getUrl() + "?" + urlParams);

			// Open Connection
			con = request.getConnectionFactory().openConnection(obj);

			if (con instanceof HttpsURLConnection && request.isTrustCert()) {
				((HttpsURLConnection) con)
						.setSSLSocketFactory(getTrustedFactory());
			}

			// Set Request Method
			con.setRequestMethod(request.getRequestMethod().toString());

			// Set Headers
			Map<String, String> headers = request.getHeaders();
			for (Entry<String, String> header : headers.entrySet()) {
				con.setRequestProperty(header.getKey(), header.getValue());
			}

			if (!request.getCookie().isEmpty())
				con.setRequestProperty("Cookie",
						HttpUtils.generateCookie(request.getCookie()));;

			// Request Body
			if (request.getRequestType().equals(RequestType.Form_Url_Encoded)
					&& !request.getFormParams().isEmpty()) {

				// For Form_URL_Encoded Request
				con.setRequestProperty("Content-Type",
						"application/x-www-form-urlencoded");
				con.setDoOutput(true);
				DataOutputStream out = new DataOutputStream(
						con.getOutputStream());
				out.writeBytes(urlEncodeUTF8(request.getFormParams()));
				out.flush();
				out.close();
			} else if (request.getRequestType().equals(RequestType.Form_Data)
					&& !request.getFormParams().isEmpty()) {

				// For Form Data
				String twoHyphens = "--";
				String boundary = "*****"
						+ Long.toString(System.currentTimeMillis()) + "*****";
				String lineEnd = "\r\n";
				int bytesRead, bytesAvailable, bufferSize;
				byte[] buffer;
				int maxBufferSize = 1 * 1024 * 1024;

				con.setRequestProperty("Content-Type",
						"multipart/form-data; boundary=" + boundary);
				con.setRequestProperty("Connection", "Keep-Alive");
				con.setDoOutput(true);
				DataOutputStream dos = new DataOutputStream(
						con.getOutputStream());
				Map<String, Object> params = request.getFormParams();
				Iterator<String> keys = params.keySet().iterator();
				while (keys.hasNext()) {
					String key = keys.next();
					Object value = params.get(key);

					dos.writeBytes(twoHyphens + boundary + lineEnd);
					if (value instanceof File) {
						File file = (File) value;
						FileInputStream fis = new FileInputStream(file);
						dos.writeBytes("Content-Disposition: form-data; name=\""
								+ key + "\"; filename=\"" + file.getName()
								+ "\"" + lineEnd);
						dos.writeBytes("Content-Type: "
								+ URLConnection.guessContentTypeFromStream(fis)
								+ lineEnd);
						dos.writeBytes(
								"Content-Transfer-Encoding: binary" + lineEnd);
						dos.writeBytes(lineEnd);
						bytesAvailable = fis.available();
						bufferSize = Math.min(bytesAvailable, maxBufferSize);
						buffer = new byte[bufferSize];
						bytesRead = fis.read(buffer, 0, bufferSize);
						while (bytesRead > 0) {
							dos.write(buffer, 0, bufferSize);
							bytesAvailable = fis.available();
							bufferSize = Math.min(bytesAvailable,
									maxBufferSize);
							bytesRead = fis.read(buffer, 0, bufferSize);
						}
						fis.close();
					} else {
						dos.writeBytes("Content-Disposition: form-data; name=\""
								+ key + "\"" + lineEnd);
						dos.writeBytes("Content-Type: text/plain" + lineEnd);
						dos.writeBytes(lineEnd);
						dos.writeBytes(String.valueOf(value));
					}
					dos.writeBytes(lineEnd);
				}
				dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
				dos.flush();
				dos.close();
			} else if (request.getRequestType().equals(RequestType.Raw)
					&& request.getBody() != null
					&& !request.getBody().isEmpty()) {

				// For RAW Request Body
				con.setRequestProperty("Content-Type",
						request.getContentType());
				con.setDoOutput(true);
				DataOutputStream out = new DataOutputStream(
						con.getOutputStream());
				out.writeBytes(request.getBody());
				out.flush();
				out.close();
			}
			response.setStatusCode(con.getResponseCode());
			response.setResponseHeader(con.getHeaderFields());
			InputStream is = con.getInputStream();
			if (request.isUncompress())
				is = new GZIPInputStream(is);
			BufferedReader in = new BufferedReader(new InputStreamReader(is));
			String inputLine;
			StringBuffer buffer = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				buffer.append(inputLine);
			}
			in.close();
			response.setResponseBody(buffer.toString());
			onResponse();
		} catch (IOException e) {
			if (con != null && con.getErrorStream() != null) {
				try {
					InputStream is = con.getErrorStream();
					BufferedReader in = new BufferedReader(
							new InputStreamReader(is));
					String inputLine;
					StringBuffer buffer = new StringBuffer();
					while ((inputLine = in.readLine()) != null) {
						buffer.append(inputLine);
					}
					in.close();
					response.setResponseBody(buffer.toString());
				} catch (IOException e1) {
					response.setException(e1);
				}
			} else {
				response.setException(e);
			}
		}
		return response;
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

	private static SSLSocketFactory getTrustedFactory() {
		if (TRUSTED_FACTORY == null) {
			final TrustManager[] trustAllCerts = new TrustManager[]{
					new X509TrustManager() {
						public X509Certificate[] getAcceptedIssuers() {
							return new X509Certificate[0];
						}
						public void checkClientTrusted(X509Certificate[] chain,
								String authType) {
						}
						public void checkServerTrusted(X509Certificate[] chain,
								String authType) {
						}
					}};
			try {
				SSLContext context = SSLContext.getInstance("TLS");
				context.init(null, trustAllCerts, new SecureRandom());
				TRUSTED_FACTORY = context.getSocketFactory();
			} catch (GeneralSecurityException e) {
				// To Do Security Exception
			}
		}
		return TRUSTED_FACTORY;
	}
}