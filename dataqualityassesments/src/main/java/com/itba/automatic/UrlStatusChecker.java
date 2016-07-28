package com.itba.automatic;

import java.net.HttpURLConnection;
import java.net.URL;

public class UrlStatusChecker {
	public static int check (String urlString) {
		URL url;
		try {
			url = new URL(urlString);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();

			return connection.getResponseCode();
		} catch (Exception e) {
			e.printStackTrace();
			return 500;
		}
	}
}
