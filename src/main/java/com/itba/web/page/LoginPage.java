package com.itba.web.page;

import java.util.List;
import java.util.Random;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.google.common.base.Strings;
import com.itba.domain.EntityModel;
import com.itba.domain.model.Campaign;
import com.itba.domain.model.User;
import com.itba.domain.repository.CampaignRepo;
import com.itba.domain.repository.EvaluationSessionRepo;
import com.itba.domain.repository.UserRepo;
import com.itba.web.WicketSession;
import com.itba.web.feedback.CustomFeedbackPanel;

@SuppressWarnings("serial")
public class LoginPage extends AnonimousPage {
	@SpringBean
	private UserRepo users;

	@SpringBean
	private CampaignRepo campaigns;

	@SpringBean
	private EvaluationSessionRepo evaluationSessions;

	private String username;
	private String password;
	private IModel<Campaign> selectedCampaignModel = new EntityModel<Campaign>(Campaign.class);
	private final IModel<String> campaignDescriptionModel = Model.of();

	@Override
	protected void onInitialize() {
		super.onInitialize();
		if (((WicketSession) getSession()).isSignedIn()) {
			setResponsePage(HomePage.class);
		}
		add(new CustomFeedbackPanel("feedbackPanel"));

		selectedCampaignModel.setObject(campaigns.getAll().get(0));
		String campaignDescription = Strings.nullToEmpty(selectedCampaignModel.getObject().getDescription()).isEmpty() ? 
										"La campaña aún no cuenta con una descripción..." : selectedCampaignModel.getObject().getDescription();
		campaignDescriptionModel.setObject(campaignDescription);
		Form<LoginPage> form = new Form<LoginPage>("loginForm", new CompoundPropertyModel<LoginPage>(this)) {

			@Override
			protected void onSubmit() {
				WicketSession appSession = (WicketSession) getSession();
				if (selectedCampaignModel.getObject() == null) {
					error(getString("campaignNotFoundError"));
				} else if (appSession.signIn(username, password, selectedCampaignModel.getObject(), users,
						evaluationSessions)) {
					setResponsePage(HomePage.class);
				} else {
					error(getString("invalidCredentials"));
				}
			}
		};

        final Label campaignDescriptionLabel = new Label("campaignDescription", campaignDescription);
        add(campaignDescriptionLabel);
        campaignDescriptionLabel.setOutputMarkupId(true);
		
		DropDownChoice<Campaign> campaignDropDownChoice = new DropDownChoice<Campaign>("campaigns",
				selectedCampaignModel, new LoadableDetachableModel<List<Campaign>>() {
					@Override
					protected List<Campaign> load() {
						return campaigns.getAll();
					}
				}, new ChoiceRenderer<Campaign>("name"));
		
		
		
        OnChangeAjaxBehavior onChangeAjaxBehavior = new OnChangeAjaxBehavior() {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
        		String campaignDescription = Strings.nullToEmpty(selectedCampaignModel.getObject().getDescription()).isEmpty() ? 
						"La campaña aún no cuenta con una descripción..." : selectedCampaignModel.getObject().getDescription();
            	campaignDescriptionLabel.setDefaultModelObject(campaignDescription);
            	target.add(campaignDescriptionLabel);
            }
        };
        
        campaignDropDownChoice.add(onChangeAjaxBehavior);
		form.add(campaignDropDownChoice);
		form.add(new TextField<String>("username").setRequired(true));
		form.add(new PasswordTextField("password").setRequired(true));
		form.add(new Button("login"));
		add(form);

		Form<LoginPage> guestForm = new Form<LoginPage>("guestForm", new CompoundPropertyModel<LoginPage>(this)) {

			@Override
			protected void onSubmit() {
				WicketSession appSession = (WicketSession) getSession();
				Random r = new Random();
				User user = new User("guest", "guest" + r.nextInt(), "guest", "GUEST");
				users.save(user);
				if (selectedCampaignModel.getObject() == null) {
					error(getString("campaignNotFoundError"));
				} else if (appSession.signIn(user.getUsername(), user.getPassword(), selectedCampaignModel.getObject(),
						users, evaluationSessions)) {
					setResponsePage(HomePage.class);
				} else {
					error(getString("invalidCredentials"));
				}
			}
		};

		guestForm.add(new Button("guestLogin"));
		add(guestForm);

		add(new BookmarkablePageLink<RegisterUserPage>("registerLink", RegisterUserPage.class));
	}
}
