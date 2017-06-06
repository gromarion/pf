package com.itba.domain.repository;

import java.util.List;

import com.itba.domain.model.EvaluatedResourceDetail;
import com.itba.domain.repository.hibernate.HibernateRepo;

public interface EvaluatedResourceDetailRepo extends HibernateRepo {

	List<EvaluatedResourceDetail> getPreviousErrors(String resource, String predicate, String object);
}
