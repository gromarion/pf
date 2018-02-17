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
}
