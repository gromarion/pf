package com.itba.web.page;

import java.math.BigDecimal;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.itba.domain.EntityModel;
import com.itba.domain.model.EvaluatedResource;
import com.itba.domain.model.EvaluationSession;
import com.itba.domain.model.User;
import com.itba.domain.repository.CampaignRepo;
import com.itba.domain.repository.EndpointStatsRepo;
import com.itba.domain.repository.ErrorRepo;
import com.itba.domain.repository.EvaluatedResourceRepo;
import com.itba.domain.repository.UserRepo;
import com.itba.formulae.ManualErrorsFormulae;
import com.itba.web.WicketSession;
import com.itba.web.feedback.CustomFeedbackPanel;

import lib.StringUtils;

@SuppressWarnings("serial")
@AuthorizeInstantiation({ User.EVALUATOR_ROLE, User.ADMIN_ROLE })
public class RelatedEvaluationsPage extends BasePage {

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

	private final IModel<User> userModel = new EntityModel<User>(User.class);
	private final IModel<EvaluationSession> evaluationSessioneModel = new EntityModel<EvaluationSession>(EvaluationSession.class);
	private final IModel<EvaluatedResource> evaluatedResourceModel = new EntityModel<EvaluatedResource>(EvaluatedResource.class);

	// TODO: evitar mostrar el recurso múltiples veces en las columnas. Mostrarlo una vez a modo de título
	
	public RelatedEvaluationsPage(final PageParameters parameters) {
		add(new CustomFeedbackPanel("feedbackPanel"));
		userModel.setObject(userRepo.getByUsername(WicketSession.get().getUsername()));
		
		if (parameters.getNamedKeys().contains("resourceId")) {
			evaluatedResourceModel.setObject(evaluatedResourceRepo.get(parameters.get("resourceId").toInt()));
			evaluationSessioneModel.setObject(evaluatedResourceModel.getObject().getSession());
		}
		
		final String resource = evaluatedResourceModel.getObject() == null ?
				parameters.get("resource").toString() :
				evaluatedResourceModel.getObject().getResource();

		final IModel<List<EvaluatedResource>> evaluatedResources = new LoadableDetachableModel<List<EvaluatedResource>>() {
			@Override
			protected List<EvaluatedResource> load() {
				List<EvaluatedResource> result = evaluatedResourceRepo.getAllRelated(resource, evaluationSessioneModel);
				return result;
			}
		};

		WebMarkupContainer tableContainer = new WebMarkupContainer("tableContainer");
		WebMarkupContainer alertContainer = new WebMarkupContainer("alertContainer");
		alertContainer.add(new Label("noRelatedLabel", getString("noRelatedLabel")));
		
		tableContainer.add(new ListView<EvaluatedResource>("evaluatedResources", evaluatedResources) {
			@Override
			protected void populateItem(final ListItem<EvaluatedResource> evaluatedResource) {
				evaluatedResource
						.add(new Label("resourceTimestamp", evaluatedResource.getModelObject().getFormattedDate()));
				evaluatedResource.add(new Label("evaluatorUsername",
						evaluatedResource.getModelObject().getSession().getUser().getUsername()));
				if (evaluatedResource.getModelObject().isCorrect()) {
					evaluatedResource.add(new AttributeModifier("class", "table-success"));
				}
				BigDecimal score = evaluatedResource.getModelObject().getScore();
				evaluatedResource.add(new Label("resourceScore", StringUtils.formatDouble(score.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue(), 3)));
				AjaxLink<Void> resultLink = new AjaxLink<Void>("resultLink") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						PageParameters parameters = new PageParameters();
						// TODO: ojo con estos parámetros acá!
						parameters.add("comesFromMyResources", "true");
						parameters.add("selection", evaluatedResource.getModelObject().getResource());
						parameters.add("resourceSessionId", evaluatedResource.getModelObject().getSession().getId());
						setResponsePage(ResultItemPage.class, parameters);
					}
				};
				evaluatedResource.add(resultLink);
			}
		});
		
		add(new Label("resourceNameLabel", resource));
		add(new Label("titleLabel", getString("titleLabel")));
		add(tableContainer.setVisible(!evaluatedResources.getObject().isEmpty()));
		add(alertContainer.setVisible(evaluatedResources.getObject().isEmpty()));
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		userModel.detach();
		evaluatedResourceModel.detach();
		evaluationSessioneModel.detach();
	}
}