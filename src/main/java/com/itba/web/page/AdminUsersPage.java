package com.itba.web.page;

import java.util.List;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.itba.domain.EntityModel;
import com.itba.domain.model.EvaluatedResource;
import com.itba.domain.model.EvaluationSession;
import com.itba.domain.model.User;
import com.itba.domain.repository.UserRepo;
import com.itba.web.feedback.CustomFeedbackPanel;

@SuppressWarnings("serial")
@AuthorizeInstantiation({"ADMIN" })
public class AdminUsersPage extends BasePage {
	
	@SpringBean
	private UserRepo userRepo;

	private final IModel<User> userModel = new EntityModel<User>(User.class);

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
			protected void populateItem(ListItem<User> user) {
				user.add(new Label("fullname", user.getModelObject().getFullName()));				
				user.add(new Label("username", user.getModelObject().getUsername()));				

			}
			
		});
		 
	}	
}