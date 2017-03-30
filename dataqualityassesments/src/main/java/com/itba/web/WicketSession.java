package com.itba.web;

import org.apache.wicket.Session;
import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;

import com.google.common.base.Optional;
import com.itba.domain.EntityModel;
import com.itba.domain.EvaluationSessionRepo;
import com.itba.domain.UserRepo;
import com.itba.domain.model.Campaign;
import com.itba.domain.model.EvaluationSession;
import com.itba.domain.model.User;

@SuppressWarnings("serial")
public class WicketSession extends WebSession {

	private String username;
    private IModel<EvaluationSession> evaluationSessionModel = new EntityModel<EvaluationSession>(EvaluationSession.class);

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
		User user = users.getByName(username);

		if (user != null && user.checkPassword(password)) {
			this.username = username;
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
    }
    
    public String getUsername() {
		return username;
	}
}
