package com.itba.domain.repository.hibernate;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.itba.domain.model.Error;
import com.itba.domain.repository.EvaluatedResourceDetailRepo;

@Repository
public class HibernateEvaluatedResourceDetailRepo extends AbstractHibernateRepo implements EvaluatedResourceDetailRepo {

    @Autowired
    public HibernateEvaluatedResourceDetailRepo(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
    
    @Override
    public List<Error> getPreviousErrors(String resource, String predicate, String object) {
    	
    	
    	
        List<Error> result = find(
        		"Select detail.error from EvaluatedResourceDetail detail "
        		+ " where detail.resource.resource = ? "
        		+ " and detail.predicate = ? "
        		+ " and detail.object = ? "
        		, resource, predicate, object);
        return result;
    }
}
