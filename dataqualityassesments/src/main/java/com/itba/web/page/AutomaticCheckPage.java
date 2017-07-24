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
		Label hasLicenseLabel = new Label("hasLicense", "Si");
		Label doesntHaveLiceseLabel = new Label("doesntHaveLicense", "No");
		Label serverNormalLabel = new Label("SPARQLServerNormal", "Normal");
		Label serverDownLabel = new Label("SPARQLServerDown", "Caído");
		hasLicenseLabel.setVisible(hasLicense);
		doesntHaveLiceseLabel.setVisible(!hasLicense);
		add(hasLicenseLabel);
		add(doesntHaveLiceseLabel);
		
		serverNormalLabel.setVisible(isAvailable);
		serverDownLabel.setVisible(!isAvailable);
		add(serverNormalLabel);
		add(serverDownLabel);
		
		Link<Void> backButton = new Link<Void>("back") {
			@Override
			public void onClick() {
				setResponsePage(AutomaticOrManualPage.class);
			}
		};
		
		add(backButton);
	}
}
