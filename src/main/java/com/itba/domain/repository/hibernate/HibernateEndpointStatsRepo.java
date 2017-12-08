package com.itba.domain.repository.hibernate;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.itba.domain.model.EndpointStats;
import com.itba.domain.repository.EndpointStatsRepo;

@Repository
public class HibernateEndpointStatsRepo extends AbstractHibernateRepo implements EndpointStatsRepo, Serializable {
	private static final long serialVersionUID = 1L;

	@Autowired
	public HibernateEndpointStatsRepo(SessionFactory sessionFactory) {
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
	
	@Override
	public BigDecimal getSuccessfulRequestsRatio(String endpointURL) {
		BigDecimal endpointStatsCount = BigDecimal.valueOf(getAllForEndpoint(endpointURL).size());
		if (endpointStatsCount.intValue() == 0) {
			return new BigDecimal(0);
		}
		BigDecimal successfullEndpointStatsCount = (BigDecimal.valueOf((Long)getSession().createQuery(
			"SELECT count(*) FROM EndpointStats e"
			+ " WHERE e.endpointUrl = '" + endpointURL + "'"
			+ " AND (e.statusCode LIKE '2%' OR e.statusCode LIKE '3%')").list().get(0)));
		return successfullEndpointStatsCount.divide(endpointStatsCount, 3, RoundingMode.HALF_EVEN)
				.multiply(BigDecimal.valueOf(100)).setScale(1, RoundingMode.HALF_EVEN);
	}
}
