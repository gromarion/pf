package lib;

import java.util.List;

public class WeightedAverage {
	private List<WeightAndValue> weightsAndvalues;
	
	private WeightedAverage(List<WeightAndValue> weightsAndValues) {
		this.weightsAndvalues = weightsAndValues;
	}
	
	public double compute() {
		double ans = 0;
		
		for (WeightAndValue weightAndValue : weightsAndvalues) {
			ans += weightAndValue.getWeight() * weightAndValue.getValue();
		}
		
		return ans;
	}

	public static class Builder {
		private List<WeightAndValue> weightsAndValues;
		
		public Builder weightAndValue(double weight, int value) {
			weightsAndValues.add(new WeightAndValue(weight, value));
			
			return this;
		} 
	}
	
	private static class WeightAndValue {
		private double weight;
		private int value;
		
		public WeightAndValue(double weight, int value) {
			this.weight = weight;
			this.value = value;
		}
		
		public double getWeight() {
			return weight;
		}
		
		public int getValue() {
			return value;
		}
	}
}
