package lib;

import java.io.Serializable;
import java.util.Map;

@SuppressWarnings("serial")
public class Score implements Serializable {

	private double score;
	private int errors;
	private Map<String, Integer> errorsAmount;
	
	public Score(double score, int errors, Map<String, Integer> errorsAmount) {
		this.score = score;
		this.errors = errors;
		this.errorsAmount = errorsAmount;
	}
	
	public double getScore() {
		return score;
	}
	
	public int getErrors() {
		return errors;
	}
	
	public Map<String, Integer> getErrorsAmount() {
		return errorsAmount;
	}
	
	public String scoreString() {
		if (score < 0) {
			return "";
		}

		return StringUtils.formatDouble(score, 3);
	}
	
	public String errorsString() {
		if (errors < 0) {
			return "";
		}

		return errors + "";
	}
	
	public String toString() {
		return StringUtils.letterQualification(score * 100) + " - " + scoreString();
	}
}
