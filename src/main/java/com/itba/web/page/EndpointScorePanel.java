package com.itba.web.page;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.itba.domain.SparqlRequestHandler;
import com.itba.domain.model.EndpointStats;
import com.itba.domain.model.Error;
import com.itba.domain.repository.EndpointStatsRepo;
import com.itba.domain.repository.EvaluatedResourceRepo;
import com.itba.formulae.EndpointQualityFormulae.EndpointScore;
import com.itba.formulae.GlobalFormulae;

import lib.StringUtils;

@SuppressWarnings("serial")
public class EndpointScorePanel extends Panel {
	private final Map<String, String> errorColors;
	private final Label endpointURLLabel = new Label("endpointURL", "");
	private final Label endpointScoreLabel = new Label("endpointScore", "");

	private EndpointScore endpointScore;
	private EvaluatedResourceRepo evaluatedResourceRepo;
	@SpringBean
	private EndpointStatsRepo endpointStatsRepo;
	@SpringBean
	private GlobalFormulae globalFormulae;
	private int number;

	public EndpointScorePanel(String id, EndpointScore endpointScore, EvaluatedResourceRepo evaluatedResourceRepo,
			int number) {
		super(id);
		this.number = number;
		this.errorColors = new HashMap<>();
		errorColors.put("Tipodedatoincorrectamenteextraído", "#FF7777");
		errorColors.put("Valordelobjetoextraídodeformaincompleta", "#D4AB6A");
		errorColors.put("Objetosemánticamenteincorrecto", "#808015");
		errorColors.put("Enlaceexternoincorrecto", "#6DA398");
		this.endpointScore = endpointScore;
		this.evaluatedResourceRepo = evaluatedResourceRepo;

		boolean hasLicense;
		boolean isAvailable;
		try {
			hasLicense = SparqlRequestHandler.hasLicense(endpointScore.getCampaign(), endpointStatsRepo);
		} catch (IOException e) {
			hasLicense = false;
		}
		try {
			SparqlRequestHandler.requestSuggestions("", endpointScore.getCampaign(), endpointStatsRepo, 0, 1);
			isAvailable = true;
		} catch (IOException e) {
			isAvailable = false;
		}
		WebMarkupContainer globalGradePanel = new WebMarkupContainer("global-grade-panel");
		WebMarkupContainer documentQualityPanel = new WebMarkupContainer("document-quality-panel");
		WebMarkupContainer hasLicenseContainer = new WebMarkupContainer("hasLicense");
		WebMarkupContainer doesntHaveLicenseContainer = new WebMarkupContainer("doesntHaveLicense");
		WebMarkupContainer serverNormalContainer = new WebMarkupContainer("SPARQLServerNormal");
		WebMarkupContainer serverDownContainer = new WebMarkupContainer("SPARQLServerDown");
		hasLicenseContainer.setVisible(hasLicense);
		doesntHaveLicenseContainer.setVisible(!hasLicense);
		add(hasLicenseContainer);
		add(doesntHaveLicenseContainer);

		boolean anyEndpointStats = !endpointScore.getCampaign().getSessions().isEmpty();
		serverNormalContainer.setVisible(isAvailable);
		serverDownContainer.setVisible(!isAvailable);
		globalGradePanel.setVisible(anyEndpointStats);
		documentQualityPanel.setVisible(anyEndpointStats);

		add(serverNormalContainer);
		add(serverDownContainer);
		add(globalGradePanel);
		add(documentQualityPanel);

		double globalScore = globalFormulae.getGlobalScore();
		char globalGradeLetter = StringUtils.letterQualification(globalScore);

		List<Character> grades = Arrays.asList('a', 'b', 'c', 'd', 'f');
		for (Character grade : grades) {
			WebMarkupContainer gradeContainer = new WebMarkupContainer(grade + "-global-grade");
			Label gradeLabel = new Label(grade + "-global-grade-value", endpointScore.getScoreString());
			gradeLabel.add(new AttributeModifier("id", grade + "-global-grade-" +number));
			Label gradePercentageLabel = new Label(grade + "-global-grade-percentage", "%");
			if (Character.toUpperCase(grade) != globalGradeLetter) {
				gradeLabel.setVisible(false);
				gradePercentageLabel.setVisible(false);
				gradeContainer
						.add(new AttributeModifier("class", grade + "-grade circular-grade d-inline-block faded"));
			} else {
				gradeContainer.add(new AttributeModifier("class", grade + "-grade circular-grade d-inline-block"));
			}
			globalGradePanel.add(gradeLabel);
			globalGradePanel.add(gradePercentageLabel);
			globalGradePanel.add(gradeContainer);
		}
		Label endpointQuality = new Label("endpointQuality", 0);
		endpointQuality.add(new AttributeModifier("id", "endpoint-quality-" + number));
		
		Label endpointAvailability = new Label("endpointAvailability", 0);
		endpointAvailability.add(new AttributeModifier("id", "endpoint-availability-" + number));
		
		Label averageDocumentQuality = new Label("averageQuality", 0);
		averageDocumentQuality.add(new AttributeModifier("id", "average-quality-" + number));
		
		Label totalResources = new Label("totalResources", 0);
		totalResources.add(new AttributeModifier("id", "total-resources-" + number));
		
		Label incorrectData = new Label("incorrectData", 0);
		incorrectData.add(new AttributeModifier("id", "incorrect-data-" + number));
		
		Label incompleteData = new Label("incompleteData", 0);
		incompleteData.add(new AttributeModifier("id", "incomplete-data-" + number));
		
		Label semanticallyIncorrect = new Label("semanticallyIncorrect", 0);
		semanticallyIncorrect.add(new AttributeModifier("id", "semantically-incorrect-" + number));
		
		Label externalLink = new Label("externalLink", 0);
		externalLink.add(new AttributeModifier("id", "external-link-" + number));
		
		add(endpointQuality);
		add(endpointAvailability);
		documentQualityPanel.add(averageDocumentQuality);
		documentQualityPanel.add(totalResources);
		documentQualityPanel.add(incorrectData);
		documentQualityPanel.add(incompleteData);
		documentQualityPanel.add(semanticallyIncorrect);
		documentQualityPanel.add(externalLink);

		endpointURLLabel.setDefaultModelObject(endpointScore.getEndpointURL());
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

		response.render(JavaScriptHeaderItem
				.forReference(new JavaScriptResourceReference(ResultItemPage.class, "js/count-up.js")));
		response.render(JavaScriptHeaderItem
				.forReference(new JavaScriptResourceReference(ResultItemPage.class, "js/reports-panel.js")));
		double incorrectData = errorTypeStats
				.get(errorTypeStats.keySet().stream().filter(e -> e.getId() == 1).collect(Collectors.toList()).get(0));
		double incompleteData = errorTypeStats
				.get(errorTypeStats.keySet().stream().filter(e -> e.getId() == 2).collect(Collectors.toList()).get(0));
		double semanticallyIncorrect = errorTypeStats
				.get(errorTypeStats.keySet().stream().filter(e -> e.getId() == 3).collect(Collectors.toList()).get(0));
		double externalLink = errorTypeStats
				.get(errorTypeStats.keySet().stream().filter(e -> e.getId() == 4).collect(Collectors.toList()).get(0));
		double globalScore = globalFormulae.getGlobalScore();
		response.render(OnDomReadyHeaderItem.forScript("initializeReportsPanel(" + globalScore + ", '"
				+ StringUtils.letterQualification(globalScore) + "', " + endpointScore.getScoreString() + ", "
				+ endpointScore.getSuccessfulRequestsRatio() + ", " + totalResources + ", "
				+ (100 * globalFormulae.getAvarageResourceQuality()) + ", " + incorrectData + ", " + incompleteData
				+ ", " + semanticallyIncorrect + ", " + externalLink + ", " + number + ");"));
	}
}
