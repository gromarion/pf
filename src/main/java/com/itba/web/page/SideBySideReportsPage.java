package com.itba.web.page;

import java.io.IOException;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.google.common.base.Strings;
import com.itba.domain.model.Campaign;
import com.itba.domain.repository.CampaignRepo;
import com.itba.domain.repository.EvaluatedResourceRepo;
import com.itba.formulae.EndpointQualityFormulae;
import com.itba.formulae.EndpointQualityFormulae.EndpointScore;
import com.itba.web.WicketSession;

@SuppressWarnings("serial")
public class SideBySideReportsPage extends BasePage {

	@SpringBean
	private EvaluatedResourceRepo evaluatedResourceRepo;
	@SpringBean
	private EndpointQualityFormulae endpointQualityFormulae;
	@SpringBean
	private CampaignRepo campaigns;

	public SideBySideReportsPage(PageParameters parameters) {
		Campaign campaignA = getCampaign(parameters, "campaignIdA");
		Campaign campaignB = getCampaign(parameters, "campaignIdB");

		try {
			EndpointScore endpointAScore = endpointQualityFormulae.getScore(campaignA);
			EndpointScore endpointBScore = endpointQualityFormulae.getScore(campaignB);
			final EndpointScorePanel endpointAScorePanel = new EndpointScorePanel("endpointAScorePanel", endpointAScore,
					evaluatedResourceRepo, 1);
			final EndpointScorePanel endpointBScorePanel = new EndpointScorePanel("endpointBScorePanel", endpointBScore,
					evaluatedResourceRepo, 2);
			add(endpointAScorePanel);
			add(endpointBScorePanel);
			add(new Label("endpointAName", campaignA.getName()));
			add(new Label("endpointBName", campaignB.getName()));
		} catch (IOException e) {
			setResponsePage(ErrorPage.class);
		}
	}

	private Campaign getCampaign(PageParameters parameters, String campaignParam) {
		return Strings.isNullOrEmpty(parameters.get(campaignParam).toString())
				? WicketSession.get().getEvaluationSession().get().getCampaign()
				: campaigns.get(parameters.get(campaignParam).toInt());
	}
}
