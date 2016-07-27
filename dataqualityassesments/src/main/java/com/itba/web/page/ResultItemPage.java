package com.itba.web.page;

import com.itba.domain.SparqlRequestHandler;
import com.itba.sparql.JsonSparqlResult;
import com.itba.sparql.ResultItem;
import com.itba.web.panel.ErrorPanel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.util.List;

@SuppressWarnings("serial")
public class ResultItemPage extends BasePage {

    public ResultItemPage(PageParameters parameters) {
        final String selection = parameters.get("selection").toString();
        final ModalWindow errorModalWindow = new ModalWindow("errorModal");
        errorModalWindow.setContent(new ErrorPanel(errorModalWindow.getContentId()));
        errorModalWindow.setTitle("This is modal window with panel content.");
        errorModalWindow.setCookieName("modal-2");
        add(errorModalWindow);
        List<List<ResultItem>> results = new JsonSparqlResult(SparqlRequestHandler.requestResource(selection).toString()).data;
        add(new ListView<List<ResultItem>>("resultItemList", results) {
            @Override
            protected void populateItem(ListItem<List<ResultItem>> listItem) {
                List<ResultItem> resultItem = listItem.getModelObject();
                listItem.add(new Label("predicate", resultItem.get(0)));
                listItem.add(new Label("object", resultItem.get(1)));
                listItem.add(new AjaxLink<Void>("showModalLink") {
                    @Override
                    public void onClick(AjaxRequestTarget target)
                    {
                        errorModalWindow.show(target);
                    }
                });
            }
        });




    }
}
