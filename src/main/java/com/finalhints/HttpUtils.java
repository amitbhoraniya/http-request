package com.finalhints;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class HttpUtils {

	public static String urlEncodeUTF8(String s) {
		try {
			return URLEncoder.encode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new UnsupportedOperationException(e);
		}
	}

	public static String urlEncodeUTF8(Map<?, ?> map) {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<?, ?> entry : map.entrySet()) {
			if (sb.length() > 0) {
				sb.append("&");
			}
			sb.append(String.format("%s=%s",
					urlEncodeUTF8(entry.getKey().toString()),
					urlEncodeUTF8(String.valueOf(entry.getValue()))));
		}
		return sb.toString();
	}

	public static String generateCookie(Map<?, ?> map) {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<?, ?> entry : map.entrySet()) {
			if (sb.length() > 0) {
				sb.append(";");
			}
			sb.append(String.format("%s=%s", entry.getKey().toString(),
					String.valueOf(entry.getValue())));
		}
		return sb.toString();
	}

	public static byte[] compressToGZIP(final String str) throws IOException {
		if ((str == null) || (str.length() == 0)) {
			return null;
		}
		ByteArrayOutputStream obj = new ByteArrayOutputStream();
		GZIPOutputStream gzip = new GZIPOutputStream(obj);
		gzip.write(str.getBytes("UTF-8"));
		gzip.close();
		return obj.toByteArray();
	}

	public static String decompressGZIP(final byte[] compressed)
			throws IOException {
		String outStr = "";
		if ((compressed == null) || (compressed.length == 0)) {
			return "";
		}
		if (isGZIPCompressed(compressed)) {
			GZIPInputStream gis = new GZIPInputStream(
					new ByteArrayInputStream(compressed));
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(gis, "UTF-8"));

			String line;
			while ((line = bufferedReader.readLine()) != null) {
				outStr += line;
			}
		} else {
			outStr = new String(compressed);
		}
		return outStr;
	}

	public static boolean isGZIPCompressed(final byte[] compressed) {
		return (compressed[0] == (byte) (GZIPInputStream.GZIP_MAGIC))
				&& (compressed[1] == (byte) (GZIPInputStream.GZIP_MAGIC >> 8));
	}
}
