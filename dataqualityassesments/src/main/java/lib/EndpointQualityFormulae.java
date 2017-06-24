package lib;

import java.io.Serializable;
import java.util.List;

import com.itba.domain.model.EndpointStats;
import com.itba.domain.repository.EndpointStatsRepo;

public class EndpointQualityFormulae implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final int MULTIPLYER = 1000000;
	
	private EndpointStatsRepo endpointStatsRepo;
	
	public EndpointQualityFormulae(EndpointStatsRepo endpointStatsRepo) {
		this.endpointStatsRepo = endpointStatsRepo;
	}
	
	public EndpointScore getScore(String endpointURL) {
		return new EndpointScore(endpointURL, endpointStatsRepo.getAllForEndpoint(endpointURL));
	}
	
	public static class EndpointScore implements Serializable {
		private static final long serialVersionUID = 1L;

		private String endpointURL;
		private List<EndpointStats> endpointStats; 
		private int score;
		
		public EndpointScore(String endpointURL, List<EndpointStats> endpointStats) {
			this.endpointURL = endpointURL;
			this.endpointStats = endpointStats;
			this.score = computeScore();
		}
		
		public String getEndpointURL() {
			return endpointURL;
		}
		
		public List<EndpointStats> getEndpointStats() {
			return endpointStats;
		}
		
		public long getScore() {
			return score;
		}
		
		private int computeScore() {
			int successfulResponses = 0;
			int erroredResponses = 0;

			for (EndpointStats stats : endpointStats) {
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
				return MULTIPLYER;
			} else {
				return (1 - (successfulResponses / (successfulResponses + erroredResponses))) * MULTIPLYER;
			}
		}
	}
}
