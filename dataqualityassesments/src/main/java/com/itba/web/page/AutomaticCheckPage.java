package com.itba.web.page;

import java.io.IOException;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.spring.injection.annot.SpringBean;
import com.itba.domain.SparqlRequestHandler;
import com.itba.domain.model.Campaign;
import com.itba.domain.repository.CampaignRepo;
import com.itba.domain.repository.EndpointStatsRepo;
import com.itba.web.WicketSession;

@SuppressWarnings("serial")
public class AutomaticCheckPage extends BasePage {
	private static final long serialVersionUID = 1L;
	
	@SpringBean
	private EndpointStatsRepo endpointStatsRepo;
	@SpringBean
	private CampaignRepo campaignRepo;

	@Override
    protected void onInitialize() {
		super.onInitialize();
		Campaign campaign = WicketSession.get().getEvaluationSession().get().getCampaign();
		String hasLicense;
		try {
			hasLicense = SparqlRequestHandler.hasLicense(campaign, endpointStatsRepo) ? "Si" : "No";
		} catch (IOException e) {
			hasLicense = "No";
			add(new Label("SPARQLServerStatus", "Normal"));
		}
		try {
			SparqlRequestHandler.requestSuggestions("", campaign, endpointStatsRepo, 0, 1);
			add(new Label("SPARQLServerStatus", "Normal"));
		} catch (IOException e) {
			add(new Label("SPARQLServerStatus", "Caído"));
		}
		add(new Label("hasLicense", hasLicense));
		
		Link<Void> backButton = new Link<Void>("back") {
			@Override
			public void onClick() {
				setResponsePage(AutomaticOrManualPage.class);
			}
		};
		
		add(backButton);
	}
}
