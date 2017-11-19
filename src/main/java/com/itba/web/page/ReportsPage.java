package com.itba.web.page;

import java.io.IOException;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.itba.domain.EntityModel;
import com.itba.domain.model.Campaign;
import com.itba.domain.model.User;
import com.itba.domain.repository.CampaignRepo;
import com.itba.domain.repository.EvaluatedResourceRepo;
import com.itba.formulae.EndpointQualityFormulae;
import com.itba.formulae.EndpointQualityFormulae.EndpointScore;

@SuppressWarnings("serial")
@AuthorizeInstantiation({User.EVALUATOR_ROLE, User.ADMIN_ROLE})
public class ReportsPage extends BasePage {
	
	@SpringBean
	private EndpointQualityFormulae endpointQualityFormulae;
	@SpringBean
	private EvaluatedResourceRepo evaluatedResourceRepo;
	@SpringBean
	private CampaignRepo campaigns;
	
	private IModel<Campaign> selectedCampaignModel = new EntityModel<Campaign>(Campaign.class);
	
	@Override
	protected void onInitialize() {
		super.onInitialize();
        selectedCampaignModel.setObject(campaigns.getAll().get(0));
		DropDownChoice<Campaign> campaignDropDownChoice = new DropDownChoice<Campaign>("campaigns",
				selectedCampaignModel, new LoadableDetachableModel<List<Campaign>>() {
					@Override
					protected List<Campaign> load() {
						return campaigns.getAll();
					}
				}, new ChoiceRenderer<Campaign>("name"));
		
		try {
			EndpointScore endpointScore = endpointQualityFormulae.getScore(selectedCampaignModel.getObject());
			final EndpointScorePanel endpointScorePanel = new EndpointScorePanel("endpointScorePanel", endpointScore, evaluatedResourceRepo);
			endpointScorePanel.setVisible(!endpointScore.getEndpointStats().isEmpty());
			add(campaignDropDownChoice);
			add(endpointScorePanel.setOutputMarkupId(true));
	        OnChangeAjaxBehavior onChangeAjaxBehavior = new OnChangeAjaxBehavior() {
	            @Override
	            protected void onUpdate(AjaxRequestTarget target) {
	            	try {
						endpointScorePanel.setEndpointScore(endpointQualityFormulae.getScore(selectedCampaignModel.getObject()));
					} catch (IOException e) {
						e.printStackTrace();
					}
	            	target.add(endpointScorePanel);
	            }
	        };
	        campaignDropDownChoice.add(onChangeAjaxBehavior);
		} catch (IOException e) {
			setResponsePage(ErrorPage.class);
			e.printStackTrace();
		}
	}
}
