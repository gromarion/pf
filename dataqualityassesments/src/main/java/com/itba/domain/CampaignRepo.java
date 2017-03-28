package com.itba.domain;

import com.itba.domain.model.Campaign;

import java.util.List;

public interface CampaignRepo extends HibernateRepo {

    public List<Campaign> getAll();
}
