package com.itba.web.charts;

import java.util.List;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.util.string.Strings;

import com.google.common.collect.Lists;

public class GaugeChart {
	private static final String DATA_SEPARATOR = ",";

	private final List<String> data = Lists.newLinkedList();

	public void appendData(String id, double value, String circleColor, String textColor, String waveColor) {
		data.add("{'id': '" + id + "', 'value': '" + value + "', 'circleColor': '" + circleColor + "', 'textColor': '"
				+ textColor + "', 'waveColor': '" + waveColor + "'}");
	}

	public HeaderItem getRender() {
		String joinedData = Strings.join(DATA_SEPARATOR, data);
		String json = String.format("[%s]", joinedData);

		return OnDomReadyHeaderItem.forScript("displayGaugeCharts(" + json + ");");
	}
}
