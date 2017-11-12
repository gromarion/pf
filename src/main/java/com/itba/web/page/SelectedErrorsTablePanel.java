package com.itba.web.page;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.itba.domain.model.EvaluatedResourceDetail;
import com.itba.domain.repository.ErrorRepo;
import com.itba.domain.repository.EvaluatedResourceDetailRepo;
import com.itba.web.modal.EditErrorCommentModal;

@SuppressWarnings("serial")
public class SelectedErrorsTablePanel extends Panel {
	
	@SpringBean
	private EvaluatedResourceDetailRepo evaluatedResourceDetailRepo;
	@SpringBean
	private ErrorRepo errorRepo;
	
	private final IModel<List<EvaluatedResourceDetail>> usedErrorDetails;

	public SelectedErrorsTablePanel(String id, final String predicate, final String object, final String resource) {
		super(id);
		
		this.usedErrorDetails = new LoadableDetachableModel<List<EvaluatedResourceDetail>>() {
    	    @Override
    	    protected List<EvaluatedResourceDetail> load() { 
    	        return evaluatedResourceDetailRepo.getPreviousErrors(resource, predicate, object);
    	    }
    	};
    	
    	add(new ListView<EvaluatedResourceDetail>("usedErrorDetails", usedErrorDetails) {
			@Override
			protected void populateItem(final ListItem<EvaluatedResourceDetail> errorDetail) {
				final PageParameters refreshParameters = new PageParameters();
				refreshParameters.add("predicate", predicate);
				refreshParameters.add("object", object);
				refreshParameters.add("resource", resource);
				errorDetail.add(new Label("errorName", errorDetail.getModelObject().getError().getName()));
				errorDetail.add(new Label("errorComment", errorDetail.getModelObject().getComment()));
				errorDetail.add(new AjaxLink<Void>("removeErrorLink") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						evaluatedResourceDetailRepo.delete(errorDetail.getModelObject());
						setResponsePage(ErrorSelectionPage.class, refreshParameters);
					}
				});
				errorDetail.add(new AjaxLink<Void>("editCommentLink") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						// TODO: do nothing for now...
					}
				}.add(new AttributeModifier("data-target", "#modal" + errorDetail.getModelObject().getId())));
				errorDetail.add(new EditErrorCommentModal("editCommentModal", "Editar comentario", errorDetail.getModel(), refreshParameters));
			}
		}.setVisible(usedErrorDetails.getObject().size() > 0));
	}
	
	public IModel<List<EvaluatedResourceDetail>> getUsedErrorDetails() {
		return usedErrorDetails;
	}
}
