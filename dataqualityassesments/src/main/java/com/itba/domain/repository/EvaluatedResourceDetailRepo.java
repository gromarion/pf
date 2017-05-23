package com.itba.domain.repository;

import java.util.List;

import com.itba.domain.model.Error;
import com.itba.domain.repository.hibernate.HibernateRepo;

public interface EvaluatedResourceDetailRepo extends HibernateRepo {

	List<Error> getPreviousErrors(String resource, String predicate, String object);
}
