package com.itba.web.page;

import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.spring.injection.annot.SpringBean;
import com.itba.domain.model.Campaign;
import com.itba.domain.repository.CampaignRepo;
import com.itba.domain.repository.EndpointStatsRepo;
import com.itba.web.WicketSession;
import lib.EndpointQualityFormulae;
import lib.EndpointQualityFormulae.EndpointScore;

public class AutomaticOrManualPage extends BasePage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private CampaignRepo campaignRepo;
	@SpringBean
	private EndpointStatsRepo endpointStatsRepo;

	@Override
	protected void onInitialize() {
		super.onInitialize();
		add(new BookmarkablePageLink<RegisterUserPage>("automaticCheckLink", AutomaticCheckPage.class));
		add(new BookmarkablePageLink<RegisterUserPage>("manualCheckLink", ResourceSearchPage.class));
		add(new BookmarkablePageLink<CreateEndpointPage>("createEndpointLink", CreateEndpointPage.class));

		final Campaign campaign = campaignRepo.get(Campaign.class,
				WicketSession.get().getEvaluationSession().get().getCampaign().getId());

		EndpointScore endpointScore = new EndpointQualityFormulae(endpointStatsRepo).getScore(campaign.getEndpoint());
		EndpointScorePanel endpointScorePanel = new EndpointScorePanel("endpointScorePanel", endpointScore);

		endpointScorePanel.setVisible(!endpointScore.getEndpointStats().isEmpty());
		add(endpointScorePanel);
	}
}
