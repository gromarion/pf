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

import com.itba.domain.model.EndpointStats;
import com.itba.domain.model.Error;
import com.itba.domain.model.EvaluatedResource;
import com.itba.domain.repository.EvaluatedResourceRepo;
import com.itba.formulae.EndpointQualityFormulae.EndpointScore;
import com.itba.web.charts.DonutChartWithLabels;
import com.itba.web.charts.GaugeChart;

@SuppressWarnings("serial")
public class EndpointScorePanel extends Panel {
	private static final double DURATION = 2.5;
	private static final String TOTAL_RESOURCES_ID = "totalResources";
	private static final String ERRORED_RESOURCES_ID = "erroredResources";
	private static final String CORRECT_RESOURCES_ID = "correctResources";

	private final Map<String, String> errorColors;

	private EndpointScore endpointScore;
	private EvaluatedResourceRepo evaluatedResourceRepo;

	public EndpointScorePanel(String id, EndpointScore endpointScore, EvaluatedResourceRepo evaluatedResourceRepo) {
		super(id);
		this.errorColors = new HashMap<>();
		errorColors.put("Tipodedatoincorrectamenteextraído", "#FF7777");
		errorColors.put("Valordelobjetoextraídodeformaincompleta", "#D4AB6A");
		errorColors.put("Objetosemánticamenteincorrecto", "#808015");
		errorColors.put("Enlaceexternoincorrecto", "#6DA398");
		this.endpointScore = endpointScore;
		this.evaluatedResourceRepo = evaluatedResourceRepo;

		add(new Label("endpointURL", endpointScore.getEndpointURL()));
		add(new Label("endpointScore", endpointScore.getScoreString()));
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
			errorTypeChart.appendData(id, errorTypeStats.get(e), errorColors.get(id), errorColors.get(id),
					errorColors.get(id));
		}

		List<EvaluatedResource> resources = evaluatedResourceRepo.getAll();
		int totalResources = resources.size();
		int erroredResources = evaluatedResourceRepo.getErrored().size();
		int correctResources = totalResources - erroredResources;

		response.render(JavaScriptHeaderItem
				.forReference(new JavaScriptResourceReference(ResultItemPage.class, "js/d3.min.js")));
		response.render(JavaScriptHeaderItem
				.forReference(new JavaScriptResourceReference(ResultItemPage.class, "js/donut-chart.js")));
		response.render(JavaScriptHeaderItem
				.forReference(new JavaScriptResourceReference(ResultItemPage.class, "js/gauge-chart.js")));
		response.render(JavaScriptHeaderItem
				.forReference(new JavaScriptResourceReference(ResultItemPage.class, "js/count-up.js")));
		response.render(endpointStatsChart.getRender());
		response.render(errorTypeChart.getRender());
		response.render(OnDomReadyHeaderItem.forScript(
				"(new CountUp('" + TOTAL_RESOURCES_ID + "', 0, " + totalResources + ", 0, " + DURATION + ")).start();"));
		response.render(OnDomReadyHeaderItem.forScript(
				"(new CountUp('" + ERRORED_RESOURCES_ID + "', 0, " + erroredResources + ", 0, " + DURATION + ")).start();"));
		response.render(OnDomReadyHeaderItem.forScript(
				"(new CountUp('" + CORRECT_RESOURCES_ID + "', 0, " + correctResources + ", 0, " + DURATION + ")).start();"));
	}
}
