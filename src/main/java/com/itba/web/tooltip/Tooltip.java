package com.itba.web.tooltip;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;

public class Tooltip {

	public enum Position {
		LEFT("left"),
		RIGHT("right"),
		TOP("top"),
		BOTTOM("bottom");
		
		private final String position;
		
		Position (String position) {
			this.position = position;
		}
		
		public String getPosition() {
			return this.position;
		}
	}
	
	public static void addToComponent(final Component component, Position position, String title) {
		component.add(new AttributeModifier("data-toggle", "tooltip"));
		component.add(new AttributeModifier("data-placement", position.getPosition()));
		component.add(new AttributeModifier("title", title));
	}
}
