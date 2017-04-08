package com.itba.web.page;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.spring.injection.annot.SpringBean;
import com.itba.domain.CampaignRepo;
import com.itba.domain.EvaluatedResourceRepo;
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
	}
}
