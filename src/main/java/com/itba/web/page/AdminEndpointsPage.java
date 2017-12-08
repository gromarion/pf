package com.itba.web.page;

import java.util.List;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.itba.domain.model.Campaign;
import com.itba.domain.model.User;
import com.itba.domain.repository.CampaignRepo;
import com.itba.web.WicketSession;

@SuppressWarnings("serial")
@AuthorizeInstantiation({User.ADMIN_ROLE})
public class AdminEndpointsPage extends BasePage {

	@SpringBean
    private CampaignRepo campaigns;
	
	private final LoadableDetachableModel<List<Campaign>> campaignsModel = new LoadableDetachableModel<List<Campaign>>() {
		@Override
		protected List<Campaign> load() {
			return campaigns.getAll();
		}
	};
	
	public AdminEndpointsPage() {
		ListView<Campaign> campaignsList = new ListView<Campaign>("campaignsList", campaignsModel) {
			
			@Override
			protected void populateItem(final ListItem<Campaign> item) {
				Label nameLabel = new Label("nameLabel", item.getModelObject().getName());
				Label endpointLabel = new Label("endpointLabel", item.getModelObject().getEndpoint());
				Label graphLabel = new Label("graphLabel", item.getModelObject().getGraphs());
				Link<Void> deleteButton = new Link<Void>("deleteButton") {
					@Override
					public void onClick() {
						campaigns.delete(item.getModelObject());
						setResponsePage(AdminEndpointsPage.class);
					}
				};
				Link<Void> editButton = new Link<Void>("editButton") {
					@Override
					public void onClick() {
						PageParameters parameters = new PageParameters();
						parameters.add("campaignId", item.getModelObject().getId());
						setResponsePage(EditEndpointPage.class, parameters);
					}
				};
				
				item.add(nameLabel);
				item.add(endpointLabel);
				item.add(graphLabel);
				item.add(editButton);
				item.add(deleteButton.setVisible(
						item.getModelObject().getId() != WicketSession.get().getEvaluationSession().get().getCampaign().getId()
						&& !item.getModelObject().hasEvaluations()));
			}
		};
		
		add(campaignsList);
		add(new BookmarkablePageLink<CreateEndpointPage>("createEndpointLink", CreateEndpointPage.class));
	}
	
	@Override
	protected void onDetach() {
		super.onDetach();
		campaignsModel.detach();
	}
}
