package com.itba.domain.repository.hibernate;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.itba.domain.model.EndpointStats;
import com.itba.domain.repository.EndpointStatsRepo;

@Repository
public class HIbernateEndpointStatsRepo  extends AbstractHibernateRepo implements EndpointStatsRepo {

    @Autowired
    public HIbernateEndpointStatsRepo(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public List<EndpointStats> getAll() {
        return find("from Campaign");
    }
}