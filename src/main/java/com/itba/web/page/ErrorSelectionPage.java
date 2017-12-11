package com.itba.web.page;

import java.math.BigDecimal;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.Link;
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
import com.itba.domain.repository.EvaluationSessionRepo;
import com.itba.formulae.ManualErrorsFormulae;
import com.itba.web.WicketSession;
import com.itba.web.feedback.CustomFeedbackPanel;

import lib.Score;
import lib.StringUtils;
import utils.URLHelper;

@SuppressWarnings("serial")
public class ErrorSelectionPage extends BasePage {
	
	@SpringBean
	private ManualErrorsFormulae manualErrorsFormulae;
	@SpringBean
	private ErrorRepo errorRepo;
	@SpringBean
	private EvaluationSessionRepo evaluationSessionRepo;
	@SpringBean
	private EvaluatedResourceRepo evaluatedResourceRepo;
	@SpringBean
	private EvaluatedResourceDetailRepo evaluatedResourceDetailRepo;

	private final IModel<Error> errorModel = new EntityModel<Error>(Error.class);
	private final IModel<EvaluationSession> currentSession = new EntityModel<EvaluationSession>(EvaluationSession.class);
	private final IModel<EvaluatedResource> evaluatedResource = new EntityModel<EvaluatedResource>(EvaluatedResource.class);
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
    	
        currentSession.setObject(WicketSession.get().getEvaluationSession().get());

    	final String predicate = parameters.get("predicate").toString();
    	final String object = parameters.get("object").toString();
    	final String resource = parameters.get("resource").toString();
    	if (parameters.getNamedKeys().contains("sessionId")) {
    		evaluatedResource.setObject(evaluatedResourceRepo.getResourceForSession(evaluationSessionRepo.get(parameters.get("sessionId").toInt()), resource).orNull());
    	}
    	
    	SelectedErrorsTablePanel selectedErrorsTablePanel = new SelectedErrorsTablePanel("selectedErrorsTablePanel", predicate, object, evaluatedResource);
    	
    	add(selectedErrorsTablePanel);
    	IModel<List<EvaluatedResourceDetail>> usedErrorDetails = selectedErrorsTablePanel.getUsedErrorDetails();
    	selectedErrorsTablePanel.setVisible(usedErrorDetails.getObject().size() > 0);
    	
    	for (int i = 0 ; i < usedErrorDetails.getObject().size() ; i++) {
    		availableErrors.getObject().remove(usedErrorDetails.getObject().get(i).getError());
    	}
    	
        add(new CustomFeedbackPanel("feedbackPanel"));
    	
    	if(!StringUtils.containsURL(object)) {
    		availableErrors.getObject().remove(errorRepo.get(4));
    	}
    	
            	
        final TextArea<String> comments = new TextArea<String>("comments", Model.of(""));
        final ExternalLink predicateLink = new ExternalLink("predicateLink", predicate, predicate);
        final Label objectLabel = new Label("objectLabel", URLHelper.transformURLs(object, false));
        objectLabel.setEscapeModelStrings(false);
        final ExternalLink resourceLink = new ExternalLink("resourceLink", resource, resource);
        final Label errorDescriptionLabel = new Label("errorDescription", errorDescription);
        final Label errorExampleLabel = new Label("errorExample", errorExample);
        
        errorDescriptionLabel.setOutputMarkupId(true);
        errorExampleLabel.setOutputMarkupId(true);
        
        if (availableErrors.getObject().size() > 0) {
        	errorModel.setObject(availableErrors.getObject().get(0));
        	errorDescriptionLabel.setDefaultModelObject(errorModel.getObject().getDescription());
        	errorExampleLabel.setDefaultModelObject(errorModel.getObject().getExample());
        }
    	
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
				evaluatedResource.get().getDetails().add(detail);
				
				try {
					Score score = manualErrorsFormulae.compute(resource, Optional.of(evaluatedResource.get().getSession()));
					evaluatedResource.get().setScore(new BigDecimal(score.getScore()).setScale(3, BigDecimal.ROUND_HALF_EVEN));
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				PageParameters parameters = new PageParameters();
                parameters.add("predicate", predicate);
                parameters.add("object", object);
                parameters.add("resource", resource);
                parameters.add("sessionId", evaluatedResource.get().getSession().getId());
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
        
        Link<Void> backButton = new Link<Void>("back") {
			@Override
			public void onClick() {
				PageParameters parameters = new PageParameters();
				String resourceURL = resource;
                parameters.add("selection", resourceURL);
                setResponsePage(ResultItemPage.class, parameters);
			}
		};
		form.add(backButton);
        
		boolean showForm = availableErrors.getObject().size() > 0 &&
				(evaluatedResource.getObject() == null || (evaluatedResource.getObject().getSession().getUser().equals(WicketSession.get().getUser())));
		
        add(form.setVisible(showForm));
    }

	@Override
	protected void onDetach() {
		super.onDetach();
		errorModel.detach();
		currentSession.detach();
		evaluatedResource.detach();
	}
}
