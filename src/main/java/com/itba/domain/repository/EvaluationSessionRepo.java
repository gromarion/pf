package com.itba.domain.repository;

import com.google.common.base.Optional;
import com.itba.domain.model.Campaign;
import com.itba.domain.model.EvaluationSession;
import com.itba.domain.model.User;
import com.itba.domain.repository.hibernate.HibernateRepo;

public interface EvaluationSessionRepo extends HibernateRepo {
	
	public EvaluationSession get(int sessionId);

    public Optional<EvaluationSession> getForCampaignAndUser(final Campaign campaign, final User user);
    
}
