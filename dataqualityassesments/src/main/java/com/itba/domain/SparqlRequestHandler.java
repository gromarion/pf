package com.itba.domain;

import org.apache.wicket.ajax.json.JSONArray;
import org.apache.wicket.ajax.json.JSONObject;

import com.itba.domain.model.Campaign;
import com.itba.networking.RequestHandler;
import com.itba.web.WicketSession;

public class SparqlRequestHandler {

    public static JSONArray requestSuggestions(String text) {
    	if (text.length() <= 2) {
    		return new JSONArray();
    	}
        String query = WicketSession.get().getEvaluationSession().get().getCampaign().getQueryforAutocomplete(text);
        String queryURL = WicketSession.get().getEvaluationSession().get().getCampaign().generateQueryURL(query);
        JSONObject response = RequestHandler.jsonSendGet(queryURL);

        return (JSONArray) ((JSONObject) response.get("results")).get("bindings");
    }
    
    public static JSONObject requestResource(String resource) {
    	String sparqlQuery = WicketSession.get().getEvaluationSession().get().getCampaign().getQueryforResourceTriples(resource);
        String queryURL = WicketSession.get().getEvaluationSession().get().getCampaign().generateQueryURL(sparqlQuery);
        JSONObject response = RequestHandler.jsonSendGet(queryURL);
        return response;
    }
}