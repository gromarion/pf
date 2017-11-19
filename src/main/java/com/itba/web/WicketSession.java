package com.itba.web;

import org.apache.wicket.Session;
import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;

import com.google.common.base.Optional;
import com.itba.domain.EntityModel;
import com.itba.domain.model.Campaign;
import com.itba.domain.model.EvaluationSession;
import com.itba.domain.model.User;
import com.itba.domain.repository.EvaluationSessionRepo;
import com.itba.domain.repository.UserRepo;

@SuppressWarnings("serial")
public class WicketSession extends WebSession {

    private IModel<EvaluationSession> evaluationSessionModel = new EntityModel<EvaluationSession>(EvaluationSession.class);
    private IModel<User> user;

    public static WicketSession get() {
        return (WicketSession) Session.get();
    }

    public WicketSession(Request request) {
        super(request);
    }

    public Optional<EvaluationSession> getEvaluationSession() {
        return Optional.fromNullable(evaluationSessionModel.getObject());
    }

    public boolean signIn(String username, String password, Campaign campaign, UserRepo users, EvaluationSessionRepo evaluationSessions) {
		User user = users.getByUsername(username);

		if (user != null && user.checkPassword(password) && !user.isDeleted()) {
			this.user = new EntityModel<User>(User.class, user);
			Optional<EvaluationSession> session = evaluationSessions.getForCampaignAndUser(campaign, user);
	    	
	        if (!session.isPresent()) {
	            EvaluationSession newSession = new EvaluationSession(campaign, user);
	            evaluationSessions.save(newSession);
	            session = Optional.of(newSession);
	        }
	        this.evaluationSessionModel.setObject(session.get());
	        return true;
		}
		return false;
    }

    public boolean isSignedIn() {
        return evaluationSessionModel.getObject() != null;
    }

    public void signOut() {
        invalidate();
        clear();
    }

    @Override
    public void detach() {
        super.detach();
        if (evaluationSessionModel != null) evaluationSessionModel.detach();
        if (user != null) user.detach();
    }
    
    public User getUser() {
    	return user.getObject();
    }
    
    public String getUsername() {
		return user.getObject().getUsername();
	}
    
    public String getFullName() {
		return user.getObject().getFullName();
	}
}
