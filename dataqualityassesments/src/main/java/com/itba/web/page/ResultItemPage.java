package com.itba.web.page;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.itba.domain.CampaignRepo;
import com.itba.domain.SparqlRequestHandler;
import com.itba.domain.model.Campaign;
import com.itba.sparql.JsonSparqlResult;
import com.itba.sparql.ResultItem;
import com.itba.web.WicketSession;

@SuppressWarnings("serial")
public class ResultItemPage extends BasePage {
	@SpringBean
	CampaignRepo campaignRepo;

    public ResultItemPage(PageParameters parameters) {
        final String resource = parameters.get("selection").toString();
        final Campaign campaign = campaignRepo.get(Campaign.class, WicketSession.get().getEvaluationSession().get().getCampaign().getId());
        List<List<ResultItem>> results = new JsonSparqlResult(SparqlRequestHandler.requestResource(resource, campaign).toString()).data;

        add(new ResourceSearchPanel("search"));
        add(new ListView<List<ResultItem>>("resultItemList", results) {
            @Override
            protected void populateItem(ListItem<List<ResultItem>> listItem) {
                final List<ResultItem> resultItem = listItem.getModelObject();
                String predicateURL = resultItem.get(0).value;

        		listItem.add(new ExternalLink("predicate", predicateURL, predicateURL));
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
