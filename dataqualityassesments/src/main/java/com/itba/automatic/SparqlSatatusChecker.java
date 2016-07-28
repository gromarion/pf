package com.itba.automatic;

import java.net.HttpURLConnection;
import java.net.URL;

import com.itba.sparql.Endpoint;

public class SparqlSatatusChecker {
	public static boolean check (String endpointUrl, String graphs, String name) {
		Endpoint endpoint = new Endpoint(0, endpointUrl, graphs, name);
		String queryURL = endpoint.generateQueryURL("ASK {}");
		URL url;
		try {
			url = new URL(queryURL);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();
			
			return connection.getResponseCode() == 200;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
