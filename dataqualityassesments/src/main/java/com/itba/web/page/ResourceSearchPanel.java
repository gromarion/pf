package com.itba.web.page;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

@SuppressWarnings("serial")
public class ResourceSearchPanel extends Panel {

	public ResourceSearchPanel(String id) {
		super(id);
		Form<Void> searchForm = new Form<>("search-form");
		final TextField<String> searchTextField = new TextField<String>("textField", Model.of(""));
		searchTextField.setOutputMarkupId(true);
		
		Button submit = new Button("submit") {
			@Override
			public void onSubmit() {
				super.onSubmit();
				PageParameters parameters = new PageParameters();
				parameters.add("search", searchTextField.getValue());
				setResponsePage(SearchResultPage.class, parameters);
			}
		};

		searchForm.add(searchTextField);
		searchForm.add(submit);
		add(searchForm);
	}
}
