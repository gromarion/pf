package com.itba.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

import sparql.Endpoint;
import sparql.JsonSparqlResult;
import sparql.ResultItem;

public class SparqlSuggestOracle {
	
	private static boolean responseReceived;

	public static List<Suggestion> requestSuggestions(String text) {
		Endpoint endpoint = new Endpoint(
            0,
            "http://live.dbpedia.org/sparql",
            "http://dbpedia.org",
            "live.dbpedia.org/sparql"
        );
		final List<Suggestion> suggestions = new ArrayList<Suggestion>();
        if (text.length() > 2) {
            try {
                String query = endpoint.getQueryforAutocomplete(text);
                String queryURL = endpoint.generateQueryURL(query);

                RequestBuilder rb = new RequestBuilder(RequestBuilder.GET, queryURL);
                rb.setCallback(new com.google.gwt.http.client.RequestCallback() {
                    public void onResponseReceived(com.google.gwt.http.client.Request req, com.google.gwt.http.client.Response res) {
                        try {
                            JsonSparqlResult result = new JsonSparqlResult(res.getText());
                            for (List<ResultItem> i : result.data) {
                                if (i.size() == 1) {
                                    suggestions.add(new SparqlSuggestItem(i.get(0).value));
                                }
                            }
                            responseReceived = true;
                        } catch (Exception e) {
                            e.printStackTrace();
                            Window.alert("Error communicating with SPARQL Endpoint!");
                        }
                    }

                    public void onError(com.google.gwt.http.client.Request request, Throwable exception) {
                    }
                });
                rb.send();
                while(!responseReceived) {
                }
                return suggestions;
            } catch (RequestException e) {
                Window.alert("Error occurred" + e.getMessage());
                return suggestions;
            }
        }
        return suggestions;
    }
	
	public static class SparqlSuggestItem implements SuggestOracle.Suggestion, Serializable {
        private static final long serialVersionUID = 1L;
        public String uri;

        public SparqlSuggestItem(String uri) {
            this.uri = uri;
        }

        public String getDisplayString() {
            return this.uri;
        }

        public String getReplacementString() {
            return this.uri;
        }
    }
}
