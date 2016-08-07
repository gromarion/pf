package com.itba.domain;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.itba.domain.model.Campaign;
import com.itba.domain.model.EvaluationSession;
import com.itba.domain.model.User;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class HibernateEvaluationSessionRepo extends AbstractHibernateRepo implements EvaluationSessionRepo {

    private static final DateTimeFormatter FMT = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Autowired
    public HibernateEvaluationSessionRepo(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Optional<EvaluationSession> getForCampaignAndUserWithinRange(final Campaign campaign, final User user,
                                                                        final DateTime fromDate, final DateTime toDate) {
        Query query = getSession().createQuery(
                "SELECT e FROM EvaluationSession e " +
                        " WHERE e.user = " + user.getId() +
                        " AND e.campaign = " + campaign.getId() +
                        " AND e.timestamp BETWEEN \'" + fromDate.toString(FMT)
                        + "\' AND \'" + toDate.toString(FMT) + "\'");

        List<EvaluationSession> result = query.list();
        if(result.isEmpty()) return Optional.absent();
        Preconditions.checkState(result.size() == 1, "Inconsistent state: duplicated session");
        return Optional.of(result.get(0));
    }
}
