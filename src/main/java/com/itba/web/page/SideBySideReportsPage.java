package com.itba.web.page;

import java.io.IOException;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.google.common.base.Strings;
import com.itba.domain.EntityModel;
import com.itba.domain.model.Campaign;
import com.itba.domain.repository.CampaignRepo;
import com.itba.domain.repository.EvaluatedResourceRepo;
import com.itba.formulae.EndpointQualityFormulae;
import com.itba.formulae.EndpointQualityFormulae.EndpointScore;
import com.itba.web.WicketSession;

@SuppressWarnings("serial")
public class SideBySideReportsPage extends BasePage {

	@SpringBean
	private EvaluatedResourceRepo evaluatedResourceRepo;
	@SpringBean
	private EndpointQualityFormulae endpointQualityFormulae;
	@SpringBean
	private CampaignRepo campaigns;
	private IModel<Campaign> selectedCampaignAModel = new EntityModel<Campaign>(Campaign.class);
	private IModel<Campaign> selectedCampaignBModel = new EntityModel<Campaign>(Campaign.class);

	public SideBySideReportsPage(PageParameters parameters) {
		Campaign campaignA = getCampaign(parameters, "campaignIdA");
		Campaign campaignB = getCampaign(parameters, "campaignIdB");
		
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
		
		OnChangeAjaxBehavior campaignADropDownBehavior = new OnChangeAjaxBehavior() {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				PageParameters params = new PageParameters();

				params.set("campaignIdA", selectedCampaignAModel.getObject().getId());
				params.set("campaignIdB", parameters.get("campaignIdB"));
				setResponsePage(SideBySideReportsPage.class, params);
			}
		};
		campaignADropDownChoice.add(campaignADropDownBehavior);
		
		OnChangeAjaxBehavior campaignBDropDownBehavior = new OnChangeAjaxBehavior() {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				PageParameters params = new PageParameters();

				params.set("campaignIdA", parameters.get("campaignIdA"));
				params.set("campaignIdB", selectedCampaignBModel.getObject().getId());
				setResponsePage(SideBySideReportsPage.class, params);
			}
		};
		campaignBDropDownChoice.add(campaignBDropDownBehavior);

		add(campaignADropDownChoice);
		add(campaignBDropDownChoice);

		try {
			EndpointScore endpointAScore = endpointQualityFormulae.getScore(campaignA);
			EndpointScore endpointBScore = endpointQualityFormulae.getScore(campaignB);
			final EndpointScorePanel endpointAScorePanel = new EndpointScorePanel("endpointAScorePanel", endpointAScore,
					evaluatedResourceRepo, 1);
			final EndpointScorePanel endpointBScorePanel = new EndpointScorePanel("endpointBScorePanel", endpointBScore,
					evaluatedResourceRepo, 2);
			add(endpointAScorePanel);
			add(endpointBScorePanel);
			add(new Label("endpointAName", campaignA.getName()));
			add(new Label("endpointBName", campaignB.getName()));
		} catch (IOException e) {
			setResponsePage(ErrorPage.class);
		}
	}

	private Campaign getCampaign(PageParameters parameters, String campaignParam) {
		return Strings.isNullOrEmpty(parameters.get(campaignParam).toString())
				? WicketSession.get().getEvaluationSession().get().getCampaign()
				: campaigns.get(parameters.get(campaignParam).toInt());
	}
}
