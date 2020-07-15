package com.fongmi.android.ltv.utils;

import android.webkit.URLUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {

	private static final String CONTENT_DISPOSITION = "Content-Disposition";
	private static final String FORCE_DOWNLOAD = "force-download";

	public static HttpURLConnection connect(String url) throws IOException {
		HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
		conn.setInstanceFollowRedirects(false);
		conn.setConnectTimeout(5000);
		conn.setReadTimeout(5000);
		conn.setUseCaches(false);
		conn.setDoInput(false);
		conn.setDoInput(true);
		conn.connect();
		return conn;
	}

	public static String download(HttpURLConnection conn) throws IOException {
		String disposition = conn.getHeaderField(CONTENT_DISPOSITION);
		String fileName = URLUtil.guessFileName(null, disposition, null);
		File cacheFile = FileUtil.getCacheFile(fileName);
		InputStream is = conn.getInputStream();
		int bytesRead;
		byte[] buffer = new byte[1024];
		FileOutputStream os = new FileOutputStream(cacheFile);
		while (((bytesRead = is.read(buffer)) != -1)) os.write(buffer, 0, bytesRead);
		os.close();
		is.close();
		return cacheFile.getPath();
	}

	public static void download(String url) throws IOException {
		HttpURLConnection conn = connect(url);
		if (isFile(conn)) download(conn);
	}

	public static boolean isRedirect(HttpURLConnection conn) throws IOException {
		return conn.getResponseCode() / 100 == 3;
	}

	public static boolean isFile(HttpURLConnection conn) {
		return conn.getContentType().contains(FORCE_DOWNLOAD);
	}
}
