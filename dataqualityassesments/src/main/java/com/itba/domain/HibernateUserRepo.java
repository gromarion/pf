package com.itba.domain;

import com.itba.domain.model.User;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class HibernateUserRepo extends AbstractHibernateRepo implements UserRepo {

    @Autowired
    public HibernateUserRepo(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public User get(int userId) {
        return get(User.class, userId);
    }

    @Override
    public User getByName(String name) {
        List<User> result = find("from User where name = ?", name);
        return result.size() > 0 ? result.get(0) : null;
    }
}