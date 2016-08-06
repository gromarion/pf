package com.itba.domain;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class HibernateEvaluatedResourceRepo extends AbstractHibernateRepo implements EvaluatedResourceRepo {

    @Autowired
    public HibernateEvaluatedResourceRepo(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
}
