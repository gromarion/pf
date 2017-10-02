package com.itba.domain.repository;

import java.util.List;

import com.itba.domain.model.User;
import com.itba.domain.repository.hibernate.HibernateRepo;

public interface UserRepo extends HibernateRepo {

    public User get(int userId);

    public User getByUsername(String name);
    
    public List<User> getAll();

}
