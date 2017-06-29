package lib;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.wicket.ajax.json.JSONException;
import org.apache.wicket.model.IModel;
import com.google.common.base.Optional;
import com.itba.domain.EntityModel;
import com.itba.domain.model.Error; 
import com.itba.domain.SparqlRequestHandler;
import com.itba.domain.model.Campaign;
import com.itba.domain.model.EvaluatedResource;
import com.itba.domain.model.EvaluatedResourceDetail;
import com.itba.domain.model.EvaluationSession;
import com.itba.domain.repository.CampaignRepo;
import com.itba.domain.repository.EndpointStatsRepo;
import com.itba.domain.repository.EvaluatedResourceRepo;
import com.itba.sparql.JsonSparqlResult;
import com.itba.sparql.ResultItem;
import com.itba.web.WicketSession;

public class ManualErrorsFormulae {
	private EvaluatedResourceRepo evaluatedResourceRepo;
	private Campaign campaign;
	private EndpointStatsRepo endpointStatsRepo;

	private static final int INCORRECT_DATA = 1;
	private static final int INCORRECT_EXTRACTION = 2;
	private static final int SEMANTIC_ERROR = 3;
	private static final int INCORRECT_EXTERNAL_LINK = 4;
	
	private static final int FACTOR = 1000000;
	
	public ManualErrorsFormulae(CampaignRepo campaignRepo, EvaluatedResourceRepo evaluatedResourceRepo, EndpointStatsRepo endpointStatsRepo) {
		this.evaluatedResourceRepo = evaluatedResourceRepo;
		this.campaign = campaignRepo.get(Campaign.class, WicketSession.get().getEvaluationSession().get().getCampaign().getId());
		this.endpointStatsRepo = endpointStatsRepo;
	}
	
	// Esto por ahora es un switch case porque no en todos los casos es un tipo
	// de fórmula erroredOverTotal.
	public Score compute(String resource) throws JSONException, IOException {
		IModel<EvaluationSession> currentSession = new EntityModel<EvaluationSession>(EvaluationSession.class);
    	currentSession.setObject(WicketSession.get().getEvaluationSession().get());
    	Optional<EvaluatedResource> evaluatedResource = evaluatedResource(resource);
    	List<List<ResultItem>> properties = new JsonSparqlResult(SparqlRequestHandler.requestResource(resource, campaign, endpointStatsRepo).toString()).data;

        Map<Integer, Double> errors = new HashMap<>();
        Map<String, Integer> errorsAmount = new HashMap<>();
        double ans = 0;
		if (!evaluatedResource.isPresent()) {
        	return new Score(-1, -1, errorsAmount);
        }
        
        if (evaluatedResource.get().isCorrect()) {
        	return new Score(FACTOR * properties.size(), 0, errorsAmount);
        }
        
        for (EvaluatedResourceDetail detail : evaluatedResource.get().getDetails()) {
        	int errorId = detail.getError().getId();
        	addError(errorsAmount, detail.getError());

        	errors.put(errorId, computeError(evaluatedResource.get(), errorId, properties.size()));
        }
        for (Integer errorId : errors.keySet()) {
        	ans += getWeightForError(errorId) * errors.get(errorId);
        }
        
        return new Score(Math.round((1 - ans / properties.size()) * FACTOR), evaluatedResource.get().getDetails().size(), errorsAmount);
	}
	
	private void addError(Map<String, Integer> errorsAmount, Error error) {
		if (errorsAmount.containsKey(error.getName())) {
			errorsAmount.put(error.getName(), errorsAmount.get(error.getName()) + 1);
		} else {
			errorsAmount.put(error.getName(), 1);
		}
	}
	
	private double computeError(EvaluatedResource evaluatedResource, int errorId, int propertiesAmount) {
		if (errorId >= INCORRECT_DATA && errorId <= INCORRECT_EXTERNAL_LINK) {
			return erroredOverTotal(evaluatedResource, errorId, propertiesAmount);			
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

	private Optional<EvaluatedResource> evaluatedResource(String resource) {
		IModel<EvaluationSession> currentSession = new EntityModel<EvaluationSession>(EvaluationSession.class);
		currentSession.setObject(WicketSession.get().getEvaluationSession().get());
		String escapedResource = resource.replaceAll("'", "''");

		return evaluatedResourceRepo.getResourceForSession(currentSession.getObject(), escapedResource);
	}
	
	private double getWeightForError(int errorId) {
    	// TODO: Implementar entidad que almacena los pesos asociados a cada error.
    	return 0.25;
    }
	
	@SuppressWarnings("serial")
	public static class Score implements Serializable {
		private long score;
		private int errors;
		private Map<String, Integer> errorsAmount;
		
		public Score(long score, int errors, Map<String, Integer> errorsAmount) {
			this.score = score;
			this.errors = errors;
			this.errorsAmount = errorsAmount;
		}
		
		public long getScore() {
			return score;
		}
		
		public int getErrors() {
			return errors;
		}
		
		public Map<String, Integer> getErrorsAmount() {
			return errorsAmount;
		}
		
		public String scoreString() {
			String scoreString = String.format("%,d", score);
			return stringValue(scoreString);
		}
		
		public String errorsString() {
			return stringValue(errors + "");
		}
		
		private String stringValue(String value) {
			return value.equals("-1") ? "-" : value;
		}
	}
}
