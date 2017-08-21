package com.itba.web.page;

import java.util.Map;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

import com.itba.web.charts.DonutChartWithLabels;

import lib.Score;

@SuppressWarnings("serial")
public class ResourceScorePanel extends Panel {
	private Score score;

	public ResourceScorePanel(String id, Score score) {
		super(id);
		this.score = score;
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		DonutChartWithLabels errorsDataChart = new DonutChartWithLabels("resource-score-chart");
		Map<String, Integer> errorsAmount = score.getErrorsAmount();

		for (String error : errorsAmount.keySet()) {
			errorsDataChart.appendData(error, errorsAmount.get(error));
		}

		response.render(JavaScriptHeaderItem
				.forReference(new JavaScriptResourceReference(ResultItemPage.class, "js/d3.min.js")));
		response.render(JavaScriptHeaderItem
				.forReference(new JavaScriptResourceReference(ResultItemPage.class, "js/donut-chart.js")));
		response.render(JavaScriptHeaderItem
				.forReference(new JavaScriptResourceReference(ResultItemPage.class, "js/display-donut-chart.js")));
		response.render(errorsDataChart.getRender());
	}
}
