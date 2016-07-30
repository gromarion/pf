package com.itba.domain;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.itba.domain.model.Error;

import java.util.List;

@Repository
public class HibernateErrorRepo extends AbstractHibernateRepo implements ErrorRepo {

    @Autowired
    public HibernateErrorRepo(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public List<Error> getAll() {
        return find("from Error");
    }
}
