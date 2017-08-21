package com.itba.web.page;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.itba.domain.EntityModel;
import com.itba.domain.model.Campaign;
import com.itba.domain.repository.CampaignRepo;

@SuppressWarnings("serial")
public class CreateEndpointPage extends WebPage {

	@SpringBean
    private CampaignRepo campaigns;
	
	@Override
	protected void onInitialize() {
		super.onInitialize();

		Form<Campaign> form              = new Form<Campaign>("createEndpointForm",
				new CompoundPropertyModel<Campaign>(new EntityModel<Campaign>(Campaign.class)));
		
		final TextField<String> name     = new TextField<String>("name", Model.of(""));
		final TextField<String> endpoint = new TextField<String>("endpoint", Model.of(""));
		
		form.add(name);
		form.add(endpoint);
		form.add(new Button("submit", new ResourceModel("submit")) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit() {
				Campaign newCampaign = new Campaign(name.getValue(), endpoint.getValue());
				campaigns.save(newCampaign);
				setResponsePage(AutomaticOrManualPage.class);
			}
		});
		
		Link<Void> backButton = new Link<Void>("back") {
			@Override
			public void onClick() {
				setResponsePage(LoginPage.class);
			}
		};
		form.add(backButton);
		
		add(form);
	}
}