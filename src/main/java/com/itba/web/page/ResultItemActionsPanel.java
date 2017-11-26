package com.itba.web.page;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.google.common.base.Strings;
import com.itba.domain.EntityModel;
import com.itba.domain.model.EvaluatedResource;
import com.itba.domain.repository.EvaluatedResourceRepo;
import com.itba.web.WicketSession;
import com.itba.web.tooltip.Tooltip;
import com.itba.web.tooltip.Tooltip.Position;

@SuppressWarnings("serial")
public class ResultItemActionsPanel extends Panel {
	
	@SpringBean
	private EvaluatedResourceRepo evaluatedResourceRepo;
	
	private final IModel<EvaluatedResource> resourceModel = new EntityModel<EvaluatedResource>(EvaluatedResource.class);

	public ResultItemActionsPanel(String id, PageParameters parameters) {
		super(id);
		final String comesFromMyResources = parameters.get("comesFromMyResources").toString();
		final String resource = parameters.get("selection").toString();
		final IModel<String> resourceOkLabelMessage = Model.of();
		final Label resourceOkLabel = new Label("resourceOkLabel", resourceOkLabelMessage);
		
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

		Tooltip.addToComponent(resourceOkButton, Position.TOP, "Un tooltip sobre este botón");
		resourceOkButton.add(resourceOkLabel);
		add(resourceOkButton.setVisible(resourceModel.getObject() == null
				|| (resourceModel.getObject() != null && !resourceModel.getObject().hasDetails())));
	}
}
