package com.itba.web.page;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.util.Strings;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.json.JSONException;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.itba.domain.EntityModel;
import com.itba.domain.model.Error;
import com.itba.domain.model.EvaluatedResource;
import com.itba.domain.model.EvaluatedResourceDetail;
import com.itba.domain.model.EvaluationSession;
import com.itba.domain.model.User;
import com.itba.domain.repository.CampaignRepo;
import com.itba.domain.repository.EndpointStatsRepo;
import com.itba.domain.repository.ErrorRepo;
import com.itba.domain.repository.EvaluatedResourceRepo;
import com.itba.domain.repository.UserRepo;
import com.itba.domain.repository.hibernate.PaginatedResult;
import com.itba.formulae.ManualErrorsFormulae;
import com.itba.web.WicketSession;
import com.itba.web.feedback.CustomFeedbackPanel;

@SuppressWarnings("serial")
@AuthorizeInstantiation({ "EVALUATOR", "ADMIN" })
public class ErrorsByUserPage extends BasePage {

	@SpringBean
	private ManualErrorsFormulae manualErrorsFormulae;
	@SpringBean
	private EvaluatedResourceRepo evaluatedResourceRepo;
	@SpringBean
	private CampaignRepo campaignRepo;
	@SpringBean
	private EndpointStatsRepo endpointStatsRepo;
	@SpringBean
	private ErrorRepo errorRepo;
	@SpringBean
	private UserRepo userRepo;

	private final IModel<Error> errorModel = new EntityModel<Error>(Error.class);
	private final IModel<User> userModel = new EntityModel<User>(User.class);
	private final IModel<EvaluationSession> currentSession = new EntityModel<EvaluationSession>(
			EvaluationSession.class);
	private final IModel<List<Error>> availableErrors = new LoadableDetachableModel<List<Error>>() {
		@Override
		protected List<Error> load() {
			return errorRepo.getAll();
		}
	};
	private boolean hasNextPage;

	public ErrorsByUserPage(final PageParameters parameters) {
		add(new CustomFeedbackPanel("feedbackPanel"));

		// TODO: que esta página sea parametrizable por usuario y campaña
		// final String userName = parameters.get("userName").toString();
		// final String campaignName =
		// parameters.get("campaignName").toString();
		// userModel.setObject(userRepo.getByUsername(userName));

		currentSession.setObject(WicketSession.get().getEvaluationSession().get());
		final int page = fetchPage(parameters);

		final IModel<List<EvaluatedResource>> evaluatedResources = new LoadableDetachableModel<List<EvaluatedResource>>() {
			@Override
			protected List<EvaluatedResource> load() {
				User username = userRepo.getByUsername(WicketSession.get().getUsername());
				if (username.hasRole("ADMIN")) {
					List<EvaluatedResource> result = evaluatedResourceRepo.getAll();
					return result;
				} else {
					PaginatedResult<EvaluatedResource> result = getResult(parameters);
					hasNextPage = result.hasNextPage();
					return result.getResult();
				}
			}
		};
		
		DropDownChoice<Error> errorListChoice = 
	        new DropDownChoice<Error>("errorList", errorModel,
	                new LoadableDetachableModel<List<Error>>() {
	                    @Override
	                    protected List<Error> load() { 
	                        return availableErrors.getObject();
	                    }
	                }
	            , new ChoiceRenderer<Error>("name")) {
        };
        
        OnChangeAjaxBehavior onChangeAjaxBehavior = new OnChangeAjaxBehavior() {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
            	int errorId = errorModel.getObject().getId();
            	PageParameters parameters = new PageParameters();
				parameters.add("errorId", errorId);
				setResponsePage(ErrorsByUserPage.class, parameters);
            }
        };
        errorListChoice.add(onChangeAjaxBehavior);
        add(errorListChoice);

		add(new ListView<EvaluatedResource>("evaluatedResources", evaluatedResources) {
			@Override
			protected void populateItem(final ListItem<EvaluatedResource> evaluatedResource) {
				evaluatedResource
						.add(new Label("resourceTimestamp", evaluatedResource.getModelObject().getFormattedDate()));
				if (evaluatedResource.getModelObject().isCorrect()) {
					evaluatedResource.add(new AttributeModifier("class", "success"));
				}
				try {
					evaluatedResource.add(new Label("resourceScore", manualErrorsFormulae
							.compute(evaluatedResource.getModelObject().getResource()).scoreString()));
				} catch (JSONException | IOException e) {
					e.printStackTrace();
					setResponsePage(ErrorPage.class);
				}
				AjaxLink<Void> resultLink = new AjaxLink<Void>("resultLink") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						PageParameters parameters = new PageParameters();
						parameters.add("comesFromMyResources", "true");
						parameters.add("selection", evaluatedResource.getModelObject().getResource());
						setResponsePage(ResultItemPage.class, parameters);
					}
				};
				resultLink.add(new Label("resourceName", evaluatedResource.getModelObject().getResource()));
				evaluatedResource.add(resultLink);
			}
		});

		AjaxLink<Void> nextPageLink = new AjaxLink<Void>("nextPageLink") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				PageParameters parameters = new PageParameters();
				parameters.set("page", page + 1);
				setResponsePage(SearchResultPage.class, parameters);
			}
		};
		AjaxLink<Void> previousPageLink = new AjaxLink<Void>("previousPageLink") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				PageParameters parameters = new PageParameters();
				int newOffset = page > 0 ? page - 1 : 0;
				parameters.set("page", newOffset);
				setResponsePage(SearchResultPage.class, parameters);
			}
		};
		add(new Label("currentPage", page + 1));
		previousPageLink.setVisible(page > 0);
		add(previousPageLink);
		nextPageLink.setVisible(hasNextPage);
		add(nextPageLink);
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		userModel.detach();
	}

	private int fetchPage(PageParameters parameters) {
		String page = parameters.get("page").toString();

		return page == null ? 0 : Integer.parseInt(page);
	}
	
	private PaginatedResult<EvaluatedResource> getResult(PageParameters parameters) {
		PaginatedResult<EvaluatedResource> result = evaluatedResourceRepo.getAllForSession(currentSession.getObject(), fetchPage(parameters));
		if (Strings.isNotEmpty(parameters.get("errorId").toString())) {
			int errorId = Integer.parseInt(parameters.get("errorId").toString());
			Set<EvaluatedResource> filteredEvaluatedResource = new HashSet<>();
		
			for (EvaluatedResource evaluatedResource : result.getResult()) {
				for (EvaluatedResourceDetail evaluatedResourceDetail : evaluatedResource.getDetails()) {
					if (evaluatedResourceDetail.getError().getId() == errorId) {
						filteredEvaluatedResource.add(evaluatedResource);
					}
				}
			}
			result.setResult(new ArrayList<EvaluatedResource>(filteredEvaluatedResource));
		}
		
		return result;
	}
}
