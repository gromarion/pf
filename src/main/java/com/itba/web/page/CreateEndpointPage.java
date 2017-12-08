package com.itba.web.page;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.itba.domain.EntityModel;
import com.itba.domain.model.Campaign;
import com.itba.domain.repository.CampaignRepo;

@SuppressWarnings("serial")
public class CreateEndpointPage extends BasePage {

	@SpringBean
    private CampaignRepo campaigns;
	
	@Override
	protected void onInitialize() {
		super.onInitialize();

		Form<Campaign> form = new Form<Campaign>("createEndpointForm",
				new CompoundPropertyModel<Campaign>(new EntityModel<Campaign>(Campaign.class)));
		
		final TextField<String> name = new TextField<String>("name", Model.of(""));
		final TextField<String> endpoint = new TextField<String>("endpoint", Model.of(""));
		final TextField<String> graph = new TextField<String>("graph", Model.of(""));
		final TextArea<String> params = new TextArea<String>("params", Model.of(""));
		final TextArea<String> description = new TextArea<String>("description", Model.of(""));
		
		form.add(name);
		form.add(endpoint);
		form.add(graph);
		form.add(params);
		form.add(description);
		form.add(new Button("submit", new ResourceModel("submit")) {
			@Override
			public void onSubmit() {
				Campaign newCampaign = new Campaign(name.getValue(), endpoint.getValue(), graph.getValue(), params.getValue(), description.getValue());
				campaigns.save(newCampaign);
				setResponsePage(AdminEndpointsPage.class);
			}
		});
		
		Link<Void> backButton = new Link<Void>("back") {
			@Override
			public void onClick() {
				setResponsePage(AdminEndpointsPage.class);
			}
		};
		form.add(backButton);
		
		add(form);
	}
}
