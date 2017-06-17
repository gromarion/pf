package com.itba.web.page;

import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.itba.domain.model.Campaign;
import com.itba.domain.repository.CampaignRepo;
import com.itba.domain.repository.EndpointStatsRepo;
import com.itba.web.WicketSession;

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

		add(new EndpointScorePanel("endpointScorePanel", endpointStatsRepo, campaign.getEndpoint()));
	}
}
