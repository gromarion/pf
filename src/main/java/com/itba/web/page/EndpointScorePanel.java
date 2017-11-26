package com.itba.web.page;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.itba.domain.SparqlRequestHandler;
import com.itba.domain.model.Campaign;
import com.itba.domain.model.EndpointStats;
import com.itba.domain.model.Error;
import com.itba.domain.repository.EndpointStatsRepo;
import com.itba.domain.repository.EvaluatedResourceRepo;
import com.itba.formulae.EndpointQualityFormulae.EndpointScore;
import com.itba.web.WicketSession;
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
	@SpringBean
	private EndpointStatsRepo endpointStatsRepo;
	private final Label endpointURLLabel = new Label("endpointURL", "");
	private final Label endpointScoreLabel = new Label("endpointScore", "");


	public EndpointScorePanel(String id, EndpointScore endpointScore, EvaluatedResourceRepo evaluatedResourceRepo) {
		super(id);
		this.errorColors = new HashMap<>();
		errorColors.put("Tipodedatoincorrectamenteextraído", "#FF7777");
		errorColors.put("Valordelobjetoextraídodeformaincompleta", "#D4AB6A");
		errorColors.put("Objetosemánticamenteincorrecto", "#808015");
		errorColors.put("Enlaceexternoincorrecto", "#6DA398");
		this.endpointScore = endpointScore;
		this.evaluatedResourceRepo = evaluatedResourceRepo;
		
		Campaign campaign = WicketSession.get().getEvaluationSession().get().getCampaign();
		boolean hasLicense;
		boolean isAvailable;
		try {
			hasLicense = SparqlRequestHandler.hasLicense(campaign, endpointStatsRepo);
		} catch (IOException e) {
			hasLicense = false;
		}
		try {
			SparqlRequestHandler.requestSuggestions("", campaign, endpointStatsRepo, 0, 1);
			isAvailable = true;
		} catch (IOException e) {
			isAvailable = false;
		}
		Label hasLicenseLabel = new Label("hasLicense", getString("yesLicense"));
		Label doesntHaveLiceseLabel = new Label("doesntHaveLicense", getString("noLicense"));
		Label serverNormalLabel = new Label("SPARQLServerNormal", getString("endpointUp"));
		Label serverDownLabel = new Label("SPARQLServerDown", getString("endpointDown"));
		hasLicenseLabel.setVisible(hasLicense);
		doesntHaveLiceseLabel.setVisible(!hasLicense);
		add(hasLicenseLabel);
		add(doesntHaveLiceseLabel);
		
		serverNormalLabel.setVisible(isAvailable);
		serverDownLabel.setVisible(!isAvailable);
		add(serverNormalLabel);
		add(serverDownLabel);

		endpointURLLabel.setDefaultModelObject(endpointScore.getEndpointURL());
		endpointScoreLabel.setDefaultModelObject(endpointScore.getScoreString());
		add(endpointURLLabel);
		add(endpointScoreLabel);
	}

	public void setEndpointScore(EndpointScore endpointScore) {
		this.endpointScore = endpointScore;
		endpointURLLabel.setDefaultModelObject(endpointScore.getEndpointURL());
		endpointScoreLabel.setDefaultModelObject(endpointScore.getScoreString());
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

		int totalResources = evaluatedResourceRepo.getAllByCampaign(endpointScore.getCampaign()).size();
		int erroredResources = evaluatedResourceRepo.getErroredByCampaign(endpointScore.getCampaign()).size();
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
