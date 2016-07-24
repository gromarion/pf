package com.itba.domain;

import org.apache.wicket.ajax.json.JSONArray;
import org.apache.wicket.ajax.json.JSONObject;
import sparql.Endpoint;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SparqlSuggestOracle {

    public static JSONArray requestSuggestions(String text) {
        Endpoint endpoint = new Endpoint(
                0,
                "http://live.dbpedia.org/sparql",
                "http://dbpedia.org",
                "live.dbpedia.org/sparql"
        );
        if (text.length() > 2) {
            String query = endpoint.getQueryforAutocomplete(text);
            String queryURL = endpoint.generateQueryURL(query);

            try {
//                JsonSparqlResult result = new JsonSparqlResult(res.getText());
//                for (List<ResultItem> i : result.data) {
//                    if (i.size() == 1) {
//                        suggestions.add(i.get(0).value);
//                    }
//                }
//                responseReceived = true;
                JSONObject response = sendGet(queryURL);
                return (JSONArray) ((JSONObject) response.get("results")).get("bindings");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new JSONArray();
        }
        return new JSONArray();
    }

    private static JSONObject sendGet(String url) throws Exception {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return new JSONObject(response.toString());
    }
}