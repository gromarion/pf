package com.itba.web.page;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListChoice;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.google.common.base.Optional;
import com.itba.domain.EntityModel;
import com.itba.domain.model.Error;
import com.itba.domain.model.EvaluatedResource;
import com.itba.domain.model.EvaluatedResourceDetail;
import com.itba.domain.model.EvaluationSession;
import com.itba.domain.repository.ErrorRepo;
import com.itba.domain.repository.EvaluatedResourceDetailRepo;
import com.itba.domain.repository.EvaluatedResourceRepo;
import com.itba.web.WicketSession;
import com.itba.web.feedback.CustomFeedbackPanel;
import com.itba.web.modal.EditErrorCommentModal;

import lib.StringUtils;
import utils.URLHelper;

@SuppressWarnings("serial")
public class ErrorSelectionPage extends BasePage {
	
	@SpringBean
	private ErrorRepo errorRepo;

	@SpringBean
	private EvaluatedResourceRepo evaluatedResourceRepo;

	@SpringBean
	private EvaluatedResourceDetailRepo evaluatedResourceDetailRepo;

	private final IModel<Error> errorModel = new EntityModel<Error>(Error.class);
	private final IModel<EvaluationSession> currentSession = new EntityModel<EvaluationSession>(
			EvaluationSession.class);
	private final IModel<String> errorDescription = Model.of();
	private final IModel<String> errorExample = Model.of();
	private final IModel<List<Error>> availableErrors = new LoadableDetachableModel<List<Error>>() {
		@Override
		protected List<Error> load() {
			return errorRepo.getAll();
		}
	};

	public ErrorSelectionPage(PageParameters parameters) {
    	if (!((WicketSession) getSession()).isSignedIn()) {
        	setResponsePage(LoginPage.class);
		}
        add(new CustomFeedbackPanel("feedbackPanel"));
        
        final String predicate = parameters.get("predicate").toString();
        final String object = parameters.get("object").toString();
        final String resource = parameters.get("resource").toString();
        
        final IModel<List<EvaluatedResourceDetail>> usedErrorDetails = new LoadableDetachableModel<List<EvaluatedResourceDetail>>() {
    	    @Override
    	    protected List<EvaluatedResourceDetail> load() { 
    	        return evaluatedResourceDetailRepo.getPreviousErrors(resource, predicate, object);
    	    }
    	};
        
    	for (int i = 0 ; i < usedErrorDetails.getObject().size() ; i++) {
    		availableErrors.getObject().remove(usedErrorDetails.getObject().get(i).getError());
    	}
    	
    	if(!StringUtils.containsURL(object)) {
    		availableErrors.getObject().remove(errorRepo.get(4));
    	}
    	
        currentSession.setObject(WicketSession.get().getEvaluationSession().get());
            	
        final TextArea<String> comments = new TextArea<String>("comments", Model.of(""));
        final ExternalLink predicateLink = new ExternalLink("predicateLink", predicate, predicate);
        final Label objectLabel = new Label("objectLabel", URLHelper.transformURLs(object));
        objectLabel.setEscapeModelStrings(false);
        final ExternalLink resourceLink = new ExternalLink("resourceLink", resource, resource);
        final Label errorDescriptionLabel = new Label("errorDescription", errorDescription);
        final Label errorExampleLabel = new Label("errorExample", errorExample);
        final Label foundErrorsLabel = new Label("foundErrorsLabel", getString("foundErrorsLabel"));
        final Label errorNameLabel = new Label("errorNameLabel", getString("errorNameLabel"));
        final Label errorCommentLabel = new Label("errorCommentLabel", getString("errorCommentLabel"));
        final Label errorActions = new Label("errorActions", getString("errorActions"));
        final Label newErrorLabel = new Label("newErrorLabel", getString("newErrorLabel"));
        
        errorDescriptionLabel.setOutputMarkupId(true);
        errorExampleLabel.setOutputMarkupId(true);
        
        if (availableErrors.getObject().size() > 0) {
        	errorModel.setObject(availableErrors.getObject().get(0));
        	errorDescriptionLabel.setDefaultModelObject(errorModel.getObject().getDescription());
        	errorExampleLabel.setDefaultModelObject(errorModel.getObject().getExample());
        }
    	
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
        
        ListChoice<Error> errorListChoice = 
                new ListChoice<Error>("errorList", errorModel,
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
            	errorDescriptionLabel.setDefaultModelObject(errorModel.getObject().getDescription());
            	errorExampleLabel.setDefaultModelObject(errorModel.getObject().getExample());
            	target.add(errorDescriptionLabel);
            	target.add(errorExampleLabel);
            }
        };
        errorListChoice.add(onChangeAjaxBehavior);
        		
        Form<ErrorSelectionPage> form = new Form<ErrorSelectionPage>("errorForm",
                new CompoundPropertyModel<ErrorSelectionPage>(this)) {
			@Override
            protected void onSubmit() {
				
		        Optional<EvaluatedResource> evaluatedResource = evaluatedResourceRepo.getResourceForSession(currentSession.getObject(), resource);
				if (!evaluatedResource.isPresent()) {
					evaluatedResource = Optional.of(new EvaluatedResource(currentSession.getObject(), resource));
					evaluatedResourceRepo.save(evaluatedResource.get());
				} else {
					EvaluatedResource r = evaluatedResource.get();
					r.setTimestamp(System.currentTimeMillis());
					evaluatedResourceRepo.save(r);
				}
				
				evaluatedResource.get().setCorrect(false);
				
				EvaluatedResourceDetail detail = new EvaluatedResourceDetail(evaluatedResource.get(), errorModel.getObject(), predicate, object);
				detail.setComment(comments.getValue());
				evaluatedResourceDetailRepo.save(detail);
				
				PageParameters parameters = new PageParameters();
                parameters.add("predicate", predicate);
                parameters.add("object", object);
                parameters.add("resource", resource);
                setResponsePage(ErrorSelectionPage.class, parameters);
            }
        };
        
        form.add(comments);
        form.add(errorListChoice);
        form.add(errorDescriptionLabel);
        form.add(resourceLink);
        form.add(objectLabel);
        form.add(predicateLink);
        form.add(errorExampleLabel);
        
        Button submit = new Button("submit");
        if (availableErrors.getObject().size() == 0) submit.setVisible(false);
        form.add(submit);
        
        add(new Link<Void>("back") {
			@Override
			public void onClick() {
				PageParameters parameters = new PageParameters();
				String resourceURL = resource;
                parameters.add("selection", resourceURL);
                setResponsePage(ResultItemPage.class, parameters);
			}
		});
        
        add(form.setVisible(availableErrors.getObject().size() > 0));
        add(newErrorLabel.setVisible(availableErrors.getObject().size() > 0));
        add(foundErrorsLabel.setVisible(usedErrorDetails.getObject().size() > 0));
        add(errorNameLabel.setVisible(usedErrorDetails.getObject().size() > 0));
        add(errorCommentLabel.setVisible(usedErrorDetails.getObject().size() > 0));
        add(errorActions.setVisible(usedErrorDetails.getObject().size() > 0));
    }

	@Override
	protected void onDetach() {
		super.onDetach();
		errorModel.detach();
	}
}
