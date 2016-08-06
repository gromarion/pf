package com.itba.web.page;

import com.itba.domain.UserRepo;
import com.itba.domain.model.User;
import com.itba.web.WicketSession;
import com.itba.web.feedback.CustomFeedbackPanel;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class LoginPage extends WebPage {

    @SpringBean
    private UserRepo users;

    private String name;
    private String password;

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(new CustomFeedbackPanel("feedbackPanel"));
        Form<LoginPage> form = new Form<LoginPage>("loginForm",
                new CompoundPropertyModel<LoginPage>(this)) {

            @Override
            protected void onSubmit() {
                User user = users.getByName(name);
                if (user != null && user.checkPassword(password)) {
                    WicketSession session = WicketSession.get();
                    session.signIn(user.getName(), user.getPassword(), users);
                    setResponsePage(HomePage.class);
                } else {
                    error(getString("invalidCredentials"));
                }
            }
        };

        form.add(new TextField<String>("name").setRequired(true));
        form.add(new PasswordTextField("password").setRequired(true));
        form.add(new Button("login"/*, new ResourceModel("login")*/));
        add(form);
    }
}
