package com.itba.domain.repository.hibernate;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.itba.domain.repository.EvaluatedResourceDetailRepo;

@Repository
public class HibernateEvaluatedResourceDetailRepo extends AbstractHibernateRepo implements EvaluatedResourceDetailRepo {

    @Autowired
    public HibernateEvaluatedResourceDetailRepo(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
}
