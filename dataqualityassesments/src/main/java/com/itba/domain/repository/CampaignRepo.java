package com.itba.domain.repository;

import com.itba.domain.model.Campaign;
import com.itba.domain.repository.hibernate.HibernateRepo;

import java.util.List;

public interface CampaignRepo extends HibernateRepo {

    public List<Campaign> getAll();
}
