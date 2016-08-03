package com.itba.domain;

import com.itba.domain.model.Error;

import java.util.List;

public interface ErrorRepo extends HibernateRepo {

    public List<Error> getAll();

    public Error get(int hotelId);

}
