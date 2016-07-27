package com.itba.web.page;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.itba.domain.SparqlRequestHandler;
import com.itba.sparql.JsonSparqlResult;
import com.itba.sparql.ResultItem;

@SuppressWarnings("serial")
public class ResultItemPage extends BasePage {

    public ResultItemPage(PageParameters parameters) {
        final String selection = parameters.get("selection").toString();
        List<List<ResultItem>> results = new JsonSparqlResult(SparqlRequestHandler.requestResource(selection).toString()).data;
        add(new ListView<List<ResultItem>>("resultItemList", results) {
            @Override
            protected void populateItem(ListItem<List<ResultItem>> listItem) {
                List<ResultItem> resultItem = listItem.getModelObject();
                listItem.add(new Label("predicate", resultItem.get(0)));
                listItem.add(new Label("object", resultItem.get(1)));
//                listItem.add(new Link<T>() {
//                	@Override
//                	public void onClick() {
//                		// TODO Auto-generated method stub
//                		
//                	}
//				});
                // TODO: acá agregar un botón que dispare el modal de los errores
            }
        });
    }
}
