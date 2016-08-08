package com.itba.web.page;

import java.util.List;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.itba.domain.CampaignRepo;
import com.itba.domain.EvaluationSessionRepo;
import com.itba.domain.UserRepo;
import com.itba.domain.model.Campaign;
import com.itba.domain.model.EvaluationSession;
import com.itba.domain.model.User;
import com.itba.web.WicketSession;
import com.itba.web.feedback.CustomFeedbackPanel;

public class LoginPage extends WebPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
    private UserRepo users;

    @SpringBean
    private CampaignRepo campaigns;

    @SpringBean
    private EvaluationSessionRepo evaluationSessions;

    // TODO: manejar esto con modelos para evitar los errores de serialización
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
			private static final long serialVersionUID = 1L;

			@Override
            protected void onSubmit() {
                User user = users.getByName(name);
                if (selectedCampaign == null) {
                    error(getString("campaignNotFoundError"));
                } else if (user != null && user.checkPassword(password)) {
                    WicketSession appSession = WicketSession.get();
//                    DateTime nowTime = DateTime.now();
//                    DateTime validTo = nowTime.minusHours(1); // TODO: la sesión dura 1 hora. Puede modificarse
                    Optional<EvaluationSession> session = evaluationSessions.getForCampaignAndUser(selectedCampaign, user);
                    if (!session.isPresent()) {
                        EvaluationSession newSession = new EvaluationSession(selectedCampaign, user);
                        evaluationSessions.save(newSession);
                        session = Optional.of(newSession);
                    }
                    appSession.signIn(session.get());
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
        add(new BookmarkablePageLink<RegisterUserPage>("registerLink", RegisterUserPage.class));
    }
}
