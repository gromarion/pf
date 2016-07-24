package com.itba.domain;

import org.apache.wicket.ajax.json.JSONArray;
import org.apache.wicket.ajax.json.JSONObject;

import com.itba.networking.RequestHandler;
import com.itba.sparql.Endpoint;

public class SparqlSuggestOracle {

    public static JSONArray requestSuggestions(String text) {
    	if (text.length() <= 2) {
    		return new JSONArray();
    	}
        Endpoint endpoint = new Endpoint(
                0,
                "http://live.dbpedia.org/sparql",
                "http://dbpedia.org",
                "live.dbpedia.org/sparql"
        );
        String query = endpoint.getQueryforAutocomplete(text);
        String queryURL = endpoint.generateQueryURL(query);
        JSONObject response = RequestHandler.sendGet(queryURL);

        return (JSONArray) ((JSONObject) response.get("results")).get("bindings");
    }
}