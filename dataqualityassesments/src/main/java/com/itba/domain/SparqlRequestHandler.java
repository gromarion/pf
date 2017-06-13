package com.itba.domain;

import java.io.IOException;

import org.apache.wicket.ajax.json.JSONArray;
import org.apache.wicket.ajax.json.JSONException;
import org.apache.wicket.ajax.json.JSONObject;

import com.itba.domain.model.Campaign;
import com.itba.domain.repository.EndpointStatsRepo;
import com.itba.networking.RequestHandler;

public class SparqlRequestHandler {

	public static JSONArray requestSuggestions(String search, Campaign campaign, EndpointStatsRepo endpointStatsRepo,
			int offset, int limit) throws JSONException, IOException {
		String query = campaign.getQueryforSearchResultPage(search, offset, limit);
		String queryURL = campaign.generateQueryURL(query);
		JSONObject response = RequestHandler.jsonSendGet(endpointStatsRepo, campaign.getEndpoint(), queryURL);

		return (JSONArray) ((JSONObject) response.get("results")).get("bindings");
	}

	public static JSONObject requestResource(String resource, Campaign campaign, EndpointStatsRepo endpointStatsRepo)
			throws JSONException, IOException {
		String sparqlQuery = campaign.getQueryforResourceTriples(resource);
		String queryURL = campaign.generateQueryURL(sparqlQuery);

		return RequestHandler.jsonSendGet(endpointStatsRepo, campaign.getEndpoint(), queryURL);
	}

	public static JSONObject requestRandomResource(Campaign campaign, EndpointStatsRepo endpointStatsRepo)
			throws JSONException, IOException {
		String sparqlQuery = campaign.getQueryforRandomResource();
		String queryURL = campaign.generateQueryURL(sparqlQuery);
		JSONObject response = RequestHandler.jsonSendGet(endpointStatsRepo, campaign.getEndpoint(), queryURL);

		return (JSONObject) ((JSONArray) ((JSONObject) response.get("results")).get("bindings")).get(0);
	}
}