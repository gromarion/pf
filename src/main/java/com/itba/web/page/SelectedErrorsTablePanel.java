package com.itba.web.page;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.itba.domain.model.EvaluatedResource;
import com.itba.domain.model.EvaluatedResourceDetail;
import com.itba.domain.repository.ErrorRepo;
import com.itba.domain.repository.EvaluatedResourceDetailRepo;
import com.itba.formulae.ManualErrorsFormulae;
import com.itba.web.WicketSession;
import com.itba.web.modal.EditErrorCommentModal;

import lib.Score;

@SuppressWarnings("serial")
public class SelectedErrorsTablePanel extends Panel {

	@SpringBean
	private EvaluatedResourceDetailRepo evaluatedResourceDetailRepo;
	@SpringBean
	private ErrorRepo errorRepo;
	@SpringBean
	private ManualErrorsFormulae manualErrorsFormulae;

	private final IModel<List<EvaluatedResourceDetail>> usedErrorDetails;

	public SelectedErrorsTablePanel(String id, final String predicate, final String object,
			final IModel<EvaluatedResource> resource, boolean isAuthor) {
		super(id);

		this.usedErrorDetails = new LoadableDetachableModel<List<EvaluatedResourceDetail>>() {
			@Override
			protected List<EvaluatedResourceDetail> load() {
				return resource.getObject() == null ? Lists.newArrayList()
						: evaluatedResourceDetailRepo.getPreviousErrors(resource.getObject(), predicate, object);
			}
		};
		
		WebMarkupContainer alertContainer = new WebMarkupContainer("alertContainer");
		alertContainer.add(new Label("readOnlyWarning", getString("readOnlyWarning")));
		alertContainer.setVisible(!isAuthor);
		add(alertContainer);

		final Label errorActionsLabel = new Label("errorActionsLabel", getString("errorActionsLabel"));

		add(new ListView<EvaluatedResourceDetail>("usedErrorDetails", usedErrorDetails) {
			@Override
			protected void populateItem(final ListItem<EvaluatedResourceDetail> errorDetail) {

				boolean isAuthorUser = WicketSession.get().getUser()
						.equals(resource.getObject().getSession().getUser());

				final PageParameters refreshParameters = new PageParameters();
				refreshParameters.add("predicate", predicate);
				refreshParameters.add("object", object);
				refreshParameters.add("resource", resource.getObject().getResource());
				refreshParameters.add("sessionId", resource.getObject().getSession().getId());
				errorDetail.add(new Label("errorName", errorDetail.getModelObject().getError().getName()));
				errorDetail.add(new Label("errorComment", errorDetail.getModelObject().getComment()));
				errorDetail.add(new AjaxLink<Void>("removeErrorLink") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						try {
							EvaluatedResource resource = errorDetail.getModelObject().getResource();
							resource.getDetails().remove(errorDetail.getModelObject());
							evaluatedResourceDetailRepo.delete(errorDetail.getModelObject());
							Score score = manualErrorsFormulae.compute(resource.getResource(),
									Optional.of(resource.getSession()));
							resource.setScore(new BigDecimal(score.getScore()).setScale(3, RoundingMode.HALF_EVEN));
							setResponsePage(ErrorSelectionPage.class, refreshParameters);
						} catch (Exception e) {
							// TODO: do nothing for now...
						}
					}
				}.setVisible(isAuthorUser));
				errorDetail.add(new AjaxLink<Void>("editCommentLink") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						// TODO: do nothing for now...
					}
				}.setVisible(isAuthorUser)
						.add(new AttributeModifier("data-target", "#modal" + errorDetail.getModelObject().getId())));
				errorDetail.add(new EditErrorCommentModal("editCommentModal", "Editar comentario",
						errorDetail.getModel(), refreshParameters));
				errorActionsLabel.setVisible(isAuthorUser);
			}
		}.setVisible(usedErrorDetails.getObject().size() > 0));

		add(errorActionsLabel);
	}

	public IModel<List<EvaluatedResourceDetail>> getUsedErrorDetails() {
		return usedErrorDetails;
	}

}