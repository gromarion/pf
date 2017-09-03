package com.itba.web.modal;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.itba.domain.model.EvaluatedResourceDetail;
import com.itba.domain.repository.EvaluatedResourceDetailRepo;
import com.itba.web.page.ErrorSelectionPage;

@SuppressWarnings("serial")
public class EditErrorCommentModal extends Panel {
	
	@SpringBean
	private EvaluatedResourceDetailRepo evaluatedResourceDetailRepo;
	
	public EditErrorCommentModal(final String id, final String title, final IModel<EvaluatedResourceDetail> detailModel, final PageParameters refreshParameters) {
		super(id);
		final Label titleLabel = new Label("titleLabel", title);
        final TextArea<String> comments = new TextArea<String>("comments", Model.of(detailModel.getObject().getComment()));
        final Button submit = new Button("submit");
        Form<EditErrorCommentModal> form = new Form<EditErrorCommentModal>("commentForm",
                new CompoundPropertyModel<EditErrorCommentModal>(this)) {
			@Override
            protected void onSubmit() {
				detailModel.getObject().setComment(comments.getValue());
				evaluatedResourceDetailRepo.save(detailModel.getObject());
                setResponsePage(ErrorSelectionPage.class, refreshParameters);
            }
        };
        
        form.add(comments);
		form.add(submit);
        WebMarkupContainer container = new WebMarkupContainer("container");
        container.add(new AttributeModifier("id", "modal" + detailModel.getObject().getId()));
		add(container);
		container.add(titleLabel);
		container.add(form);
	}
}