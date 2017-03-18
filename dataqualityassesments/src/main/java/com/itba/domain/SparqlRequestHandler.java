package com.itba.domain;

import org.apache.wicket.ajax.json.JSONArray;
import org.apache.wicket.ajax.json.JSONObject;

import com.itba.domain.model.Campaign;
import com.itba.networking.RequestHandler;

public class SparqlRequestHandler {

    public static JSONArray requestSuggestions(String text, Campaign campaign) {
    	if (text.length() <= 2) {
    		return new JSONArray();
    	}
        String query = campaign.getQueryforAutocomplete(text);
        String queryURL = campaign.generateQueryURL(query);
        JSONObject response = RequestHandler.jsonSendGet(queryURL);

        return (JSONArray) ((JSONObject) response.get("results")).get("bindings");
    }
    
    public static JSONObject requestResource(String resource, Campaign campaign) {
    	String sparqlQuery = campaign.getQueryforResourceTriples(resource);
        String queryURL = campaign.generateQueryURL(sparqlQuery);
        JSONObject response = RequestHandler.jsonSendGet(queryURL);
        return response;
    }
}