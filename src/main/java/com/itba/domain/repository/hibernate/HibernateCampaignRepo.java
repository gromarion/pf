package com.itba.domain.repository.hibernate;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.itba.domain.model.Campaign;
import com.itba.domain.repository.CampaignRepo;

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

	@Override
	public Campaign get(int campaignId) {
		return get(Campaign.class, campaignId);
	}
}
