package com.itba.web;

import com.google.common.base.Optional;
import com.itba.domain.EntityModel;
import com.itba.domain.model.EvaluationSession;
import org.apache.wicket.Session;
import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;

@SuppressWarnings("serial")
public class WicketSession extends WebSession {

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

    public void signIn(EvaluationSession evaluationSession) {
        this.evaluationSessionModel.setObject(evaluationSession);
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
}
