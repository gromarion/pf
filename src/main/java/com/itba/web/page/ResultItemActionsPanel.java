package com.itba.web.page;

import java.math.BigDecimal;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.itba.domain.EntityModel;
import com.itba.domain.model.EvaluatedResource;
import com.itba.domain.model.EvaluationSession;
import com.itba.domain.repository.EvaluatedResourceRepo;
import com.itba.domain.repository.EvaluationSessionRepo;
import com.itba.formulae.ManualErrorsFormulae;
import com.itba.web.WicketSession;
import com.itba.web.tooltip.Tooltip;
import com.itba.web.tooltip.Tooltip.Position;

import lib.Score;

@SuppressWarnings("serial")
public class ResultItemActionsPanel extends Panel {

	@SpringBean
	private ManualErrorsFormulae manualErrorsFormulae;
	@SpringBean
	private EvaluatedResourceRepo evaluatedResourceRepo;
	@SpringBean
	private EvaluationSessionRepo sessionRepo;

	private final IModel<EvaluatedResource> resourceModel = new EntityModel<EvaluatedResource>(EvaluatedResource.class);

	public ResultItemActionsPanel(String id, PageParameters parameters) {
		super(id);
		final String comesFromMyResources = parameters.get("comesFromMyResources").toString();
		final String resource = parameters.get("selection").toString();
		final IModel<String> resourceOkLabelMessage = Model.of();
		final Label resourceOkLabel = new Label("resourceOkLabel", resourceOkLabelMessage);

		Optional<EvaluationSession> resourceSession = Optional.absent();
		if (!parameters.get("resourceSessionId").isNull()) {
			resourceSession = Optional.of(sessionRepo.get(parameters.get("resourceSessionId").toInt()));
		}
		resourceModel.setObject(evaluatedResourceRepo.getResourceForSession(
				resourceSession.isPresent() ? resourceSession.get() : WicketSession.get().getEvaluationSession().get(),
				resource).orNull());

		Link<Void> resourceOkButton = new Link<Void>("resourceOkButton") {
			@Override
			public void onClick() {
				if (resourceModel.getObject() == null) {
					resourceModel.setObject(
							new EvaluatedResource(WicketSession.get().getEvaluationSession().get(), resource));
					evaluatedResourceRepo.save(resourceModel.getObject());
				}
				resourceModel.getObject().setCorrect(!resourceModel.getObject().isCorrect());
				if(resourceModel.getObject().isCorrect()) 
					resourceModel.getObject().setScore(new BigDecimal(1));
				
				try {
					Score score = manualErrorsFormulae.compute(resource, Optional.of(resourceModel.getObject().getSession()));
					resourceModel.getObject().setScore(new BigDecimal(score.getScore()));
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				PageParameters parameters = new PageParameters();
				parameters.add("selection", resource);
				parameters.add("comesFromMyResources", Strings.nullToEmpty(comesFromMyResources));
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
		
		Link<Void> relatedEvaluationsButton = new Link<Void>("relatedEvaluationsButton") {
			@Override
			public void onClick() {
				PageParameters parameters = new PageParameters();
				if (resourceModel.getObject() != null) {
					parameters.add("resourceId", resourceModel.getObject().getId());
				} else {
					parameters.add("resource", resource);
				}
				setResponsePage(RelatedEvaluationsPage.class, parameters);
			}
		};
		
		WebMarkupContainer editCommentsButtonContainer = new WebMarkupContainer("editCommentsButtonContainer");
		editCommentsButtonContainer.setVisible(resourceModel.getObject() == null ||
				resourceModel.getObject().getSession().getUser().equals(WicketSession.get().getUser()));
		
		resourceOkButton.add(resourceOkLabel);
		add(resourceOkButton.setVisible(resourceModel.getObject() == null
				|| (resourceModel.getObject() != null && !resourceModel.getObject().hasDetails() && 
						resourceModel.getObject().getSession().getUser().equals(WicketSession.get().getUser()))
			)
		);
		add(relatedEvaluationsButton);
		add(editCommentsButtonContainer);
	}
	
	@Override
	protected void onDetach() {
		super.onDetach();
		resourceModel.detach();
	}
}
