package com.itba.web.page;

import com.google.common.collect.Lists;
import com.itba.domain.CampaignRepo;
import com.itba.domain.EvaluationSessionRepo;
import com.itba.domain.UserRepo;
import com.itba.domain.model.Campaign;
import com.itba.domain.model.User;
import com.itba.web.WicketSession;
import com.itba.web.feedback.CustomFeedbackPanel;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class LoginPage extends WebPage {

    @SpringBean
    private UserRepo users;

    @SpringBean
    private CampaignRepo campaigns;

    @SpringBean
    private EvaluationSessionRepo evaluationSessions;

    private List<Campaign> availableCampaigns = Lists.newLinkedList();

    private String name;
    private String password;
    private Campaign selectedCampaign;

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(new CustomFeedbackPanel("feedbackPanel"));
        availableCampaigns = campaigns.getAll();
        if (!availableCampaigns.isEmpty()) selectedCampaign = availableCampaigns.get(0);
        Form<LoginPage> form = new Form<LoginPage>("loginForm",
                new CompoundPropertyModel<LoginPage>(this)) {

            @Override
            protected void onSubmit() {
                User user = users.getByName(name);
                if (selectedCampaign == null) {
                    error(getString("campaignNotFoundError"));
                } else if (user != null && user.checkPassword(password)) {
                    WicketSession session = WicketSession.get();
                    // TODO: instanciar el nuevo EvaluationSession, y que ese sea el objeto que se guarda en la sesión!!

                    if (session.signIn(user.getName(), user.getPassword(), selectedCampaign, users)){

                        setResponsePage(HomePage.class);

                    } else {
                        error(getString("unknownSignInError"));
                    }
                } else {
                    error(getString("invalidCredentials"));
                }
            }
        };

        DropDownChoice<Campaign> listCampaigns = new DropDownChoice<Campaign>(
                "campaigns", new PropertyModel<Campaign>(this, "selectedCampaign"), availableCampaigns, new ChoiceRenderer<Campaign>("name"));
        form.add(listCampaigns);
        form.add(new TextField<String>("name").setRequired(true));
        form.add(new PasswordTextField("password").setRequired(true));
        form.add(new Button("login"));
        add(form);
    }
}
