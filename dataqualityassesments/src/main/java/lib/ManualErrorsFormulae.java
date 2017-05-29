package lib;

import org.apache.wicket.model.IModel;
import com.itba.domain.EntityModel;
import com.itba.domain.SparqlRequestHandler;
import com.itba.domain.model.Campaign;
import com.itba.domain.model.EvaluatedResource;
import com.itba.domain.model.EvaluatedResourceDetail;
import com.itba.domain.model.EvaluationSession;
import com.itba.domain.repository.CampaignRepo;
import com.itba.domain.repository.EvaluatedResourceRepo;
import com.itba.sparql.JsonSparqlResult;
import com.itba.web.WicketSession;

public class ManualErrorsFormulae {
	private CampaignRepo campaignRepo;
	private EvaluatedResourceRepo evaluatedResourceRepo;
	private Campaign campaign;

	private static final int INCORRECT_DATA = 1;
	private static final int INCORRECT_EXTRACTION = 2;
	private static final int SEMANTIC_ERROR = 3;
	private static final int INCORRECT_EXTERNAL_LINK = 4;
	
	public ManualErrorsFormulae(CampaignRepo campaginRepo, EvaluatedResourceRepo evaluatedResourceRepo) {
		this.campaignRepo = campaginRepo;
		this.evaluatedResourceRepo = evaluatedResourceRepo;
		this.campaign = campaignRepo.get(Campaign.class, WicketSession.get().getEvaluationSession().get().getCampaign().getId());
	}

	// Esto por ahora es un switch case porque no en todos los casos es un tipo
	// de fórmula erroredOverTotal.
	public double compute(String resource, int errorId) {
		switch (errorId) {
		case INCORRECT_DATA:
			return erroredOverTotal(resource, INCORRECT_DATA);
		case INCORRECT_EXTRACTION:
			return erroredOverTotal(resource, INCORRECT_EXTRACTION);
		case SEMANTIC_ERROR:
			return erroredOverTotal(resource, SEMANTIC_ERROR);
		case INCORRECT_EXTERNAL_LINK:
			return erroredOverTotal(resource, INCORRECT_EXTERNAL_LINK);
		default:
			return 0;
		}
	}

	// TODO: Mejorar esta query. Me estoy trayendo todos los evaluated resource
	// details, y filtro por cada uno de ellos en memoria, según el tipo de
	// error que me interesa a mí. Est tengo que poder filtrarlo en la base de
	// datos, no en memoria.
	private double erroredOverTotal(String resource, int errorId) {
		EvaluatedResource evaluatedResource = evaluatedResource(resource);
		int numerator = 0;

		for (EvaluatedResourceDetail detail : evaluatedResource.getDetails()) {
			if (detail.getError().getId() == errorId) {
				numerator++;
			}
		}
		double denominator = new JsonSparqlResult(
				SparqlRequestHandler.requestResource(resource, campaign).toString()).data.size();

		return numerator / denominator;
	}

	private EvaluatedResource evaluatedResource(String resource) {
		IModel<EvaluationSession> currentSession = new EntityModel<EvaluationSession>(EvaluationSession.class);
		currentSession.setObject(WicketSession.get().getEvaluationSession().get());
		return evaluatedResourceRepo.getResourceForSession(currentSession.getObject(), resource).get();
	}
}
