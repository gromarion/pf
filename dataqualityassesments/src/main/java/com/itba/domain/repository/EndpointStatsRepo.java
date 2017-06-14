package com.itba.domain.repository;

import java.util.List;
import com.itba.domain.model.EndpointStats;
import com.itba.domain.repository.hibernate.HibernateRepo;

public interface EndpointStatsRepo extends HibernateRepo {

	public List<EndpointStats> getAll();
	
	public List<EndpointStats> getAllForEndpoint(String endpointURL);
}
