package com.itba.domain.repository.hibernate;

import com.itba.domain.model.Campaign;
import com.itba.domain.repository.CampaignRepo;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class HibernateCampaignRepo extends AbstractHibernateRepo implements CampaignRepo {

    @Autowired
    public HibernateCampaignRepo(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public List<Campaign> getAll() {
        return find("from Campaign");
    }
}
