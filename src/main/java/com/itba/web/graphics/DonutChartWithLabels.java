package com.itba.web.graphics;

import java.util.List;

import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.util.string.Strings;

import com.google.common.collect.Lists;

public class DonutChartWithLabels {

	private static final String dataSeparator = ",";
	private final String id;
	private final List<String> data = Lists.newLinkedList();
	
	public DonutChartWithLabels(String id) {
		this.id = id;
	}
	
	public String getId() {
		return this.id;
	}
	
	public void appendData(String key, String value) {
		data.add("{'label': '" + key + "', 'value': " + value + "}");
	}
	
	private String getJson() {
		String joinedData = Strings.join(dataSeparator, data);
		return String.format("[%s]", joinedData);
	}
	
	public HeaderItem getRender() {
		return OnDomReadyHeaderItem.forScript("drawChart(" + getJson() + ", '" + "#" + id + "');");
	}
}
