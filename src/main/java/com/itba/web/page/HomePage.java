package com.itba.web.page;

import com.itba.web.feedback.CustomFeedbackPanel;

@SuppressWarnings("serial")
public class HomePage extends BasePage {
	
	@Override
	protected void onInitialize() {
		super.onInitialize();
		add(new CustomFeedbackPanel("feedbackPanel"));
		add(new ResourceSearchPanel("search"));
	}
}
