package com.itba.formulae;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itba.domain.SparqlRequestHandler;
import com.itba.domain.model.Campaign;
import com.itba.domain.model.EndpointStats;
import com.itba.domain.model.Error;
import com.itba.domain.repository.EndpointStatsRepo;
import com.itba.domain.repository.ErrorRepo;
import com.itba.domain.repository.EvaluatedResourceDetailRepo;

@Service
public class EndpointQualityFormulae {

	@Autowired
	private EndpointStatsRepo endpointStatsRepo;
	@Autowired
	private EvaluatedResourceDetailRepo evaluatedResourceDetailRepo;
	@Autowired
	private ErrorRepo errorRepo;

	public EndpointQualityFormulae() {
	}

	public EndpointScore getScore(Campaign campaign) throws IOException {
		return new EndpointScore(endpointStatsRepo, evaluatedResourceDetailRepo, errorRepo, campaign);
	}

	public static class EndpointScore implements Serializable {
		private static final long serialVersionUID = 1L;

		private Campaign campaign;
		private EndpointStatsRepo endpointStatsRepo;
		private EvaluatedResourceDetailRepo evaluatedResourceDetailRepo;
		private ErrorRepo errorRepo;
		private double score;

		public EndpointScore(EndpointStatsRepo endpointStatsRepo,
				EvaluatedResourceDetailRepo evaluatedResourceDetailRepo,
				ErrorRepo errorRepo,
				Campaign campaign) throws IOException {
			this.campaign = campaign;
			this.endpointStatsRepo = endpointStatsRepo;
			this.evaluatedResourceDetailRepo = evaluatedResourceDetailRepo;
			this.errorRepo = errorRepo;
			this.score = computeScore();
		}

		public String getEndpointURL() {
			return campaign.getEndpoint(); // se llama desde el EndpointScorePanel
		}
		
		public Campaign getCampaign() {
			return campaign;
		}

		public Map<Error, Double> getErrorTypeStats() {
			Map<Error, Double> qtyByError = new HashMap<>();
			long total = 0;

			for (Error error : errorRepo.getAll()) {
				// reemplazar por getQtyByError si campaign no está
				double errorsAmount = evaluatedResourceDetailRepo.getQtyByErrorAndCampaign(error, campaign);
				total += errorsAmount;
				qtyByError.put(error, errorsAmount);
			}
			if (total > 0) {
				for (Error error : qtyByError.keySet()) {
					// reemplazar por getQtyByError si campaign no está
					long errorsAmount = evaluatedResourceDetailRepo.getQtyByErrorAndCampaign(error, campaign);
					qtyByError.put(error, 100 * (double) errorsAmount / total);
				}
			}
			return qtyByError;
		}
		
		public List<EndpointStats> getEndpointStats() {
			// TODO: cambiarlo para que devuelva un pama de endpoint -> EndpointStats??
			return endpointStatsRepo.getAllForEndpoint(campaign.getEndpoint());
		}
		
		public BigDecimal getSuccessfulRequestsRatio() {
			return endpointStatsRepo.getSuccessfulRequestsRatio(campaign.getEndpoint());
		}
		
		public double getScore() {
			return score;
		}

		public String getScoreString() {
			if (score < 0.001) {
				return "100";
			} else {
				return score * 100 + "";				
			}
		}

		private double computeScore() throws IOException {
			int successfulResponses = 0;
			int erroredResponses = 0;

			for (EndpointStats stats : getEndpointStats()) {
				switch (stats.getStatusCode().charAt(0)) {
				case '2':
					successfulResponses++;
					break;
				case '3':
					successfulResponses++;
					break;
				case '5':
					erroredResponses++;
					break;
				default:
					break;
				}
			}
			double availabilityScore;
			if (successfulResponses + erroredResponses == 0) {
				availabilityScore = 1;
			} else {
				availabilityScore = 1 - ((double) erroredResponses / (successfulResponses + erroredResponses));
			}
			int licenseScore = SparqlRequestHandler.hasLicense(campaign, endpointStatsRepo) ? 1 : 0;
			return (availabilityScore + licenseScore) / 2;
		}
	}
}
