package com.itba.domain.repository;

import java.util.List;

import com.itba.domain.model.Campaign;
import com.itba.domain.repository.hibernate.HibernateRepo;

public interface CampaignRepo extends HibernateRepo {

	public Campaign get(int campaignId);

    public List<Campaign> getAll();
}
