package com.itba.domain;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class HibernateEvaluatedResourceDetailRepo extends AbstractHibernateRepo implements EvaluatedResourceDetailRepo {

    @Autowired
    public HibernateEvaluatedResourceDetailRepo(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
}
