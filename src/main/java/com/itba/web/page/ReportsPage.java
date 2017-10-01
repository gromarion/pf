package com.itba.web.page;

import java.io.IOException;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.itba.domain.repository.EvaluatedResourceRepo;
import com.itba.formulae.EndpointQualityFormulae;
import com.itba.formulae.EndpointQualityFormulae.EndpointScore;

@SuppressWarnings("serial")
@AuthorizeInstantiation({"EVALUATOR", "ADMIN"})
public class ReportsPage extends BasePage {

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
