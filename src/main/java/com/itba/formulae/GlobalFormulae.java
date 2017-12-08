package com.itba.formulae;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itba.domain.model.EvaluationSession;
import com.itba.domain.repository.EvaluatedResourceRepo;
import com.itba.web.WicketSession;

@Service
public class GlobalFormulae {

	@Autowired
	private EndpointQualityFormulae endpointQualityFormulae;
	@Autowired
	private EvaluatedResourceRepo evaluatedResourceRepo;

	public GlobalFormulae() {
	}

	public double getAverageDocumentQuality() {
		EvaluationSession session = WicketSession.get().getEvaluationSession().get();
		double sumScore = evaluatedResourceRepo.getSumScoreByCampaign(session.getCampaign()).doubleValue();
		int totalResources = evaluatedResourceRepo.getAllByCampaign(session.getCampaign()).size();
		
		try {
			return 100 * ((sumScore / totalResources) + endpointQualityFormulae.getScore(session.getCampaign()).getScore()) / 2;			
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}
	}
}
