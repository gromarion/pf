package com.itba.web.page;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
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

		add(new BookmarkablePageLink<ErrorsByUserPage>("errorsByUserPage", ErrorsByUserPage.class));

		Link<Void> editUserProfile = new Link<Void>("editUserProfile") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new EditUserProfilePage(WicketSession.get().getUser()));
			}
		};

		Link<Void> logout = new Link<Void>("logout") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				WicketSession.get().signOut();
				setResponsePage(getApplication().getHomePage());
			}
		};

		add(editUserProfile);
		add(logout);
		Label user = new Label("user", WicketSession.get().getFullName());
		add(user);

		if (!WicketSession.get().isSignedIn()) {
			logout.setVisible(false);
			user.setVisible(false);
		}

		add(new BookmarkablePageLink<RegisterUserPage>("automaticCheckLink", AutomaticCheckPage.class));
		add(new BookmarkablePageLink<RegisterUserPage>("manualCheckLink", HomePage.class));
		add(new BookmarkablePageLink<RegisterUserPage>("reportsLink", ReportsPage.class));
	}

	protected WicketSession getAppSession() {
        return (WicketSession) getSession();
    }
}
