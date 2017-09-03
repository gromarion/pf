package com.itba.formulae;

import java.io.IOException;

import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itba.domain.model.EvaluationSession;
import com.itba.domain.repository.EvaluatedResourceRepo;
import com.itba.web.WicketSession;

@Service
public class GlobalFormulae {

	@SpringBean
	private EndpointQualityFormulae endpointQualityFormulae;

	@Autowired
	private EvaluatedResourceRepo evaluatedResourceRepo;

	public GlobalFormulae() {
	}

	public double getGlobalScore() throws IOException {
		EvaluationSession session = WicketSession.get().getEvaluationSession().get();
		double erroredResources = evaluatedResourceRepo.getErroredForSession(session).size();
		int totalResources = evaluatedResourceRepo.getAllForSession(session).size();

		return (1 - (erroredResources / totalResources) + endpointQualityFormulae.getScore().getScore()) / 2;
	}
}
