package com.itba.domain.repository.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.common.base.Optional;
import com.itba.domain.model.EvaluatedResource;
import com.itba.domain.model.EvaluationSession;
import com.itba.domain.repository.EvaluatedResourceRepo;

@Repository
public class HibernateEvaluatedResourceRepo extends AbstractHibernateRepo implements EvaluatedResourceRepo {

    @Autowired
    public HibernateEvaluatedResourceRepo(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @SuppressWarnings("unchecked")
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
    
    @SuppressWarnings("unchecked")
	@Override
    public List<EvaluatedResource> getAllForSession(final EvaluationSession session) {
        Query query = getSession().createQuery(
                "SELECT e FROM EvaluatedResource e " +
                        " WHERE e.session = " + session.getId()
        );

        return query.list();
    }
    
}
