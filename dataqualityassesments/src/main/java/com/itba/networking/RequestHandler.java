package com.itba.networking;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.wicket.ajax.json.JSONObject;

public class RequestHandler {
	public static JSONObject sendGet(String url) {
		try {
			URL obj;
			obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			
			BufferedReader in = new BufferedReader(
					new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			return new JSONObject(response.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    }
}
