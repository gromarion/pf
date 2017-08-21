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
import com.itba.web.charts.DonutChartWithLabels;
import com.itba.web.charts.GaugeChart;

@SuppressWarnings("serial")
public class EndpointScorePanel extends Panel {
	private final Map<String, String> errorColors;
	
	private EndpointScore endpointScore;

	public EndpointScorePanel(String id, EndpointScore endpointScore) {
		super(id);
		this.errorColors = new HashMap<>();
		errorColors.put("Tipodedatoincorrectamenteextraído", "#FF7777");
		errorColors.put("Valordelobjetoextraídodeformaincompleta", "#D4AB6A");
		errorColors.put("Objetosemánticamenteincorrecto", "#808015");
		errorColors.put("Enlaceexternoincorrecto", "#6DA398");
		this.endpointScore = endpointScore;
		
		add(new Label("endpointURL", endpointScore.getEndpointURL()));
		add(new Label("endpointScore", endpointScore.getScore()));
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);

		List<EndpointStats> endpointStats = endpointScore.getEndpointStats();
		Map<Error, Double> errorTypeStats = endpointScore.getErrorTypeStats();
		
		if (endpointStats.isEmpty() && errorTypeStats.isEmpty()) {
			return;
		}

		DonutChartWithLabels endpointStatsChart = new DonutChartWithLabels("endpoint-availability-chart");
		GaugeChart errorTypeChart = new GaugeChart();
		
		Map<String, Integer> statusCodesAmount = new HashMap<>();
		for (EndpointStats stats : endpointStats) {
			if (statusCodesAmount.containsKey(stats.getStatusCode())) {
				statusCodesAmount.put(stats.getStatusCode(), statusCodesAmount.get(stats.getStatusCode()) + 1);
			} else {
				statusCodesAmount.put(stats.getStatusCode(), 1);
			}
		}

		for (String statusCode : statusCodesAmount.keySet()) {
			endpointStatsChart.appendData(statusCode, statusCodesAmount.get(statusCode));
		}
		for (Error e : errorTypeStats.keySet()) {
			String id = e.getName().replaceAll(" ", "");
			errorTypeChart.appendData(id, errorTypeStats.get(e), errorColors.get(id), errorColors.get(id), errorColors.get(id));
		}

		response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(ResultItemPage.class, "js/d3.min.js")));
		response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(ResultItemPage.class, "js/donut-chart.js")));
		response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(ResultItemPage.class, "js/gauge-chart.js")));
		response.render(endpointStatsChart.getRender());
		response.render(errorTypeChart.getRender());
	}
}
