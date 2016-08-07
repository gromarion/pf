package com.itba.domain;

import com.google.common.base.Optional;
import com.itba.domain.model.Campaign;
import com.itba.domain.model.EvaluationSession;
import com.itba.domain.model.User;
import org.joda.time.DateTime;

public interface EvaluationSessionRepo extends HibernateRepo {

    public Optional<EvaluationSession> getForCampaignAndUserWithinRange(final Campaign campaign, final User user,
                                                                        final DateTime fromDate, final DateTime toDate);
}
