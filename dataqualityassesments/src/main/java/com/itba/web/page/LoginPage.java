package com.itba.web.page;

import java.util.List;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.google.common.collect.Lists;
import com.itba.domain.CampaignRepo;
import com.itba.domain.EntityModel;
import com.itba.domain.EvaluationSessionRepo;
import com.itba.domain.UserRepo;
import com.itba.domain.model.Campaign;
import com.itba.web.WicketSession;
import com.itba.web.feedback.CustomFeedbackPanel;
import com.itba.web.panel.CampaignDropDownChoice;

@SuppressWarnings("serial")
public class LoginPage extends WebPage {
	@SpringBean
    private UserRepo users;

    @SpringBean
    private CampaignRepo campaigns;

    @SpringBean
    private EvaluationSessionRepo evaluationSessions;

    private List<IModel<Campaign>> availableCampaigns = Lists.newLinkedList();

    private String name;
    private String password;
    private IModel<Campaign> selectedCampaignModel = new EntityModel<Campaign>(Campaign.class);

    @Override
    protected void onInitialize() {
        super.onInitialize();
        if (((WicketSession) getSession()).isSignedIn()) {
        	setResponsePage(AutomaticOrManualPage.class);
		}
        add(new CustomFeedbackPanel("feedbackPanel"));
        for (Campaign c : campaigns.getAll()) {
        	availableCampaigns.add(new EntityModel<Campaign>(Campaign.class, c));
        }
        
        if (!availableCampaigns.isEmpty()) selectedCampaignModel.setObject(availableCampaigns.get(0).getObject());
		Form<LoginPage> form = new Form<LoginPage>("loginForm",
                new CompoundPropertyModel<LoginPage>(this)) {

			@Override
            protected void onSubmit() {
                WicketSession appSession = (WicketSession) getSession();
                if (selectedCampaignModel.getObject() == null) {
                    error(getString("campaignNotFoundError"));
                } else if (appSession.signIn(name, password, selectedCampaignModel.getObject(), users, evaluationSessions)) {
                    setResponsePage(AutomaticOrManualPage.class);
                } else {
                    error(getString("invalidCredentials"));
                }
            }
        };
        
        CampaignDropDownChoice campaignDropDownChoice = new CampaignDropDownChoice("campaigns", availableCampaigns, selectedCampaignModel);
//        DropDownChoice<Campaign> listCampaigns = new DropDownChoice<Campaign>(
//                "campaigns", selectedCampaignModel, availableCampaigns, new ChoiceRenderer<Campaign>("name"));
        form.add(campaignDropDownChoice);
        form.add(new TextField<String>("name").setRequired(true));
        form.add(new PasswordTextField("password").setRequired(true));
        form.add(new Button("login"));
        add(form);
        add(new BookmarkablePageLink<RegisterUserPage>("registerLink", RegisterUserPage.class));
    }
}
