package com.itba.domain;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.itba.domain.model.Campaign;
import com.itba.domain.model.EvaluationSession;
import com.itba.domain.model.User;

@Repository
public class HibernateEvaluationSessionRepo extends AbstractHibernateRepo implements EvaluationSessionRepo {

//    private static final DateTimeFormatter FMT = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Autowired
    public HibernateEvaluationSessionRepo(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Optional<EvaluationSession> getForCampaignAndUser(final Campaign campaign, final User user) {
        Query query = getSession().createQuery(
                "SELECT e FROM EvaluationSession e " +
                        " WHERE e.user = " + user.getId()
                        + " AND e.campaign = " + campaign.getId()
//                        + " AND e.timestamp BETWEEN \'" + fromDate.toString(FMT)
//                        + "\' AND \'" + toDate.toString(FMT) + "\'"
                        
        		);

        List<EvaluationSession> result = query.list();
        if(result.isEmpty()) return Optional.absent();
        Preconditions.checkState(result.size() == 1, "Inconsistent state: duplicated session");
        return Optional.of(result.get(0));
    }
}
