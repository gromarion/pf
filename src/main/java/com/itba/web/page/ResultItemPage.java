package com.itba.web.page;

import java.io.IOException;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.json.JSONException;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.itba.domain.EntityModel;
import com.itba.domain.SparqlRequestHandler;
import com.itba.domain.model.Campaign;
import com.itba.domain.model.EvaluatedResource;
import com.itba.domain.model.EvaluationSession;
import com.itba.domain.repository.CampaignRepo;
import com.itba.domain.repository.EndpointStatsRepo;
import com.itba.domain.repository.ErrorRepo;
import com.itba.domain.repository.EvaluatedResourceDetailRepo;
import com.itba.domain.repository.EvaluatedResourceRepo;
import com.itba.domain.repository.EvaluationSessionRepo;
import com.itba.domain.repository.UserRepo;
import com.itba.formulae.ManualErrorsFormulae;
import com.itba.sparql.JsonSparqlResult;
import com.itba.sparql.ResultItem;
import com.itba.web.WicketSession;
import com.itba.web.feedback.CustomFeedbackPanel;
import com.itba.web.modal.EditResourceCommentModal;

import lib.Score;
import lib.StringUtils;
import utils.URLHelper;

@SuppressWarnings("serial")
public class ResultItemPage extends BasePage {

	@SpringBean
	private ManualErrorsFormulae manualErrorsFormulae;
	@SpringBean
	private EvaluationSessionRepo sessionRepo;
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
	@SpringBean
	private UserRepo userRepo;

	private final IModel<EvaluatedResource> resourceModel = new EntityModel<EvaluatedResource>(EvaluatedResource.class);

	public ResultItemPage(PageParameters parameters) {
		// TODO: adaptar esto para que puedan pasarlo o no...
		final String comesFromMyResources = parameters.get("comesFromMyResources").toString();
		final String resource = parameters.get("selection").toString();

		boolean guest = userRepo.getByUsername(getAppSession().getUsername()).hasRole("GUEST");
		final WebMarkupContainer commentsContainer = new WebMarkupContainer("commentsContainer");

		Optional<EvaluationSession> resourceSession = Optional.absent();
		if (!parameters.get("resourceSessionId").isNull()) {
			resourceSession = Optional.of(sessionRepo.get(parameters.get("resourceSessionId").toInt()));
		}

		resourceModel.setObject(evaluatedResourceRepo.getResourceForSession(
				resourceSession.isPresent() ? resourceSession.get() : WicketSession.get().getEvaluationSession().get(),
				resource).orNull());

		final Campaign campaign = resourceModel.getObject() == null
				? WicketSession.get().getEvaluationSession().get().getCampaign()
				: resourceModel.getObject().getSession().getCampaign();

		boolean hasComments = resourceModel.getObject() != null
				&& !Strings.isNullOrEmpty(resourceModel.getObject().getComments());
		final Label commentsLabel = new Label("comments", hasComments ? resourceModel.getObject().getComments() : "");

		commentsContainer.add(commentsLabel);
		add(commentsContainer.setVisible(hasComments));
		add(new EditResourceCommentModal("editCommentModal", "Editar comentario", resource, comesFromMyResources,
				resourceModel));

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
					boolean hasEvaluations = previouslyEvaluatedDetails.contains(predicateURL + resultItem.get(1));
					if (hasEvaluations) {
						listItem.add(new AttributeModifier("class", "table-danger"));
					}
					listItem.add(new ExternalLink("predicate", StringUtils.shortenText(predicateURL), predicateURL));
					String objectValue = URLHelper.transformURLs(resultItem.get(1).toString(), true);
					Label objectLabel = new Label("object", objectValue);
					objectLabel.setEscapeModelStrings(false);
					listItem.add(objectLabel);
					AjaxLink<Void> errorPageLink = new AjaxLink<Void>("errorPageLink") {
						@Override
						public void onClick(AjaxRequestTarget target) {
							PageParameters parameters = new PageParameters();
							parameters.add("predicate", resultItem.get(0));
							parameters.add("object", resultItem.get(1));
							parameters.add("resource", resource);
							if (resourceModel.getObject() != null) parameters.add("sessionId", resourceModel.getObject().getSession().getId());
							setResponsePage(ErrorSelectionPage.class, parameters);
						}
					};
					boolean isAuthorUser = false;
					if (resourceModel.getObject() != null) {
						isAuthorUser = WicketSession.get().getUser().equals(resourceModel.getObject().getSession().getUser());
					}
					errorPageLink.setVisible(isAuthorUser || (!isAuthorUser && hasEvaluations));
					String errorPageLinkClass = isAuthorUser ? "fa fa-pencil" : "fa fa-eye";
					errorPageLink.add(new AttributeModifier("class", errorPageLinkClass));
					listItem.add(errorPageLink);
				}
			});
		} catch (JSONException | IOException e) {
			e.printStackTrace(System.out);
			setResponsePage(ErrorPage.class);
		}
		final CustomFeedbackPanel customFeedbackPanel = new CustomFeedbackPanel("feedbackPanel");
		customFeedbackPanel.setOutputMarkupId(true);

		Score score;
		try {
			score = manualErrorsFormulae.compute(resource, resourceSession);
			ResourceScorePanel resourceScorePanel = new ResourceScorePanel("scorePanel", score);
			resourceScorePanel.add(new Label("resourceScore", score.toString()));
			if (score.getScore() < 0 || score.getErrors() == 0) {
				resourceScorePanel.setVisible(false);
			}
			add(resourceScorePanel);
		} catch (JSONException | IOException e) {
			e.printStackTrace(System.out);
			setResponsePage(ErrorPage.class);
		}

		add(new ResourceSearchPanel("search"));
		String resourceName = resource.substring(resource.lastIndexOf('/') + 1);
		add(new ExternalLink("resourceName", resource, resourceName.replace('_', ' ')));

		add(customFeedbackPanel);
		ResultItemActionsPanel actionsPanel = new ResultItemActionsPanel("actionsPanel", parameters);
		actionsPanel.setVisible(!guest);
		add(actionsPanel);
		int sessionId = parameters.get("resourceSessionId").isNull() ? -1 : parameters.get("resourceSessionId").toInt(); 
		ErrorsTablePanel errorsTablePanel = new ErrorsTablePanel("errorsTablePanel", resource, sessionId);
		add(errorsTablePanel);
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		resourceModel.detach();
	}
}