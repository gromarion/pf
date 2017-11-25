package com.itba.web.page;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.itba.domain.model.User;
import com.itba.domain.repository.EvaluationSessionRepo;
import com.itba.domain.repository.UserRepo;
import com.itba.web.feedback.CustomFeedbackPanel;

@SuppressWarnings("serial")
@AuthorizeInstantiation({ "ADMIN" })
public class AdminUsersPage extends BasePage {

	@SpringBean
	private UserRepo userRepo;
	@SpringBean
	private EvaluationSessionRepo evaluationSessionRepo;

	public AdminUsersPage() {
		add(new CustomFeedbackPanel("feedbackPanel"));

		final IModel<List<User>> users = new LoadableDetachableModel<List<User>>() {

			@Override
			protected List<User> load() {
				return userRepo.getAll();
			}
		};

		add(new ListView<User>("allUsers", users) {

			@Override
			protected void populateItem(final ListItem<User> user) {
				user.add(new Label("fullname", user.getModelObject().getFullName()));
				user.add(new Label("username", user.getModelObject().getUsername()));
				AjaxLink<Void> deleteLink = new AjaxLink<Void>("deleteUser") {

					@Override
					public void onClick(AjaxRequestTarget target) {
						userRepo.deleteUser(user.getModelObject().getId());
						setResponsePage(AdminUsersPage.class);
					}
				};
				user.add(deleteLink);
			}
		});
	}
}