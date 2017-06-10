package com.itba.web.page;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
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
import com.itba.domain.repository.EvaluatedResourceRepo;
import com.itba.domain.repository.UserRepo;
import com.itba.web.WicketSession;
import com.itba.web.feedback.CustomFeedbackPanel;

@SuppressWarnings("serial")
public class ErrorsByUserPage extends BasePage {

    @SpringBean
    private EvaluatedResourceRepo evaluatedResourceRepo;
    
    @SpringBean
    private UserRepo userRepo;
	
    final IModel<User> userModel = new EntityModel<User>(User.class);
    private final IModel<EvaluationSession> currentSession = new EntityModel<EvaluationSession>(EvaluationSession.class);
    
	public ErrorsByUserPage(PageParameters parameters) {
    	if (!((WicketSession) getSession()).isSignedIn()) {
        	setResponsePage(LoginPage.class);
		}
    	
        add(new CustomFeedbackPanel("feedbackPanel"));
        
        // TODO: que esta página sea parametrizable por usuario y campaña
//        final String userName = parameters.get("userName").toString();
//        final String campaignName = parameters.get("campaignName").toString();
//        userModel.setObject(userRepo.getByUsername(userName));
        
        currentSession.setObject(WicketSession.get().getEvaluationSession().get());
        
        final IModel<List<EvaluatedResource>> evaluatedResources = new LoadableDetachableModel<List<EvaluatedResource>>() {
    	    @Override
    	    protected List<EvaluatedResource> load() { 
    	        return evaluatedResourceRepo.getAllForSession(currentSession.getObject());
    	    }
    	};
    	
    	add(new ListView<EvaluatedResource>("evaluatedResources", evaluatedResources) {
			@Override
			protected void populateItem(final ListItem<EvaluatedResource> evaluatedResource) {
				evaluatedResource.add(new Label("resourceName", evaluatedResource.getModelObject().getResource()));
				evaluatedResource.add(new Label("resourceTimestamp", evaluatedResource.getModelObject().getFormattedDate()));
				evaluatedResource.add(new AjaxLink<Void>("resultLink") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        PageParameters parameters = new PageParameters();
                        parameters.add("selection", evaluatedResource.getModelObject().getResource());
                        setResponsePage(ResultItemPage.class, parameters);
                    }
                });
			}
		});
	}
	
	@Override
    protected void onDetach() {
        super.onDetach();
        userModel.detach();
    }
}
