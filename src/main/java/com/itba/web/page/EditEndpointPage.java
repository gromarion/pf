package com.itba.web.page;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.itba.domain.EntityModel;
import com.itba.domain.model.Campaign;
import com.itba.domain.model.User;
import com.itba.domain.repository.CampaignRepo;

@SuppressWarnings("serial")
@AuthorizeInstantiation({User.ADMIN_ROLE})
public class EditEndpointPage extends BasePage {

	@SpringBean
    private CampaignRepo campaigns;
	
	private final IModel<Campaign> campaignModel = new EntityModel<Campaign>(Campaign.class);
	
	public EditEndpointPage(PageParameters parameters) {
		campaignModel.setObject(campaigns.get(parameters.get("campaignId").toInt()));

		
		Form<Campaign> form = new Form<Campaign>("editEndpointForm",
				new CompoundPropertyModel<Campaign>(new EntityModel<Campaign>(Campaign.class)));
		
		final TextField<String> name = new TextField<String>("name", Model.of(campaignModel.getObject().getName()));
		final TextField<String> endpoint = new TextField<String>("endpoint", Model.of(campaignModel.getObject().getEndpoint()));
		final TextField<String> graph = new TextField<String>("graph", Model.of(campaignModel.getObject().getGraphs()));
		final TextArea<String> params = new TextArea<String>("params", Model.of(campaignModel.getObject().getParams()));
		final TextArea<String> description = new TextArea<String>("description", Model.of(campaignModel.getObject().getDescription()));
		
		form.add(name);
		form.add(endpoint);
		form.add(graph);
		form.add(params);
		form.add(description);
		form.add(new Button("submit", new ResourceModel("submit")) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit() {
				campaignModel.getObject().setName(name.getModelObject());
				campaignModel.getObject().setEndpoint(endpoint.getModelObject());
				campaignModel.getObject().setGraphs(graph.getModelObject());
				campaignModel.getObject().setParams(params.getModelObject());
				campaignModel.getObject().setDescription(description.getModelObject());
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
	
	@Override
	protected void onDetach() {
		super.onDetach();
		campaignModel.detach();
	}
}
