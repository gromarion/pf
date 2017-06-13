package com.itba.web.page;

import java.io.IOException;
import java.util.List;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.json.JSONException;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import com.itba.domain.SparqlRequestHandler;
import com.itba.domain.model.Campaign;
import com.itba.domain.repository.CampaignRepo;
import com.itba.domain.repository.EndpointStatsRepo;
import com.itba.domain.repository.ErrorRepo;
import com.itba.domain.repository.EvaluatedResourceDetailRepo;
import com.itba.domain.repository.EvaluatedResourceRepo;
import com.itba.sparql.JsonSparqlResult;
import com.itba.sparql.ResultItem;
import com.itba.web.WicketSession;
import com.itba.web.feedback.CustomFeedbackPanel;
import lib.ManualErrorsFormulae;

@SuppressWarnings("serial")
public class ResultItemPage extends BasePage {
	@SpringBean
	private CampaignRepo campaignRepo;
	@SpringBean
	private ErrorRepo errorRepo;
	@SpringBean
	private EvaluatedResourceRepo evaluatedResourceRepo;
	@SpringBean
	private EvaluatedResourceDetailRepo evaluatedResourceDetailRepo;
	@SpringBean
	private EndpointStatsRepo endpointStatsRepo;

	public ResultItemPage(PageParameters parameters) {
		final String resource = parameters.get("selection").toString();
		final String search = parameters.get("search").toString();
		final Campaign campaign = campaignRepo.get(Campaign.class,
				WicketSession.get().getEvaluationSession().get().getCampaign().getId());
		List<List<ResultItem>> results;
		try {
			results = new JsonSparqlResult(
					SparqlRequestHandler.requestResource(resource, campaign, endpointStatsRepo).toString()).data;
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
							parameters.add("predicate", resultItem.get(0));
							parameters.add("object", resultItem.get(1));
							parameters.add("resource", resource);
							setResponsePage(ErrorSelectionPage.class, parameters);
						}
					});
				}
			});
		} catch (JSONException | IOException e) {
			setResponsePage(ErrorPage.class);
		}
		final CustomFeedbackPanel customFeedbackPanel = new CustomFeedbackPanel("feedbackPanel");
		customFeedbackPanel.setOutputMarkupId(true);
		Form<Void> form = new Form<>("form");
		final TextArea<String> comments = new TextArea<String>("comments", Model.of(""));
		comments.setOutputMarkupId(true);
		Button submit = new Button("submit") {
			@Override
			public void onSubmit() {
				super.onSubmit();
			}
		};

		ManualErrorsFormulae.Score score;
		try {
			score = new ManualErrorsFormulae(campaignRepo, evaluatedResourceRepo, endpointStatsRepo).compute(resource);
			ResourceScorePanel resourceScorePanel = new ResourceScorePanel("scorePanel", score);
			
			add(new Label("resourceScore", score.scoreString()));
			if (score.getScore() == -1) {
				resourceScorePanel.setVisible(false);
			}
			add(resourceScorePanel);
		} catch (JSONException | IOException e) {
			setResponsePage(ErrorPage.class);
		}
		

		Link<Void> backButton = new Link<Void>("back") {
			@Override
			public void onClick() {
				PageParameters parameters = new PageParameters();
				String searchString = search == null ? "" : search;
				parameters.add("search", searchString);
				setResponsePage(SearchResultPage.class, parameters);
			}
		};

		form.add(backButton);
		form.add(comments);
		form.add(submit);
		add(new ResourceSearchPanel("search"));
		add(form);
		String resourceName = resource.substring(resource.lastIndexOf('/') + 1);
		add(new Label("resourceName", resourceName.replace('_', ' ')));

		add(customFeedbackPanel);

	}
}
