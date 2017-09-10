package com.itba.web.page;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;

import com.itba.web.feedback.CustomFeedbackPanel;

@SuppressWarnings("serial")
@AuthorizeInstantiation({"EVALUATOR", "ADMIN"})
public class ResourceSearchPage extends BasePage {
	@Override
	protected void onInitialize() {
		super.onInitialize();
		add(new CustomFeedbackPanel("feedbackPanel"));
		add(new ResourceSearchPanel("search"));
	}
}
