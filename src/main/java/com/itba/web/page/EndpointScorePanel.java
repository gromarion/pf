package com.itba.web.page;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
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
import com.itba.formulae.GlobalFormulae;
import com.itba.web.WicketSession;

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
	@SpringBean
	private GlobalFormulae globalFormulae;
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
		WebMarkupContainer hasLicenseContainer = new WebMarkupContainer("hasLicense");
		WebMarkupContainer doesntHaveLicenseContainer = new WebMarkupContainer("doesntHaveLicense");
		WebMarkupContainer serverNormalContainer = new WebMarkupContainer("SPARQLServerNormal");
		WebMarkupContainer serverDownContainer = new WebMarkupContainer("SPARQLServerDown");
		hasLicenseContainer.setVisible(hasLicense);
		doesntHaveLicenseContainer.setVisible(!hasLicense);
		add(hasLicenseContainer);
		add(doesntHaveLicenseContainer);

		serverNormalContainer.setVisible(isAvailable);
		serverDownContainer.setVisible(!isAvailable);
		add(serverNormalContainer);
		add(serverDownContainer);

		endpointURLLabel.setDefaultModelObject(endpointScore.getEndpointURL());
		// add(endpointURLLabel);
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

		Map<String, Integer> statusCodesAmount = new HashMap<>();
		for (EndpointStats stats : endpointStats) {
			if (statusCodesAmount.containsKey(stats.getStatusCode())) {
				statusCodesAmount.put(stats.getStatusCode(), statusCodesAmount.get(stats.getStatusCode()) + 1);
			} else {
				statusCodesAmount.put(stats.getStatusCode(), 1);
			}
		}

		int totalResources = evaluatedResourceRepo.getAllByCampaign(endpointScore.getCampaign()).size();
		int erroredResources = evaluatedResourceRepo.getErroredByCampaign(endpointScore.getCampaign()).size();
		int correctResources = totalResources - erroredResources;

		response.render(JavaScriptHeaderItem
				.forReference(new JavaScriptResourceReference(ResultItemPage.class, "js/count-up.js")));
		response.render(JavaScriptHeaderItem
				.forReference(new JavaScriptResourceReference(ResultItemPage.class, "js/reports-panel.js")));
		double incorrectData = errorTypeStats.get(errorTypeStats.keySet().stream().filter(e -> e.getId() == 1).collect(Collectors.toList()).get(0));
		double incompleteData = errorTypeStats.get(errorTypeStats.keySet().stream().filter(e -> e.getId() == 2).collect(Collectors.toList()).get(0));
		double semanticallyIncorrect = errorTypeStats.get(errorTypeStats.keySet().stream().filter(e -> e.getId() == 3).collect(Collectors.toList()).get(0));
		double externalLink = errorTypeStats.get(errorTypeStats.keySet().stream().filter(e -> e.getId() == 4).collect(Collectors.toList()).get(0));
		response.render(OnDomReadyHeaderItem.forScript("initializeReportsPanel(" + endpointScore.getScoreString() + ", " + endpointScore.getSuccessfulRequestsRatio() + ", " + totalResources + ", " + globalFormulae.getAverageDocumentQuality() + ", " + incorrectData + ", " + incompleteData + ", " + semanticallyIncorrect + ", " + externalLink + ");"));
	}
}
