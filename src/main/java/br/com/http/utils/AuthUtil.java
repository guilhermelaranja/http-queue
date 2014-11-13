package br.com.http.utils;

import java.io.IOException;
import java.util.Properties;

public final class AuthUtil {

	private static String hashKey;

	private AuthUtil() {
	}

	public static String getAuthHash() throws IOException {
		if (hashKey == null) {
			Properties properties = new Properties();
			properties.load(AuthUtil.class.getResourceAsStream("/http-queue.properties"));
			hashKey = properties.getProperty("authHash");
			return hashKey;
		}
		return hashKey;
	}
}
