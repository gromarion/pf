package com.itba.web.page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

import com.itba.domain.model.EndpointStats;
import com.itba.domain.repository.EndpointStatsRepo;

public class EndpointScorePanel extends Panel {
	private static final long serialVersionUID = 1L;
	private static final String CHART_CONTAINER_ID = "#endpoint-availability-chart";

	private EndpointStatsRepo endpointStatsRepo;
	private String endpointUrl;

	public EndpointScorePanel(String id, EndpointStatsRepo endpointStatsRepo, String endpointUrl) {
		super(id);
		this.endpointStatsRepo = endpointStatsRepo;
		this.endpointUrl = endpointUrl;
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);

		List<EndpointStats> endpointStats = endpointStatsRepo.getAllForEndpoint(endpointUrl);

		if (endpointStats.isEmpty()) {
			return;
		}

		String errorsData = "[";
		Map<String, Integer> statusCodesAmount = new HashMap<>();
		for (EndpointStats stats : endpointStats) {
			if (statusCodesAmount.containsKey(stats.getStatusCode())) {
				statusCodesAmount.put(stats.getStatusCode(), statusCodesAmount.get(stats.getStatusCode()) + 1);
			} else {
				statusCodesAmount.put(stats.getStatusCode(), 1);
			}
		}

		for (String statusCode : statusCodesAmount.keySet()) {
			errorsData += "{'label': '" + statusCode + "', 'value': " + statusCodesAmount.get(statusCode) + "},";
		}

		errorsData = errorsData.substring(0, errorsData.length() - 1);
		errorsData += "]";

		response.render(JavaScriptHeaderItem
				.forReference(new JavaScriptResourceReference(ResultItemPage.class, "js/d3.min.js")));
		response.render(JavaScriptHeaderItem
				.forReference(new JavaScriptResourceReference(ResultItemPage.class, "js/donut-chart.js")));
		response.render(OnDomReadyHeaderItem
				.forScript("drawChart(" + errorsData + ", '" + CHART_CONTAINER_ID + "');"));
	}
}
