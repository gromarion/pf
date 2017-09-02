package com.itba.formulae;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.wicket.ajax.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Optional;
import com.itba.domain.SparqlRequestHandler;
import com.itba.domain.model.Error;
import com.itba.domain.model.EvaluatedResource;
import com.itba.domain.model.EvaluatedResourceDetail;
import com.itba.domain.repository.EndpointStatsRepo;
import com.itba.domain.repository.EvaluatedResourceRepo;
import com.itba.sparql.JsonSparqlResult;
import com.itba.sparql.ResultItem;
import com.itba.web.WicketSession;

import lib.Score;
import lib.StringUtils;

@Service
public class ManualErrorsFormulae {
	
	@Autowired
	private EvaluatedResourceRepo evaluatedResourceRepo;
	
	@Autowired
	private EndpointStatsRepo endpointStatsRepo;

	// TODO: descablear esto. Sacarlo de la DB, tabla de Error
	private static final int INCORRECT_DATA = 1;
	private static final int INCORRECT_EXTRACTION = 2;
	private static final int SEMANTIC_ERROR = 3;
	private static final int INCORRECT_EXTERNAL_LINK = 4;
	
	public ManualErrorsFormulae() {
	}
	
	public double computeIndividual(String resource, int errorId) throws JSONException, IOException {
		Optional<EvaluatedResource> evaluatedResource = evaluatedResource(resource);
    	List<List<ResultItem>> properties = new JsonSparqlResult(
    			SparqlRequestHandler.requestResource(
    					resource, WicketSession.get().getEvaluationSession().get().getCampaign(), endpointStatsRepo).toString()).data;

        Map<Integer, Double> errors = new HashMap<>();        
    	errors.put(errorId, computeError(evaluatedResource.get(), errorId, properties));
        return getWeightForError(errorId) * errors.get(errorId);
	}
	
	// Esto por ahora es un switch case porque no en todos los casos es un tipo
	// de fórmula erroredOverTotal.
	public Score compute(String resource) throws JSONException, IOException {
    	Optional<EvaluatedResource> evaluatedResource = evaluatedResource(resource);
    	List<List<ResultItem>> properties = new JsonSparqlResult(
    			SparqlRequestHandler.requestResource(
    					resource, WicketSession.get().getEvaluationSession().get().getCampaign(), endpointStatsRepo).toString()).data;

        Map<Integer, Double> errors = new HashMap<>();
        Map<String, Integer> errorsAmount = new HashMap<>();
        double ans = 0;
		if (!evaluatedResource.isPresent()) {
        	return new Score(-1, -1, errorsAmount);
        }
        
        if (evaluatedResource.get().isCorrect()) {
        	return new Score(1, 0, errorsAmount);
        }
        
        for (EvaluatedResourceDetail detail : evaluatedResource.get().getDetails()) {
        	int errorId = detail.getError().getId();
        	addError(errorsAmount, detail.getError());

        	errors.put(errorId, computeError(evaluatedResource.get(), errorId, properties));
        }
        
        Set<Integer> errorIds = errors.keySet();
        
        if (errorIds.isEmpty()) {
        	return new Score(-1, -1, errorsAmount);
        }
        
        for (int errorId : errorIds) {
        	ans += getWeightForError(errorId) * errors.get(errorId);
        }
        
        return new Score(1 - ans, evaluatedResource.get().getDetails().size(), errorsAmount);
	}
	
	private void addError(Map<String, Integer> errorsAmount, Error error) {
		if (errorsAmount.containsKey(error.getName())) {
			errorsAmount.put(error.getName(), errorsAmount.get(error.getName()) + 1);
		} else {
			errorsAmount.put(error.getName(), 1);
		}
	}
	
	private double computeError(EvaluatedResource evaluatedResource, int errorId, List<List<ResultItem>> properties) {
		if (errorId >= INCORRECT_DATA && errorId <= INCORRECT_EXTERNAL_LINK) {
			if (errorId == INCORRECT_EXTERNAL_LINK) {
				return erroredLinksOverTotalLinks(evaluatedResource, errorId, properties);
			} else {
				return erroredOverTotal(evaluatedResource, errorId, properties.size());				
			}
		} else {
			return 0;
		}
	}

	// TODO: Mejorar esta query. Me estoy trayendo todos los evaluated resource
	// details, y filtro por cada uno de ellos en memoria, según el tipo de
	// error que me interesa a mí. Esto tengo que poder filtrarlo en la base de
	// datos, no en memoria.
	private double erroredOverTotal(EvaluatedResource evaluatedResource, int errorId, int propertiesAmount) {
		int numerator = 0;

		for (EvaluatedResourceDetail detail : evaluatedResource.getDetails()) {
			if (detail.getError().getId() == errorId) {
				numerator++;
			}
		}
		return ((double) numerator) / propertiesAmount;
	}
	
	private double erroredLinksOverTotalLinks(EvaluatedResource evaluatedResource, int errorId, List<List<ResultItem>> properties) {
		int numerator = 0;
		int denominator = 0;

		for (EvaluatedResourceDetail detail : evaluatedResource.getDetails()) {
			if (detail.getError().getId() == errorId) {
				numerator++;
			}
		}
		
		for (List<ResultItem> property : properties) {
			if(StringUtils.containsURL(property.get(1).toString())) {
				denominator++;
			}
		}
		
		return ((double) numerator) / denominator;
	}

	private Optional<EvaluatedResource> evaluatedResource(String resource) {
		String escapedResource = resource.replaceAll("'", "''");
		return evaluatedResourceRepo.getResourceForSession(WicketSession.get().getEvaluationSession().get(), escapedResource);
	}
	
	private double getWeightForError(int errorId) {
    	switch (errorId) {
		case INCORRECT_DATA:
			return 0.25;
		case INCORRECT_EXTRACTION:
			return 0.25;
		case SEMANTIC_ERROR:
			return 0.25;
		case INCORRECT_EXTERNAL_LINK:
			return 0.25;
		default:
			return 0;
		}
    }
}
