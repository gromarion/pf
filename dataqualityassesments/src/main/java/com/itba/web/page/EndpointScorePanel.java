package com.itba.web.page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

import com.itba.EndpointQualityFormulae.EndpointScore;
import com.itba.domain.model.EndpointStats;
import com.itba.domain.model.Error;

public class EndpointScorePanel extends Panel {
	private static final long serialVersionUID = 1L;
	private static final String CHART_CONTAINER_ID = "#endpoint-availability-chart";
	private static final String CHART_CONTAINER_ID_2 = "#endpoint-globaldocs-chart";

	private EndpointScore endpointScore;

	public EndpointScorePanel(String id, EndpointScore endpointScore) {
		super(id);
		this.endpointScore = endpointScore;
		
		add(new Label("endpointURL", endpointScore.getEndpointURL()));
		add(new Label("endpointScore", endpointScore.getScore()));
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);

		List<EndpointStats> endpointStats = endpointScore.getEndpointStats();
		Map<Error, Long> errorTypeStats = endpointScore.getErrorTypeStats();
		
		if (endpointStats.isEmpty() && errorTypeStats.isEmpty()) {
			return;
		}

		String endpointStatsData = "[";
		Map<String, Integer> statusCodesAmount = new HashMap<>();
		for (EndpointStats stats : endpointStats) {
			if (statusCodesAmount.containsKey(stats.getStatusCode())) {
				statusCodesAmount.put(stats.getStatusCode(), statusCodesAmount.get(stats.getStatusCode()) + 1);
			} else {
				statusCodesAmount.put(stats.getStatusCode(), 1);
			}
		}

		for (String statusCode : statusCodesAmount.keySet()) {
			endpointStatsData += "{'label': '" + statusCode + "', 'value': " + statusCodesAmount.get(statusCode) + "},";
		}

		endpointStatsData = endpointStatsData.substring(0, endpointStatsData.length() - 1);
		endpointStatsData += "]";
		
		StringBuilder errorTypeData = new StringBuilder("[");
		for (Error e : errorTypeStats.keySet()) {
			errorTypeData.append("{'label': '" + e.getName() + "', 'value': " + errorTypeStats.get(e) + "},");
		}
		errorTypeData.deleteCharAt(errorTypeData.length() - 1);
		errorTypeData.append("]");
		
		response.render(JavaScriptHeaderItem
				.forReference(new JavaScriptResourceReference(ResultItemPage.class, "js/d3.min.js")));
		response.render(JavaScriptHeaderItem
				.forReference(new JavaScriptResourceReference(ResultItemPage.class, "js/donut-chart.js")));
		response.render(OnDomReadyHeaderItem
				.forScript("drawChart(" + endpointStatsData + ", '" + CHART_CONTAINER_ID + "');"));
		response.render(OnDomReadyHeaderItem
				.forScript("drawChart(" + errorTypeData.toString() + ", '" + CHART_CONTAINER_ID_2 + "');"));
		
		
	}
}
