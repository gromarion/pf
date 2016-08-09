package com.itba.web.page;

import com.itba.domain.SparqlRequestHandler;
import org.apache.wicket.ajax.json.JSONArray;
import org.apache.wicket.ajax.json.JSONObject;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("serial")
public class ResourceSearchPage extends BasePage {

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
				JSONArray choices = SparqlRequestHandler.requestSuggestions(s);
				List<String> list = new ArrayList<String>();
				for (int i = 0; i < choices.length(); i++) {
					list.add((String) ((JSONObject)(((JSONObject) choices.get(i)).get("s"))).get("value"));
				}
				return list.iterator();
			}
		};

		Button submit = new Button("submit") {
			@Override
			public void onSubmit() {
				super.onSubmit();
				PageParameters parameters = new PageParameters();
				parameters.add("selection", suggestionModel.getObject());
				setResponsePage(ResultItemPage.class, parameters);
			}
		};

		form.add(submit);
		form.add(autoCompleteTextField);
		add(form);

	}
}
