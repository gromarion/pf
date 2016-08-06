package com.itba.domain;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class HibernateEvaluationSessionRepo extends AbstractHibernateRepo implements EvaluationSessionRepo {

    @Autowired
    public HibernateEvaluationSessionRepo(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
}
