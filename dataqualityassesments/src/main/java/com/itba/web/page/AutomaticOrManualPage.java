package com.itba.web.page;

import org.apache.wicket.markup.html.link.BookmarkablePageLink;

public class AutomaticOrManualPage extends BasePage {
	private static final long serialVersionUID = 1L;
	
	@Override
    protected void onInitialize() {
		super.onInitialize();
		add(new BookmarkablePageLink<RegisterUserPage>("automaticCheckLink", AutomaticCheckPage.class));
		add(new BookmarkablePageLink<RegisterUserPage>("manualCheckLink", ResourceSearchPage.class));
		add(new BookmarkablePageLink<CreateEndpointPage>("createEndpointLink", CreateEndpointPage.class));
		add(new BookmarkablePageLink<ErrorsByUserPage>("errorsByUserPage", ErrorsByUserPage.class));
	}
}
