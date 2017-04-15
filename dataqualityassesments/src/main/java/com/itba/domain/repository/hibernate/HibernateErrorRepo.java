package com.itba.domain.repository.hibernate;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.itba.domain.model.Error;
import com.itba.domain.repository.ErrorRepo;

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

    @Override
    public Error get(int errorId) {
        return get(Error.class, errorId);
    }

}
