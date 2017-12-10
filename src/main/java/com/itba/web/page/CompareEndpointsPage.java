package com.itba.web.page;

import java.util.List;

import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.itba.domain.EntityModel;
import com.itba.domain.model.Campaign;
import com.itba.domain.repository.CampaignRepo;

@SuppressWarnings("serial")
public class CompareEndpointsPage extends BasePage {
	
	@SpringBean
	private CampaignRepo campaigns;
	private IModel<Campaign> selectedCampaignAModel = new EntityModel<Campaign>(Campaign.class);
	private IModel<Campaign> selectedCampaignBModel = new EntityModel<Campaign>(Campaign.class);

	public CompareEndpointsPage(PageParameters parameters) {
		Form<CompareEndpointsPage> form = new Form<CompareEndpointsPage>("compareEndpointsForm",
				new CompoundPropertyModel<CompareEndpointsPage>(this)) {

			@Override
			protected void onSubmit() {
				PageParameters params = new PageParameters();
				params.set("campaignIdA", selectedCampaignAModel.getObject().getId());
				params.set("campaignIdB", selectedCampaignBModel.getObject().getId());

				setResponsePage(SideBySideReportsPage.class, params);
			}
		};
		
		DropDownChoice<Campaign> campaignADropDownChoice = new DropDownChoice<Campaign>("campaignA",
				selectedCampaignAModel, new LoadableDetachableModel<List<Campaign>>() {
					@Override
					protected List<Campaign> load() {
						return campaigns.getAll();
					}
				}, new ChoiceRenderer<Campaign>("name"));
		
		DropDownChoice<Campaign> campaignBDropDownChoice = new DropDownChoice<Campaign>("campaignB",
				selectedCampaignBModel, new LoadableDetachableModel<List<Campaign>>() {
					@Override
					protected List<Campaign> load() {
						return campaigns.getAll();
					}
				}, new ChoiceRenderer<Campaign>("name"));
		
		form.add(campaignADropDownChoice);
		form.add(campaignBDropDownChoice);
		
		selectedCampaignAModel.setObject(campaigns.getAll().get(0));
		selectedCampaignBModel.setObject(campaigns.getAll().get(0));
		
		add(form);
	}
}
