package com.itba.web.page;

import java.util.Map;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import lib.ManualErrorsFormulae;

@SuppressWarnings("serial")
public class ResourceScorePanel extends Panel {
	private ManualErrorsFormulae.Score score;

	private static final String CHART_CONTAINER_ID = "#resource-score-chart";

	public ResourceScorePanel(String id, ManualErrorsFormulae.Score score) {
		super(id);
		this.score = score;
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);

		String errorsData = "[";
		Map<String, Integer> errorsAmount = score.getErrorsAmount();
		Object[] errorNames = errorsAmount.keySet().toArray();

		for (int i = 0; i < errorNames.length; i++) {
			errorsData += "{'label': '" + errorNames[i] + "', 'value': " + errorsAmount.get(errorNames[i]) + "}";
			if (i < errorNames.length - 1) {
				errorsData += ", ";
			}
		}
		errorsData += "]";

		response.render(JavaScriptHeaderItem
				.forReference(new JavaScriptResourceReference(ResultItemPage.class, "js/d3.min.js")));
		response.render(JavaScriptHeaderItem
				.forReference(new JavaScriptResourceReference(ResultItemPage.class, "js/donut-chart.js")));
		response.render(JavaScriptHeaderItem
				.forReference(new JavaScriptResourceReference(ResultItemPage.class, "js/display-donut-chart.js")));
		response.render(OnDomReadyHeaderItem
				.forScript("drawChart(" + errorsData + ", '" + CHART_CONTAINER_ID + "');"));
	}
}
