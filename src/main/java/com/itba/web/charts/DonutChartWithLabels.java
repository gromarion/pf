package com.itba.web.charts;

import java.util.List;

import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.util.string.Strings;

import com.google.common.collect.Lists;

public class DonutChartWithLabels {
	private static final String DATA_SEPARATOR = ",";

	private final String id;
	private final List<String> data = Lists.newLinkedList();
	
	public DonutChartWithLabels(String id) {
		this.id = id;
	}
	
	public void appendData(String key, double value) {
		data.add("{'label': '" + key + "', 'value': '" + value + "'}");
	}
		
	public HeaderItem getRender() {
		String joinedData = Strings.join(DATA_SEPARATOR, data);
		String json = String.format("[%s]", joinedData);

		return OnDomReadyHeaderItem.forScript("drawChart(" + json + ", '#" + id + "');");
	}
}
