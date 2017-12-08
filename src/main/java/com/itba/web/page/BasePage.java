package com.itba.web.page;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.itba.domain.model.User;
import com.itba.domain.repository.UserRepo;
import com.itba.web.WicketSession;

@SuppressWarnings("serial")
public class BasePage extends WebPage {

	@SpringBean
	private UserRepo userRepo;

	public BasePage() {
		WicketSession session = getAppSession();
		if (!session.isSignedIn()) {
			redirectToInterceptPage(new LoginPage());
		}
		
		boolean guest = userRepo.getByUsername(session.getUsername()).hasRole("GUEST");
		User currentUser = userRepo.getByUsername(session.getUsername());
		
		add(new Link<Void>("home") {
			@Override
			public void onClick() {
				setResponsePage(getApplication().getHomePage());
			}
		});

		BookmarkablePageLink<ErrorsByUserPage> errorsByUser = new BookmarkablePageLink<>("errorsByUserPage",
				ErrorsByUserPage.class);
		errorsByUser.add(new Label("errorsByUserPageLabel", currentUser.hasRole(User.ADMIN_ROLE) ?
				getString("errorsByUserPageAdminLink") : getString("errorsByUserPageLink")));
		errorsByUser.setVisible(!guest);
		add(errorsByUser);

		Link<Void> editUserProfile = new Link<Void>("editUserProfile") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new EditUserProfilePage(WicketSession.get().getUser()));
			}
		};
		
		Link<Void> adminEndpointsLink = new Link<Void>("adminEndpointsLink") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new AdminEndpointsPage());
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

		add(adminEndpointsLink.setVisible(currentUser.hasRole(User.ADMIN_ROLE)));
		editUserProfile.setVisible(!guest);
		adminUsers.setVisible(currentUser.hasRole(User.ADMIN_ROLE));
		add(editUserProfile);
		add(adminUsers);
		add(logout);
		Label user = new Label("user", WicketSession.get().getFullName());
		add(user);

		if (!WicketSession.get().isSignedIn()) {
			logout.setVisible(false);
			user.setVisible(false);
		}

		add(new BookmarkablePageLink<RegisterUserPage>("reportsLink", ReportsPage.class));
	}

	protected WicketSession getAppSession() {
		return (WicketSession) getSession();
	}
}
