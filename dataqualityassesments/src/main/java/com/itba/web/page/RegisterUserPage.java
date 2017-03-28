package com.itba.web.page;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.itba.domain.UserRepo;
import com.itba.domain.model.User;
import com.itba.web.feedback.CustomFeedbackPanel;

public class RegisterUserPage extends WebPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
    private UserRepo users;

    private String name;
    private String password;
    private String confirmedPassword;

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(new CustomFeedbackPanel("feedbackPanel"));

        Form<RegisterUserPage> form = new Form<RegisterUserPage>("registerUserForm",
                new CompoundPropertyModel<RegisterUserPage>(this)) {
			private static final long serialVersionUID = 1L;

			@Override
            protected void onSubmit() {
                User user = users.getByName(name);
                if (user != null) {
                	error(getString("userAlreadyExist"));
                } else if (!password.equals(confirmedPassword)) {
                	error(getString("passwordMismatch"));
                } else {
                	user = new User(name, password);
                	users.save(user);
                	setResponsePage(LoginPage.class);
                }
            }
        };

        form.add(new TextField<String>("name").setRequired(true));
        form.add(new PasswordTextField("password").setRequired(true));
        form.add(new PasswordTextField("confirmedPassword").setRequired(true));
        form.add(new Button("register"));
        add(form);
        add(new BookmarkablePageLink<LoginPage>("loginLink", LoginPage.class));
    }
}
