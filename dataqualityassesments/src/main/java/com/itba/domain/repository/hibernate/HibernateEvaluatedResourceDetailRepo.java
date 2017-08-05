package com.itba.domain.repository.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.itba.domain.model.Error;
import com.itba.domain.model.EvaluatedResource;
import com.itba.domain.model.EvaluatedResourceDetail;
import com.itba.domain.repository.EvaluatedResourceDetailRepo;

@Repository
public class HibernateEvaluatedResourceDetailRepo extends AbstractHibernateRepo implements EvaluatedResourceDetailRepo {

    @Autowired
    public HibernateEvaluatedResourceDetailRepo(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
    
    @Override
    public List<EvaluatedResourceDetail> getPreviousErrors(String resource, String predicate, String object) {
        List<EvaluatedResourceDetail> result = find(
        		"Select detail from EvaluatedResourceDetail detail "
        		+ " where detail.resource.resource = ? "
        		+ " and detail.predicate = ? "
        		+ " and detail.object = ? "
        		, resource, predicate, object);
        return result;
    }
    
    @SuppressWarnings("unchecked")
	@Override
    public List<String> getAlreadyEvaluatedForResource(final EvaluatedResource resource) {
        Query query = getSession().createQuery(
                "SELECT distinct concat(d.predicate, d.object) FROM EvaluatedResourceDetail d " +
                        " WHERE d.resource = " + resource.getId()
        );

        return query.list();
    }
    
    @Override
    public Long getQtyByError(final Error error) {
    	Query query = getSession().createQuery(
                "SELECT count(*) FROM EvaluatedResourceDetail d " +
                        " WHERE d.error = " + error.getId()
        );
    	
    	return (Long)query.uniqueResult();
    }
}
