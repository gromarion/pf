package lib;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.model.IModel;

import com.google.common.base.Optional;
import com.itba.domain.EntityModel;
import com.itba.domain.SparqlRequestHandler;
import com.itba.domain.model.Campaign;
import com.itba.domain.model.EvaluatedResource;
import com.itba.domain.model.EvaluatedResourceDetail;
import com.itba.domain.model.EvaluationSession;
import com.itba.domain.repository.CampaignRepo;
import com.itba.domain.repository.EvaluatedResourceRepo;
import com.itba.sparql.JsonSparqlResult;
import com.itba.sparql.ResultItem;
import com.itba.web.WicketSession;

public class ManualErrorsFormulae {
	private EvaluatedResourceRepo evaluatedResourceRepo;
	private Campaign campaign;

	private static final int INCORRECT_DATA = 1;
	private static final int INCORRECT_EXTRACTION = 2;
	private static final int SEMANTIC_ERROR = 3;
	private static final int INCORRECT_EXTERNAL_LINK = 4;
	
	private static final int FACTOR = 1000000;
	
	public ManualErrorsFormulae(CampaignRepo campaignRepo, EvaluatedResourceRepo evaluatedResourceRepo) {
		this.evaluatedResourceRepo = evaluatedResourceRepo;
		this.campaign = campaignRepo.get(Campaign.class, WicketSession.get().getEvaluationSession().get().getCampaign().getId());
	}

	public String stringCompute(String resource) {
		long resourceScore = compute(resource);
    	
    	return resourceScore == -1 ? "-" : resourceScore + "";
	}
	
	// Esto por ahora es un switch case porque no en todos los casos es un tipo
	// de fórmula erroredOverTotal.
	public long compute(String resource) {
		IModel<EvaluationSession> currentSession = new EntityModel<EvaluationSession>(EvaluationSession.class);
    	currentSession.setObject(WicketSession.get().getEvaluationSession().get());
    	Optional<EvaluatedResource> evaluatedResource = evaluatedResource(resource);
    	List<List<ResultItem>> properties = new JsonSparqlResult(SparqlRequestHandler.requestResource(resource, campaign).toString()).data;

        Map<Integer, Double> errors = new HashMap<Integer, Double>();
        double ans = 0;
		if (!evaluatedResource.isPresent()) {
        	return -1;
        }
        
        if (evaluatedResource.get().isCorrect()) {
        	return FACTOR * properties.size();
        }
        
        for (EvaluatedResourceDetail detail : evaluatedResource.get().getDetails()) {
        	int errorId = detail.getError().getId();

        	errors.put(errorId, computeError(evaluatedResource.get(), errorId, properties.size()));
        }
        for (Integer errorId : errors.keySet()) {
        	ans += getWeightForError(errorId) * errors.get(errorId);
        }
        
        return Math.round((1 - ans) * FACTOR / properties.size());
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
}
