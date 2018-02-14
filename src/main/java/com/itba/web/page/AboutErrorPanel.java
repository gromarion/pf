package com.itba.web.page;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.Panel;

import utils.URLHelper;

@SuppressWarnings("serial")
public class AboutErrorPanel extends Panel {
	
	public AboutErrorPanel(String id, String resource, String predicate, String object, boolean isVisible) {
		super(id);

		final ExternalLink predicateLink = new ExternalLink("predicateLink", predicate, predicate);
		final Label objectLabel = new Label("objectLabel", URLHelper.transformURLs(object, false));
		objectLabel.setEscapeModelStrings(false);
		final ExternalLink resourceLink = new ExternalLink("resourceLink", resource, resource);
		
		add(resourceLink);
		add(predicateLink);
		add(objectLabel);
		setVisible(isVisible);
	}
}
