package com.itba.domain.repository;

import java.math.BigDecimal;
import java.util.List;
import com.itba.domain.model.EndpointStats;
import com.itba.domain.repository.hibernate.HibernateRepo;

public interface EndpointStatsRepo extends HibernateRepo {

	List<EndpointStats> getAll();
	
	List<EndpointStats> getAllForEndpoint(String endpointURL);
	
	List<EndpointStats> getSuccessfulRequests(String endpointURL);

	BigDecimal getSuccessfulRequestsRatio(String endpointURL);
}
