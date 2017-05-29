package com.itba.domain.repository;

import com.itba.domain.model.Error;
import com.itba.domain.repository.hibernate.HibernateRepo;

import java.util.List;

public interface ErrorRepo extends HibernateRepo {

    public List<Error> getAll();

    public Error get(int errorId);
}
