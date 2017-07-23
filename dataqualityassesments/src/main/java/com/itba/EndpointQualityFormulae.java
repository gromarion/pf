package com.itba;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itba.domain.SparqlRequestHandler;
import com.itba.domain.model.Campaign;
import com.itba.domain.model.EndpointStats;
import com.itba.domain.repository.CampaignRepo;
import com.itba.domain.repository.EndpointStatsRepo;
import com.itba.web.WicketSession;

import lib.StringUtils;

@Service
public class EndpointQualityFormulae {

	@Autowired
	private EndpointStatsRepo endpointStatsRepo;
	@Autowired
	private CampaignRepo campaignRepo;

	public EndpointQualityFormulae() {
	}

	public EndpointScore getScore() throws IOException {
		return new EndpointScore(campaignRepo.get(Campaign.class,
				WicketSession.get().getEvaluationSession().get().getCampaign().getId()), endpointStatsRepo);
	}

	public static class EndpointScore implements Serializable {
		private static final long serialVersionUID = 1L;

		private Campaign campaign;
		private EndpointStatsRepo endpointStatsRepo;
		private String score;

		public EndpointScore(Campaign campaign, EndpointStatsRepo endpointStatsRepo) throws IOException {
			this.campaign = campaign;
			this.endpointStatsRepo = endpointStatsRepo;
			this.score = computeScore();
		}

		public String getEndpointURL() {
			return campaign.getEndpoint();
		}

		public List<EndpointStats> getEndpointStats() {
			return endpointStatsRepo.getAllForEndpoint(campaign.getEndpoint());
		}

		public String getScore() {
			return score;
		}

		private String computeScore() throws IOException {
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
			if (erroredResponses == 0) {
				return StringUtils.letterQualification(1) + " - 1";
			} else {
				double availabilityScore = 1 - ((double) erroredResponses / (successfulResponses + erroredResponses));
				int licenseScore = SparqlRequestHandler.hasLicense(campaign, endpointStatsRepo) ? 1 : 0;
				double score = (availabilityScore + licenseScore) / 2;

				return StringUtils.letterQualification(score) + " - " + StringUtils.formatDouble(score, 2);
			}
		}
	}
}
