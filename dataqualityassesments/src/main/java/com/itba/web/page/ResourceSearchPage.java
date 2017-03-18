package com.itba.web.page;

import com.google.common.base.Optional;
import com.itba.domain.EvaluatedResourceRepo;
import com.itba.domain.SparqlRequestHandler;
import com.itba.domain.model.EvaluatedResource;
import com.itba.domain.model.EvaluationSession;
import com.itba.sparql.Endpoint;
import com.itba.web.WicketSession;
import com.itba.web.feedback.CustomFeedbackPanel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.json.JSONArray;
import org.apache.wicket.ajax.json.JSONObject;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("serial")
public class ResourceSearchPage extends BasePage {
	@SpringBean
	EvaluatedResourceRepo evaluatedResourceRepo;

	private StringBuilder values = new StringBuilder();
	
	@Override
	protected void onInitialize() {
		super.onInitialize();
		add(new CustomFeedbackPanel("feedbackPanel"));
		Form<Void> form = new Form<>("form");
		final IModel<String> suggestionModel = new IModel<String>() {
			private String value = null;

			@Override
			public String getObject() {
				return value;
			}

			@Override
			public void setObject(String object) {
				value = object;
				values.append("\n");
				values.append(value);
			}

			@Override
			public void detach() {
			}
		};
		List<Endpoint> endpointChoices = new ArrayList<>();
		Endpoint endpoint = new Endpoint(
            "http://live.dbpedia.org/sparql",
            "http://dbpedia.org",
            "live.dbpedia.org/sparql"
        );
		endpointChoices.add(endpoint);
		final IModel<Endpoint> endpointChoiceModel = Model.of(endpointChoices.get(0));
		
		ChoiceRenderer<Endpoint> choiceRenderer = new ChoiceRenderer<>("endpoint");
		final TextArea<String> comments = new TextArea<String>("comments", Model.of(""));
		comments.setOutputMarkupId(true);
		final DropDownChoice<Endpoint> endpointDropdownChoices = new DropDownChoice<>("endpointChoices", endpointChoiceModel, endpointChoices, choiceRenderer);
		final AutoCompleteTextField<String> autoCompleteTextField = new AutoCompleteTextField<String>("autoCompleteTextField", suggestionModel) {
			@Override
			protected Iterator<String> getChoices(String s) {
				JSONArray choices = SparqlRequestHandler.requestSuggestions(s, endpointChoiceModel.getObject());
				List<String> list = new ArrayList<String>();
				for (int i = 0; i < choices.length(); i++) {
					list.add((String) ((JSONObject)(((JSONObject) choices.get(i)).get("s"))).get("value"));
				}
				return list.iterator();
			}
		};

		autoCompleteTextField.add(new AjaxFormComponentUpdatingBehavior("onselect") {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				String search = autoCompleteTextField.getValue();
				Optional<EvaluationSession> session = WicketSession.get().getEvaluationSession();
				Optional<EvaluatedResource> resource = evaluatedResourceRepo.getResourceForSession(WicketSession.get().getEvaluationSession().get(), search);
				if (resource.isPresent()) {
					comments.setModelObject(resource.get().getComments());
				} else {
					evaluatedResourceRepo.save(new EvaluatedResource(session.get(), search));
				}
				target.add(comments);
			}
		});
		autoCompleteTextField.setOutputMarkupId(true);


		Button submit = new Button("submit") {
			@Override
			public void onSubmit() {
				super.onSubmit();
				Optional<EvaluatedResource> resource = evaluatedResourceRepo.getResourceForSession(WicketSession.get().getEvaluationSession().get(), autoCompleteTextField.getValue());
				if (resource.isPresent()) {
					resource.get().setComments(comments.getModelObject());
					evaluatedResourceRepo.save(resource.get());
				}
				PageParameters parameters = new PageParameters();
				parameters.add("selection", suggestionModel.getObject());
				setResponsePage(ResultItemPage.class, parameters);
			}
		};

		form.add(comments);
		form.add(submit);
		form.add(endpointDropdownChoices);
		form.add(autoCompleteTextField);
		add(form);
	}
}
