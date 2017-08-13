package com.itba.web.page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

import com.itba.EndpointQualityFormulae.EndpointScore;
import com.itba.domain.model.EndpointStats;
import com.itba.domain.model.Error;
import com.itba.web.graphics.DonutChartWithLabels;

@SuppressWarnings("serial")
public class EndpointScorePanel extends Panel {
	
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

		DonutChartWithLabels endpointStatsChart = new DonutChartWithLabels("endpoint-availability-chart");
		DonutChartWithLabels errorTypeChart = new DonutChartWithLabels("endpoint-globaldocs-chart");
		
		Map<String, Integer> statusCodesAmount = new HashMap<>();
		for (EndpointStats stats : endpointStats) {
			if (statusCodesAmount.containsKey(stats.getStatusCode())) {
				statusCodesAmount.put(stats.getStatusCode(), statusCodesAmount.get(stats.getStatusCode()) + 1);
			} else {
				statusCodesAmount.put(stats.getStatusCode(), 1);
			}
		}

		for (String statusCode : statusCodesAmount.keySet()) {
			endpointStatsChart.appendData(statusCode, statusCodesAmount.get(statusCode).toString());
		}
		for (Error e : errorTypeStats.keySet()) {
			errorTypeChart.appendData(e.getName(), errorTypeStats.get(e).toString());
		}

		response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(ResultItemPage.class, "js/d3.min.js")));
		response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(ResultItemPage.class, "js/donut-chart.js")));
		response.render(endpointStatsChart.getRender());
		response.render(errorTypeChart.getRender());
	}
}
