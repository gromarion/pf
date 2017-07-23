package com.itba.web.page;

import java.io.IOException;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.itba.EndpointQualityFormulae;
import com.itba.EndpointQualityFormulae.EndpointScore;

public class AutomaticOrManualPage extends BasePage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private EndpointQualityFormulae endpointQualityFormulae;

	@Override
	protected void onInitialize() {
		super.onInitialize();
		add(new BookmarkablePageLink<RegisterUserPage>("automaticCheckLink", AutomaticCheckPage.class));
		add(new BookmarkablePageLink<RegisterUserPage>("manualCheckLink", ResourceSearchPage.class));
		add(new BookmarkablePageLink<CreateEndpointPage>("createEndpointLink", CreateEndpointPage.class));

		EndpointScore endpointScore;
		try {
			endpointScore = endpointQualityFormulae.getScore();
			EndpointScorePanel endpointScorePanel = new EndpointScorePanel("endpointScorePanel", endpointScore);
			
			endpointScorePanel.setVisible(!endpointScore.getEndpointStats().isEmpty());
			add(endpointScorePanel);
		} catch (IOException e) {
			setResponsePage(ErrorPage.class);
			e.printStackTrace();
		}
	}
}
