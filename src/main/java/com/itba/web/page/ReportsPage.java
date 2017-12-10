package com.itba.web.page;

import java.io.IOException;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.google.common.base.Strings;
import com.itba.domain.EntityModel;
import com.itba.domain.model.Campaign;
import com.itba.domain.repository.CampaignRepo;
import com.itba.domain.repository.EndpointStatsRepo;
import com.itba.domain.repository.EvaluatedResourceRepo;
import com.itba.formulae.EndpointQualityFormulae;
import com.itba.formulae.EndpointQualityFormulae.EndpointScore;
import com.itba.web.WicketSession;

@SuppressWarnings("serial")
public class ReportsPage extends BasePage {

	@SpringBean
	private EndpointStatsRepo endpointStatsRepo;
	@SpringBean
	private EndpointQualityFormulae endpointQualityFormulae;
	@SpringBean
	private EvaluatedResourceRepo evaluatedResourceRepo;
	@SpringBean
	private CampaignRepo campaigns;
	private IModel<Campaign> selectedCampaignModel = new EntityModel<Campaign>(Campaign.class);

	private static final String CAMPAIGN_ID_PARAM = "campaignId";

	public ReportsPage(PageParameters parameters) {
		Campaign campaign = Strings.isNullOrEmpty(parameters.get(CAMPAIGN_ID_PARAM).toString())
				? WicketSession.get().getEvaluationSession().get().getCampaign()
				: campaigns.get(parameters.get(CAMPAIGN_ID_PARAM).toInt());
		selectedCampaignModel.setObject(campaign);
		EndpointScore endpointScoreUpdate;
		try {
			endpointScoreUpdate = endpointQualityFormulae.getScore(selectedCampaignModel.getObject());
			DropDownChoice<Campaign> campaignDropDownChoice = new DropDownChoice<Campaign>("campaigns",
					selectedCampaignModel, new LoadableDetachableModel<List<Campaign>>() {
						@Override
						protected List<Campaign> load() {
							return campaigns.getAll();
						}
					}, new ChoiceRenderer<Campaign>("name"));
			
			Link<Void> compareToSomeOtherEndpoint = new Link<Void>("compareToSomeOtherEndpoint") {
				@Override
				public void onClick() {
					PageParameters params = new PageParameters();
					params.set("campaignId", campaign.getId());

					setResponsePage(CompareEndpointsPage.class, params);
				}
			};
			
			add(compareToSomeOtherEndpoint);

			EndpointScore endpointScore = endpointQualityFormulae.getScore(selectedCampaignModel.getObject());
			final EndpointScorePanel endpointScorePanel = new EndpointScorePanel("endpointScorePanel", endpointScore,
					evaluatedResourceRepo, 1);
			final WebMarkupContainer notFound = new WebMarkupContainer("notFound");
			notFound.setVisible(campaign.getSessions().isEmpty());
			add(notFound);
			add(campaignDropDownChoice);
			add(endpointScorePanel);

			boolean anyReports = !endpointScoreUpdate.getEndpointStats().isEmpty();
			notFound.setVisible(!anyReports);
			endpointScorePanel.setEndpointScore(endpointScoreUpdate);

			OnChangeAjaxBehavior onChangeAjaxBehavior = new OnChangeAjaxBehavior() {
				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					PageParameters params = new PageParameters();
					params.set(CAMPAIGN_ID_PARAM, selectedCampaignModel.getObject().getId());

					setResponsePage(ReportsPage.class, params);
				}
			};
			campaignDropDownChoice.add(onChangeAjaxBehavior);
		} catch (IOException e) {
			e.printStackTrace();
			setResponsePage(ErrorPage.class);
		}
	}
}
