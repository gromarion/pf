package com.itba.formulae;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itba.domain.model.Campaign;
import com.itba.domain.repository.EvaluatedResourceRepo;

@Service
public class GlobalFormulae {

	@Autowired
	private EndpointQualityFormulae endpointQualityFormulae;
	@Autowired
	private EvaluatedResourceRepo evaluatedResourceRepo;

	public GlobalFormulae() {
	}

	public double getGlobalScore(Campaign campaign) {
		try {
			return 100
					* (getAvarageResourceQuality(campaign) + endpointQualityFormulae.getScore(campaign).getScore())
					/ 2;
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}
	}

	public double getAvarageResourceQuality(Campaign campaign) {
		double sumScore = evaluatedResourceRepo.getSumScoreByCampaign(campaign).doubleValue();
		int totalResources = evaluatedResourceRepo.getAllByCampaign(campaign).size();

		return totalResources == 0 ? 0 : sumScore / totalResources;
	}
}
