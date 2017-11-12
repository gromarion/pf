package com.itba.web.page;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.itba.domain.repository.UserRepo;
import com.itba.web.WicketSession;

@SuppressWarnings("serial")
public class BasePage extends WebPage {

	@SpringBean
	private UserRepo userRepo;

	public BasePage() {
		WicketSession session = getAppSession();
		boolean guest = userRepo.getByUsername(session.getUsername()).hasRole("GUEST");

		if (!session.isSignedIn()) {
			redirectToInterceptPage(new LoginPage());
		}

		add(new Link<Void>("home") {
			@Override
			public void onClick() {
				setResponsePage(getApplication().getHomePage());
			}
		});

		BookmarkablePageLink<ErrorsByUserPage> errorsByUser = new BookmarkablePageLink<>("errorsByUserPage",
				ErrorsByUserPage.class);

		errorsByUser.setVisible(!guest);
		add(errorsByUser);

		Link<Void> editUserProfile = new Link<Void>("editUserProfile") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new EditUserProfilePage(WicketSession.get().getUser()));
			}
		};

		Link<Void> adminUsers = new Link<Void>("adminUsers") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new AdminUsersPage());
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

		editUserProfile.setVisible(!guest);
		adminUsers.setVisible(!guest);
		add(editUserProfile);
		add(adminUsers);
		add(logout);
		Label user = new Label("user", WicketSession.get().getFullName());
		add(user);

		if (!WicketSession.get().isSignedIn()) {
			logout.setVisible(false);
			user.setVisible(false);
		}

		BookmarkablePageLink<RegisterUserPage> automaticCheck = new BookmarkablePageLink<>("automaticCheckLink",
				AutomaticCheckPage.class);
		BookmarkablePageLink<RegisterUserPage> manualCheck = new BookmarkablePageLink<>("manualCheckLink",
				ResourceSearchPage.class);
		
		automaticCheck.setVisible(!guest);
		manualCheck.setVisible(!guest);
		add(automaticCheck);
		add(manualCheck);
	}

	protected WicketSession getAppSession() {
		return (WicketSession) getSession();
	}
}
