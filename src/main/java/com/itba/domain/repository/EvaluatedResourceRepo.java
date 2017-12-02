package com.itba.domain.repository;

import java.math.BigDecimal;
import java.util.List;

import com.google.common.base.Optional;
import com.itba.domain.model.Campaign;
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
	
	List<EvaluatedResource> getAll();
	
	PaginatedResult<EvaluatedResource> getAllPaginated(int page);
	
	List<String> getErrored();

	List<String> getCorrect();

	List<EvaluatedResource> getAllByCampaign(Campaign c);

	List<String> getErroredByCampaign(Campaign c);

	List<String> getCorrectByCampaign(Campaign c);

	BigDecimal getSumScoreByCampaign(Campaign c);
}

