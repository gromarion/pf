package com.itba.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.apache.wicket.ajax.json.JSONException;
import org.apache.wicket.ajax.json.JSONObject;

import com.itba.domain.model.EndpointStats;
import com.itba.domain.repository.EndpointStatsRepo;

import lib.Cache;

public class RequestHandler {
	public static String sendGet(EndpointStatsRepo endpointStatsRepo, String endpointURL, String url)
			throws IOException {
		Cache cache = Cache.getInstance();
		String cacheKey = endpointURL + url;
		Object object = cache.get(cacheKey);
		if (object != null) {
			return object.toString();
		}
		URL obj;
		obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		Map<String, List<String>> map = con.getHeaderFields();

		for (Map.Entry<String, List<String>> entry : map.entrySet()) {
			for (String value : entry.getValue()) {
				if (value.contains("HTTP/1.1")) {
					registerResponseCode(endpointURL, entry.getValue(), endpointStatsRepo);
				}				
			}
		}

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		String stringResponse = response.toString();
		cache.put(cacheKey, stringResponse);
		return stringResponse;
	}

	public static JSONObject jsonSendGet(EndpointStatsRepo endpointStatsRepo, String endpointURL, String url)
			throws JSONException, IOException {
		return new JSONObject(sendGet(endpointStatsRepo, endpointURL, url));
	}

	private static void registerResponseCode(String endpointURL, List<String> entryValue,
			EndpointStatsRepo endpointStatsRepo) {
		String responseCode = entryValue.get(0).split(" ")[1];
		EndpointStats stats = new EndpointStats(endpointURL, responseCode, System.currentTimeMillis());

		endpointStatsRepo.save(stats);
	}
}
