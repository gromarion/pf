package com.itba.domain;

import com.itba.domain.model.User;

public interface UserRepo extends HibernateRepo {

    public User get(int userId);

    public User getByName(String name);

}
