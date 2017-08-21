package com.itba.web.page;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.json.JSONException;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.google.common.collect.Lists;
import com.itba.ManualErrorsFormulae;
import com.itba.domain.EntityModel;
import com.itba.domain.SparqlRequestHandler;
import com.itba.domain.model.Campaign;
import com.itba.domain.model.EvaluatedResource;
import com.itba.domain.model.EvaluatedResourceDetail;
import com.itba.domain.repository.CampaignRepo;
import com.itba.domain.repository.EndpointStatsRepo;
import com.itba.domain.repository.ErrorRepo;
import com.itba.domain.repository.EvaluatedResourceDetailRepo;
import com.itba.domain.repository.EvaluatedResourceRepo;
import com.itba.sparql.JsonSparqlResult;
import com.itba.sparql.ResultItem;
import com.itba.web.WicketSession;
import com.itba.web.feedback.CustomFeedbackPanel;

import lib.Score;
import lib.StringUtils;

@SuppressWarnings("serial")
public class ResultItemPage extends BasePage {

	private static final String URL_PATTERN = "(?i)\\b((?:https?://|www\\d{0,3}[.]|[a-z0-9.\\-]+[.][a-z]{2,4}/)(?:[^\\s()<>]+|\\(([^\\s()<>]+|(\\([^\\s()<>]+\\)))*\\))+(?:\\(([^\\s()<>]+|(\\([^\\s()<>]+\\)))*\\)|[^\\s`!()\\[\\]{};:\'\".,<>???“”‘’]))";

	@SpringBean
	private ManualErrorsFormulae manualErrorsFormulae;

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

	private final IModel<EvaluatedResource> resourceModel = new EntityModel<EvaluatedResource>(EvaluatedResource.class);

