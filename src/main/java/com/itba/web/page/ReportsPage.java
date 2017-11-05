package com.itba.web.page;

import java.io.IOException;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.itba.domain.model.User;
import com.itba.domain.repository.EvaluatedResourceRepo;
import com.itba.formulae.EndpointQualityFormulae;
import com.itba.formulae.EndpointQualityFormulae.EndpointScore;
import com.itba.web.WicketSession;

@SuppressWarnings("serial")
@AuthorizeInstantiation({User.EVALUATOR_ROLE, User.ADMIN_ROLE})
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
			// TODO: y si acá adentro se le pasan distintas campañas?
			endpointScore = endpointQualityFormulae.getScore(WicketSession.get().getEvaluationSession().get().getCampaign());
			EndpointScorePanel endpointScorePanel = new EndpointScorePanel("endpointScorePanel", endpointScore, evaluatedResourceRepo);
			endpointScorePanel.setVisible(!endpointScore.getEndpointStats().isEmpty());
			add(endpointScorePanel);
		} catch (IOException e) {
			setResponsePage(ErrorPage.class);
			e.printStackTrace();
		}
	}
}
