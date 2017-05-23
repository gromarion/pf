package com.itba.web.page;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListChoice;
import org.apache.wicket.markup.html.form.TextArea;
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

@SuppressWarnings("serial")
public class ErrorSelectionPage extends BasePage {

    @SpringBean
    private ErrorRepo errorRepo;
    
    @SpringBean
    private EvaluatedResourceRepo evaluatedResourceRepo;
    
    @SpringBean
    private EvaluatedResourceDetailRepo evaluatedResourceDetailRepo;

    private final IModel<Error> errorModel = new EntityModel<Error>(Error.class);
    private final IModel<EvaluationSession> currentSession = new EntityModel<EvaluationSession>(EvaluationSession.class);
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
        
        availableErrors.getObject().removeAll(evaluatedResourceDetailRepo.getPreviousErrors(resource, predicate, object));
        
        currentSession.setObject(WicketSession.get().getEvaluationSession().get());
            	
        final TextArea<String> comments = new TextArea<String>("comments", Model.of(""));
        final Label predicateLabel = new Label("predicateLabel", predicate);
        final Label objectLabel = new Label("objectLabel", object);
        final Label resourceLabel = new Label("resourceLabel", resource);
        final Label errorDescriptionLabel = new Label("errorDescription", errorDescription);
        final Label errorExampleLabel = new Label("errorExample", errorExample);
        errorDescriptionLabel.setOutputMarkupId(true);
        errorExampleLabel.setOutputMarkupId(true);
        
    	errorModel.setObject(availableErrors.getObject().get(0));
    	errorDescriptionLabel.setDefaultModelObject(errorModel.getObject().getDescription());
    	errorExampleLabel.setDefaultModelObject(errorModel.getObject().getExample());
        
        ListChoice<Error> errorListChoice = 
                new ListChoice<Error>("errorList", errorModel,
                        new LoadableDetachableModel<List<Error>>() {
                            @Override
                            protected List<Error> load() { 
                                return errorRepo.getAll();
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
				}
				
				EvaluatedResourceDetail detail = new EvaluatedResourceDetail(evaluatedResource.get(), errorModel.getObject(), predicate, object);
				evaluatedResourceDetailRepo.save(detail);
				
				PageParameters parameters = new PageParameters();
                String resourceURL = resource;
                parameters.add("selection", resourceURL);
                setResponsePage(ResultItemPage.class, parameters);
				// TODO: verificar errores y guardar
            }
        };
        
        form.add(comments);
        form.add(errorListChoice);
        form.add(errorDescriptionLabel);
        form.add(resourceLabel);
        form.add(objectLabel);
        form.add(predicateLabel);
        form.add(errorExampleLabel);
        form.add(new Button("submit"));
        
        add(form);
    }

    @Override
    protected void onDetach() {
        super.onDetach();
        errorModel.detach();
    }
}
