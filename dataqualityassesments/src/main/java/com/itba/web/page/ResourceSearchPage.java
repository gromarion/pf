package com.itba.web.page;

import org.apache.wicket.ajax.json.JSONObject;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.itba.domain.SparqlRequestHandler;
import com.itba.domain.model.Campaign;
import com.itba.domain.repository.CampaignRepo;
import com.itba.domain.repository.EvaluatedResourceRepo;
import com.itba.web.WicketSession;
import com.itba.web.feedback.CustomFeedbackPanel;

@SuppressWarnings("serial")
public class ResourceSearchPage extends BasePage {
	@SpringBean
	EvaluatedResourceRepo evaluatedResourceRepo;
	
	@SpringBean
	CampaignRepo campaignRepo;
	
	@Override
	protected void onInitialize() {
		super.onInitialize();
		add(new CustomFeedbackPanel("feedbackPanel"));
		Form<Void> form = new Form<>("form");
		form.add(new ResourceSearchPanel("search"));
		add(form);
		
		add(new Link<Void>("fetchRandom") {
			@Override
			public void onClick() {
				final Campaign campaign = campaignRepo.get(Campaign.class, WicketSession.get().getEvaluationSession().get().getCampaign().getId());
				PageParameters parameters = new PageParameters();
				JSONObject ans = SparqlRequestHandler.requestRandomResource(campaign);
				String resourceURL = (String) ((JSONObject) ans.get("s")).get("value");
				parameters.add("selection", resourceURL);

				setResponsePage(ResultItemPage.class, parameters);
			}
		});
	}
}
