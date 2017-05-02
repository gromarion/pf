package com.itba.web.page;

import org.apache.wicket.markup.html.form.Form;
import com.itba.web.feedback.CustomFeedbackPanel;

@SuppressWarnings("serial")
public class ResourceSearchPage extends BasePage {
	@Override
	protected void onInitialize() {
		super.onInitialize();
		add(new CustomFeedbackPanel("feedbackPanel"));
		Form<Void> form = new Form<>("form");
		form.add(new ResourceSearchPanel("search"));
		add(form);
	}
}
