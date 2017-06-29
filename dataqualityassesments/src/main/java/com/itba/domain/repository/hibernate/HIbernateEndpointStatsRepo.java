package com.itba.domain.repository.hibernate;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.itba.domain.model.EndpointStats;
import com.itba.domain.repository.EndpointStatsRepo;

@Repository
public class HIbernateEndpointStatsRepo extends AbstractHibernateRepo implements EndpointStatsRepo {

	@Autowired
	public HIbernateEndpointStatsRepo(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public List<EndpointStats> getAll() {
		return find("from EndpointStats");
	}

	@Override
	public List<EndpointStats> getAllForEndpoint(String endpointURL) {
		return find("from EndpointStats where endpoint_url = ?", endpointURL);
	}

	@Override
	public List<EndpointStats> getSuccessfulRequests(String endpointURL) {
		return find("from EndpointStats where endpoint_url = ? AND (statuscode LIKE '2%' OR statuscode LIKE '3%')",
				endpointURL);
	}
}
