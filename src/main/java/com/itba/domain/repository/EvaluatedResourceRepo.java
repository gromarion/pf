package com.itba.domain.repository;

import java.util.List;

import com.google.common.base.Optional;
import com.itba.domain.model.EvaluatedResource;
import com.itba.domain.model.EvaluationSession;
import com.itba.domain.repository.hibernate.HibernateRepo;
import com.itba.domain.repository.hibernate.PaginatedResult;

public interface EvaluatedResourceRepo extends HibernateRepo {

    Optional<EvaluatedResource> getResourceForSession(final EvaluationSession session, final String resource);

    PaginatedResult<EvaluatedResource> getAllForSession(EvaluationSession session, int page);
    
    List<EvaluatedResource> getAllForSession(EvaluationSession session);
    
    List<String> getErroredForSession(EvaluationSession session);

	List<String> getCorrectForSession(EvaluationSession session);
}
