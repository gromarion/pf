package com.itba.web.page;

import com.itba.domain.SparqlRequestHandler;
import com.itba.sparql.JsonSparqlResult;
import com.itba.sparql.ResultItem;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.util.List;

@SuppressWarnings("serial")
public class ResultItemPage extends BasePage {

    public ResultItemPage(PageParameters parameters) {
        final String resource = parameters.get("selection").toString();
        List<List<ResultItem>> results = new JsonSparqlResult(SparqlRequestHandler.requestResource(resource).toString()).data;
        add(new ListView<List<ResultItem>>("resultItemList", results) {
            @Override
            protected void populateItem(ListItem<List<ResultItem>> listItem) {
                final List<ResultItem> resultItem = listItem.getModelObject();
                listItem.add(new Label("predicate", resultItem.get(0)));
                listItem.add(new Label("object", resultItem.get(1)));
                listItem.add(new AjaxLink<Void>("errorPageLink") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        PageParameters parameters = new PageParameters();
                        parameters.add("resultItem", resultItem);
                        parameters.add("resource", resource);
                        setResponsePage(ErrorSelectionPage.class, parameters);
                    }
                });
            }
        });




    }
}
