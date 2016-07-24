package com.itba.domain;

import javax.xml.ws.Response;

import org.aksw.TripleCheckMate.shared.evaluate.SessionContext;
import org.apache.wicket.ajax.json.JSONArray;
import org.apache.wicket.ajax.json.JSONObject;

import com.itba.networking.RequestHandler;
import com.itba.sparql.Endpoint;

public class SparqlRequestHandler {

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
        JSONObject response = RequestHandler.jsonSendGet(queryURL);

        return (JSONArray) ((JSONObject) response.get("results")).get("bindings");
    }
    
    public static JSONObject requestResource(String resource) {
    	Endpoint endpoint = new Endpoint(
                0,
                "http://live.dbpedia.org/sparql",
                "http://dbpedia.org",
                "live.dbpedia.org/sparql"
        );
    	String sparqlQuery = endpoint.getQueryforResourceTriples(resource);
        String queryURL = endpoint.generateQueryURL(sparqlQuery);
        JSONObject response = RequestHandler.jsonSendGet(queryURL);
        return response;
    }
}