package com.itba.web.page;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import com.google.common.base.Optional;
import com.itba.domain.CampaignRepo;
import com.itba.domain.EvaluatedResourceRepo;
import com.itba.domain.model.EvaluatedResource;
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
		final TextArea<String> comments = new TextArea<String>("comments", Model.of(""));
		comments.setOutputMarkupId(true);
		final TextField<String> textField = new TextField<String>("textField", Model.of(""));
		textField.setOutputMarkupId(true);

		Button submit = new Button("submit") {
			@Override
			public void onSubmit() {
				super.onSubmit();
				Optional<EvaluatedResource> resource = evaluatedResourceRepo.getResourceForSession(WicketSession.get().getEvaluationSession().get(), textField.getValue());
				if (resource.isPresent()) {
					resource.get().setComments(comments.getModelObject());
					evaluatedResourceRepo.save(resource.get());
				}
				PageParameters parameters = new PageParameters();
				parameters.add("search", textField.getValue());
				setResponsePage(SearchResultPage.class, parameters);
			}
		};
		form.add(comments);
		form.add(submit);
		form.add(textField);
		add(form);
	}
}
