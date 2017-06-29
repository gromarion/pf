package lib;

import com.itba.domain.model.Campaign;
import com.itba.domain.model.EvaluationSession;
import com.itba.domain.repository.CampaignRepo;
import com.itba.domain.repository.EndpointStatsRepo;
import com.itba.domain.repository.EvaluatedResourceRepo;
import com.itba.web.WicketSession;

public class GeneralFormula {

	private static final int FACTOR = 1000000;

	private CampaignRepo campaignRepo;
	private EndpointStatsRepo endpointStatsRepo;
	private EvaluatedResourceRepo evaluatedResourceRepo;

	public GeneralFormula(EndpointStatsRepo endpointStatsRepo, EvaluatedResourceRepo evaluatedResourceRepo,
			CampaignRepo campaignRepo) {
		this.campaignRepo = campaignRepo;
		this.endpointStatsRepo = endpointStatsRepo;
		this.evaluatedResourceRepo = evaluatedResourceRepo;
	}

	public long calculate() {
		EvaluationSession session = WicketSession.get().getEvaluationSession().get();
		Campaign campaign = campaignRepo.get(Campaign.class, session.getCampaign().getId());

		int correctResourcesCount = evaluatedResourceRepo.getCorrectForSession(session).size();
		int totalResourcesCount = evaluatedResourceRepo.getAllForSession(session).size();

		int successfulRequestsCount = endpointStatsRepo.getSuccessfulRequests(campaign.getEndpoint()).size();
		int totalRequestsCount = endpointStatsRepo.getAllForEndpoint(campaign.getEndpoint()).size();

		return Math.round(FACTOR * ((double) correctResourcesCount + successfulRequestsCount)
				/ (totalResourcesCount + totalRequestsCount));
	}
}
