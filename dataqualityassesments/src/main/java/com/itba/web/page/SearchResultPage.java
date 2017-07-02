package com.itba.web.page;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.json.JSONArray;
import org.apache.wicket.ajax.json.JSONException;
import org.apache.wicket.ajax.json.JSONObject;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import com.itba.domain.SparqlRequestHandler;
import com.itba.domain.model.Campaign;
import com.itba.domain.repository.CampaignRepo;
import com.itba.domain.repository.EndpointStatsRepo;
import com.itba.domain.repository.EvaluatedResourceRepo;
import com.itba.web.WicketSession;
import lib.ManualErrorsFormulae;
import lib.Score;

@SuppressWarnings("serial")
public class SearchResultPage extends BasePage {
	public static int PAGE_LIMIT = 20;
	
	@SpringBean
	private CampaignRepo campaignRepo;
	@SpringBean
	private EvaluatedResourceRepo evaluatedResourceRepo;
	@SpringBean
	private EndpointStatsRepo endpointStatsRepo;

	public SearchResultPage(PageParameters parameters) {
		
		final List<String> correctResources = evaluatedResourceRepo.getCorrectForSession(WicketSession.get().getEvaluationSession().get());
		
		final String search      = formatSearch(parameters.get("search").toString());
		final int offset         = fetchOffset(parameters);
		Campaign campaign        = WicketSession.get().getEvaluationSession().get().getCampaign();
		JSONArray choices;
		List<String> choicesList = new ArrayList<>();
		try {
			choices = SparqlRequestHandler.requestSuggestions(search, campaign, endpointStatsRepo, offset * PAGE_LIMIT, PAGE_LIMIT);
			parameters.set("offset", offset);
			
			for (int i = 0; i < choices.length(); i++) {
				choicesList.add((String) ((JSONObject)(((JSONObject) choices.get(i)).get("s"))).get("value"));
			}
		} catch (JSONException | IOException e) {
			setResponsePage(ErrorPage.class);
		}
		
		add(new ResourceSearchPanel("search"));
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
                        parameters.add("search", search);
                        setResponsePage(ResultItemPage.class, parameters);
                    }
                };
                String resource = stringModel.getObject();

                resultLink.add(new Label("linkText", resource));
				listItem.add(resultLink);
				Score resourceScore;
				try {
					resourceScore = new ManualErrorsFormulae(campaignRepo, evaluatedResourceRepo, endpointStatsRepo).compute(resource);
					listItem.add(new Label("resourceScore", resourceScore.scoreString()));
					listItem.add(new Label("resourceErrors", resourceScore.errorsString()));
				} catch (JSONException | IOException e) {
					setResponsePage(ErrorPage.class);
				}
				
				listItem.add(new Label("correctBadge", "!").setVisible(correctResources.contains(stringModel.getObject())));
            }
        });
		AjaxLink<Void> nextPageLink = new AjaxLink<Void>("nextPageLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                PageParameters parameters = new PageParameters();
                parameters.set("search", search);
                parameters.set("offset", offset + 1);
                setResponsePage(SearchResultPage.class, parameters);
            }
        };
        AjaxLink<Void> previousPageLink = new AjaxLink<Void>("previousPageLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                PageParameters parameters = new PageParameters();
                parameters.set("search", search);
                int newOffset = offset > 0 ? offset - 1 : 0;
                parameters.set("offset", newOffset);
                setResponsePage(SearchResultPage.class, parameters);
            }
        };
        add(new Label("currentPage", offset + 1));
        add(previousPageLink);
        add(nextPageLink);
		add(new Label("searchWord", search));
	}
	
	private int fetchOffset(PageParameters parameters) {
		String offsetParameter = parameters.get("offset").toString();

		return offsetParameter == null ? 0 : Integer.parseInt(offsetParameter);
	}
	
	private String formatSearch(String search) {
		return search.replaceAll(" ", "_");
	}
}
