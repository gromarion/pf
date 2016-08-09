package com.itba.domain;

import com.google.common.base.Optional;
import com.itba.domain.model.EvaluatedResource;
import com.itba.domain.model.EvaluationSession;

public interface EvaluatedResourceRepo extends HibernateRepo {

    public Optional<EvaluatedResource> getResourceForSession(final EvaluationSession session, final String resource);
}

