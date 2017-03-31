package com.itba.web.page;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;

import com.itba.web.WicketSession;

@SuppressWarnings("serial")
public class BasePage extends WebPage {

    public BasePage() {
        WicketSession session = getAppSession();

        if (!session.isSignedIn()) {
            redirectToInterceptPage(new LoginPage());
        }
        
        add(new Link<Void>("home") {

			@Override
			public void onClick() {
				setResponsePage(getApplication().getHomePage());
			}

		});
        Link<Void> logout = new Link<Void>("logout") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				WicketSession.get().signOut();
				setResponsePage(getApplication().getHomePage());
			}
		};
		add(logout);
		Label user = new Label("user", WicketSession.get().getFullName());
		add(user);

		if (!WicketSession.get().isSignedIn()) {
			logout.setVisible(false);
			user.setVisible(false);
		}
    }

    protected WicketSession getAppSession() {
        return (WicketSession) getSession();
    }
}
