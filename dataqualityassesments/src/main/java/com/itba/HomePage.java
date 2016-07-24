package com.itba;

import com.itba.domain.SparqlSuggestOracle;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.ajax.json.JSONArray;
import org.apache.wicket.ajax.json.JSONObject;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("serial")
public class HomePage extends WebPage {

	private StringBuilder values = new StringBuilder();


	@Override
	protected void onInitialize() {
		super.onInitialize();
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

		AutoCompleteTextField<String> autoCompleteTextField = new AutoCompleteTextField<String>("autoCompleteTextField", suggestionModel) {
			@Override
			protected Iterator<String> getChoices(String s) {
				JSONArray choices = SparqlSuggestOracle.requestSuggestions(s);
				List<String> list = new ArrayList<String>();
				for (int i=0; i<choices.length(); i++) {
					list.add((String) ((JSONObject)(((JSONObject) choices.get(i)).get("s"))).get("value"));
				}
//				List<String> choices = Lists.newLinkedList();
				return list.iterator();
			}
		};

		form.add(autoCompleteTextField);
		add(form);

		final Label label = new Label("selection", new PropertyModel<String>(this, "values"));
		label.setOutputMarkupId(true);
		form.add(label);

		autoCompleteTextField.add(new AjaxFormSubmitBehavior(form, "change") {
			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				target.add(label);
			}

			@Override
			protected void onError(AjaxRequestTarget target) {
			}
		});
	}
}
