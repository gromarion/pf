package com.itba.web.charts;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GaugeChart implements Serializable {
	private static final long serialVersionUID = 1L;

	@JsonProperty
	private String id;
	@JsonProperty
	private double value;
	@JsonProperty
	private String circleColor;
	@JsonProperty
	private String textColor;
	@JsonProperty
	private String waveColor;

	private GaugeChart(String id, double value, String circleColor, String textColor, String waveColor) {
		this.id = id;
		this.value = value;
		this.circleColor = circleColor;
		this.textColor = textColor;
		this.waveColor = waveColor;
	}
	
	public static class Builder {
		private String id;
		private double value;
		private String circleColor;
		private String textColor;
		private String waveColor;
		
		private void setId(String id) {
			this.id = id;
		}
		
		private void setValue(double value) {
			this.value = value;
		}
		
		private void setCircleColor(String circleColor) {
			this.circleColor = circleColor;
		}
		
		private void setTextColor(String textColor) {
			this.textColor = textColor;
		}
		
		private void setWaveColor(String waveColor) {
			this.waveColor = waveColor;
		}
		
		private GaugeChart build() {
			return new GaugeChart(id, value, circleColor, textColor, waveColor);
		}
	}
}
