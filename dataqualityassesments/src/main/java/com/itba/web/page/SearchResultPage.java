package com.itba.web.page;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.json.JSONArray;
import org.apache.wicket.ajax.json.JSONObject;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.itba.domain.SparqlRequestHandler;
import com.itba.web.WicketSession;

@SuppressWarnings("serial")
public class SearchResultPage extends BasePage {

	public SearchResultPage(PageParameters parameters) {
		String search = parameters.get("search").toString();
		JSONArray choices = SparqlRequestHandler.requestSuggestions(search, WicketSession.get().getEvaluationSession().get().getCampaign());
		List<String> choicesList = new ArrayList<>();

		for (int i = 0; i < choices.length(); i++) {
			choicesList.add((String) ((JSONObject)(((JSONObject) choices.get(i)).get("s"))).get("value"));
		}
		
		add(new ListView<String>("searchResultList", choicesList) {
			@Override
            protected void populateItem(ListItem<String> listItem) {
				final IModel<String> stringModel = listItem.getModel();
				AjaxLink<Void> resultLink = new AjaxLink<Void>("resultLink") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        PageParameters parameters = new PageParameters();
                        String resourceURL = stringModel.getObject();
                        parameters.add("selection", resourceURL);
                        setResponsePage(ResultItemPage.class, parameters);
                    }
                };
                resultLink.add(new Label("linkText", stringModel.getObject()));
				listItem.add(resultLink);
            }
        });
		add(new Label("searchWord", search));
	}
}
