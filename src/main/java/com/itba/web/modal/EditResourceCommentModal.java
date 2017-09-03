package com.itba.web.modal;

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

import com.itba.domain.model.EvaluatedResource;
import com.itba.domain.repository.EvaluatedResourceRepo;
import com.itba.web.WicketSession;
import com.itba.web.page.ResultItemPage;

@SuppressWarnings("serial")
public class EditResourceCommentModal extends Panel {

	@SpringBean
	private EvaluatedResourceRepo evaluatedResourceRepo;
	
	public EditResourceCommentModal(final String id, final String title, final String resource, final IModel<EvaluatedResource> resourceModel) {
		super(id);

		final Label titleLabel = new Label("titleLabel", title);
		String prevComments = resourceModel.getObject() == null ? "" : resourceModel.getObject().getComments();
        final TextArea<String> comments = new TextArea<String>("comments", Model.of(prevComments));
        final Button submit = new Button("submit");
        Form<EditResourceCommentModal> form = new Form<EditResourceCommentModal>("commentForm",
                new CompoundPropertyModel<EditResourceCommentModal>(this)) {
			@Override
            protected void onSubmit() {
				if (resourceModel.getObject() == null) {
					resourceModel.setObject(new EvaluatedResource(WicketSession.get().getEvaluationSession().get(), resource));
				}
				resourceModel.getObject().setComments(comments.getValue());
				evaluatedResourceRepo.save(resourceModel.getObject());
				PageParameters parameters = new PageParameters();
				parameters.add("selection", resource);
				setResponsePage(ResultItemPage.class, parameters);
            }
        };
        
        form.add(comments);
		form.add(submit);
		add(titleLabel);
		add(form);
	}
}