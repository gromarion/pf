package com.itba.domain;

import com.google.common.base.Optional;
import com.itba.domain.model.EvaluatedResource;
import com.itba.domain.model.EvaluationSession;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class HibernateEvaluatedResourceRepo extends AbstractHibernateRepo implements EvaluatedResourceRepo {

    @Autowired
    public HibernateEvaluatedResourceRepo(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Optional<EvaluatedResource> getResourceForSession(final EvaluationSession session, final String resource) {
        Query query = getSession().createQuery(
                "SELECT e FROM EvaluatedResource e " +
                        " WHERE e.session = " + session.getId()
                        + " AND e.resource = '" + resource + "'"
        );

        List<EvaluatedResource> result = query.list();
        if(result.isEmpty()) return Optional.absent();
        return Optional.of(result.get(0));
    }
}
