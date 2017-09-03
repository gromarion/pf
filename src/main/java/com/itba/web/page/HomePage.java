package com.itba.web.page;

import java.io.IOException;

import org.apache.wicket.spring.injection.annot.SpringBean;

import com.itba.domain.repository.EvaluatedResourceRepo;
import com.itba.formulae.EndpointQualityFormulae;
import com.itba.formulae.EndpointQualityFormulae.EndpointScore;

public class HomePage extends BasePage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private EndpointQualityFormulae endpointQualityFormulae;

	@SpringBean
	private EvaluatedResourceRepo evaluatedResourceRepo;

	@Override
	protected void onInitialize() {
		super.onInitialize();

		EndpointScore endpointScore;
		try {
			endpointScore = endpointQualityFormulae.getScore();
			EndpointScorePanel endpointScorePanel = new EndpointScorePanel("endpointScorePanel", endpointScore,
					evaluatedResourceRepo);

			endpointScorePanel.setVisible(!endpointScore.getEndpointStats().isEmpty());
			add(endpointScorePanel);
		} catch (IOException e) {
			setResponsePage(ErrorPage.class);
			e.printStackTrace();
		}
	}
}