	public ResultItemPage(PageParameters parameters) {
		final String resource = parameters.get("selection").toString();
		final String search = parameters.get("search").toString();
		final Campaign campaign = campaignRepo.get(Campaign.class,
				WicketSession.get().getEvaluationSession().get().getCampaign().getId());
		final IModel<String> resourceOkLabelMessage = Model.of();
		final Label resourceOkLabel = new Label("resourceOkLabel", resourceOkLabelMessage);
		final Label errorNameHeaderLabel = new Label("errorNameHeaderLabel", getString("errorNameHeaderLabel"));
		final Label errorScoreHeaderLabel = new Label("errorScoreHeaderLabel", getString("errorScoreHeaderLabel"));


		resourceModel.setObject(evaluatedResourceRepo
				.getResourceForSession(WicketSession.get().getEvaluationSession().get(), resource).orNull());

		final LoadableDetachableModel<Map<String, Double>> scoreImpactModel = new LoadableDetachableModel<Map<String, Double>>() {
			@Override
			protected Map<String, Double> load() {
				Map<String, Double> scoreImpact = new HashMap<>();
				try {
					if (resourceModel.getObject() != null) {
						for (EvaluatedResourceDetail detail : resourceModel.getObject().getDetails()) {
							String key = detail.getError().getName();
							if (!scoreImpact.containsKey(key))
								scoreImpact.put(key, new Double(0));
							scoreImpact.put(key, scoreImpact.get(key)
									+ manualErrorsFormulae.computeIndividual(resource, detail.getError().getId()));
						}
					}
				} catch (Exception e) {
					// TODO: reset & log somehow...
					scoreImpact.clear();
				}
				return scoreImpact;
			}
		};

		ListView<String> detailsList = new ListView<String>("detailsList", new ArrayList<String>(scoreImpactModel.getObject().keySet())) {
			@Override
			protected void populateItem(ListItem<String> listItem) {
				Label errorNameLabel = new Label("errorNameLabel", listItem.getModelObject());
				Label errorScoreLabel = new Label("errorScoreLabel", "-" + StringUtils.formatDouble(scoreImpactModel.getObject().get(listItem.getModelObject()), 3));
				listItem.add(errorNameLabel);
				listItem.add(errorScoreLabel);
			}
		};
		
		add(detailsList.setVisible(!scoreImpactModel.getObject().isEmpty()));
		add(errorNameHeaderLabel.setVisible(!scoreImpactModel.getObject().isEmpty()));
		add(errorScoreHeaderLabel.setVisible(!scoreImpactModel.getObject().isEmpty()));

		final List<String> previouslyEvaluatedDetails = resourceModel.getObject() == null
				? Lists.<String>newLinkedList()
				: evaluatedResourceDetailRepo.getAlreadyEvaluatedForResource(resourceModel.getObject());

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
					Label objectLabel = new Label("object", transformURLs(resultItem.get(1).toString()));
					objectLabel.setEscapeModelStrings(false);
					listItem.add(objectLabel);
					listItem.add(new Label("errorsBadge", "!")
							.setVisible(previouslyEvaluatedDetails.contains(predicateURL + resultItem.get(1))));
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

		Score score;
		try {
			score = manualErrorsFormulae.compute(resource);
			// TODO: hacer el manejo del scoring adentro de este panel
			// directamente??
			ResourceScorePanel resourceScorePanel = new ResourceScorePanel("scorePanel", score);

			resourceScorePanel.add(new Label("resourceScore", score.toString()));
			if (score.getScore() < 0) {
				resourceScorePanel.setVisible(false);
			}
			add(resourceScorePanel);
		} catch (JSONException | IOException e) {
			setResponsePage(ErrorPage.class);
		}

		Link<Void> resourceOkButton = new Link<Void>("resourceOkButton") {
			@Override
			public void onClick() {
				if (resourceModel.getObject() == null) {
					resourceModel.setObject(
							new EvaluatedResource(WicketSession.get().getEvaluationSession().get(), resource));
					evaluatedResourceRepo.save(resourceModel.getObject());
				}

				resourceModel.getObject().setCorrect(!resourceModel.getObject().isCorrect());

				PageParameters parameters = new PageParameters();
				parameters.add("selection", resource);
				setResponsePage(ResultItemPage.class, parameters);
			}

			@Override
			protected void onComponentTag(final ComponentTag tag) {
				super.onComponentTag(tag);
				if (resourceModel.getObject() != null) {
					if (resourceModel.getObject().isCorrect()) {
						tag.put("class", "btn btn-danger");
						resourceOkLabelMessage.setObject("Quitar de recursos correctos");
					} else {
						tag.put("class", "btn btn-success");
						resourceOkLabelMessage.setObject("Agregar a recursos correctos");
					}
				} else {
					tag.put("class", "btn btn-success");
					resourceOkLabelMessage.setObject("Agregar a recursos correctos");
				}
			}
		};

		Link<Void> backButton = new Link<Void>("back") {
			@Override
			public void onClick() {
				PageParameters parameters = new PageParameters();
				String searchString = search == null ? "" : search;
				parameters.add("search", searchString);
				setResponsePage(SearchResultPage.class, parameters);
			}
		};

		resourceOkButton.add(resourceOkLabel);
		form.add(resourceOkButton.setVisible(resourceModel.getObject() == null
				|| (resourceModel.getObject() != null && !resourceModel.getObject().hasDetails())));
		form.add(backButton);
		form.add(comments);
		form.add(submit);
		add(new ResourceSearchPanel("search"));
		add(form);
		String resourceName = resource.substring(resource.lastIndexOf('/') + 1);
		add(new ExternalLink("resourceName", resource, resourceName.replace('_', ' ')));

		add(customFeedbackPanel);
	}

	private String transformURLs(String object) {
		Pattern patt = Pattern.compile(URL_PATTERN);
		Matcher matcher = patt.matcher(object);

		return matcher.replaceAll("<a href=\"$1\" target=\"_blank\">$1</a>");
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		resourceModel.detach();
	}
}