package com.itba.domain.repository;

import com.google.common.base.Optional;
import com.itba.domain.model.EvaluatedResource;
import com.itba.domain.model.EvaluationSession;
import com.itba.domain.repository.hibernate.HibernateRepo;

public interface EvaluatedResourceRepo extends HibernateRepo {

    public Optional<EvaluatedResource> getResourceForSession(final EvaluationSession session, final String resource);
}

