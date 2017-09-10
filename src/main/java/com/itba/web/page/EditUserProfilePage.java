package com.itba.web.page;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.model.CompoundPropertyModel;

import com.itba.domain.EntityModel;
import com.itba.domain.model.User;

@SuppressWarnings("serial")
@AuthorizeInstantiation({"EVALUATOR", "ADMIN"})
public class EditUserProfilePage extends BasePage {

	public EditUserProfilePage(User user) {
		Form<User> form = new Form<User>("editUserForm",
				new CompoundPropertyModel<User>(new EntityModel<User>(User.class, user))) {

			@Override
			protected void onSubmit() {
				setResponsePage(HomePage.class);
			}
		};
		
		PasswordTextField passwordField = new PasswordTextField("password");
		passwordField.setRequired(true);
		
		PasswordTextField confirmedPasswordField = new PasswordTextField("confirmedPassword");
		confirmedPasswordField.setRequired(true);

		form.add(new TextField<String>("fullName").setRequired(true));
		form.add(passwordField);
		form.add(confirmedPasswordField);
		form.add(new Button("submit"));
		form.add(new EqualPasswordInputValidator(passwordField, confirmedPasswordField));
		add(form);
	}
}
