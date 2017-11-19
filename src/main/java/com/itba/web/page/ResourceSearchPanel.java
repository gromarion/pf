package com.itba.web.page;

import java.io.IOException;

import org.apache.wicket.ajax.json.JSONException;
import org.apache.wicket.ajax.json.JSONObject;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.itba.domain.SparqlRequestHandler;
import com.itba.domain.model.Campaign;
import com.itba.domain.model.User;
import com.itba.domain.repository.CampaignRepo;
import com.itba.domain.repository.EndpointStatsRepo;
import com.itba.web.WicketSession;

@SuppressWarnings("serial")
public class ResourceSearchPanel extends Panel {
	@SpringBean
	private CampaignRepo campaignRepo;
	@SpringBean
	private EndpointStatsRepo endpointStatsRepo;

	public ResourceSearchPanel(String id) {
		super(id);
		setVisible(!WicketSession.get().getUser().hasRole(User.GUEST_ROLE));
		Form<Void> searchForm = new Form<>("search-form");
		final TextField<String> searchTextField = new TextField<String>("textField", Model.of(""));
		searchTextField.setOutputMarkupId(true);

		Button submit = new Button("submit") {
			@Override
			public void onSubmit() {
				super.onSubmit();
				PageParameters parameters = new PageParameters();
				parameters.add("search", searchTextField.getValue());
				setResponsePage(SearchResultPage.class, parameters);
			}
		};

		searchForm.add(searchTextField);
		searchForm.add(submit);
		searchForm.add(new Link<Void>("fetchRandom") {
			@Override
			public void onClick() {
				final Campaign campaign = campaignRepo.get(Campaign.class,
						WicketSession.get().getEvaluationSession().get().getCampaign().getId());
				PageParameters parameters = new PageParameters();
				JSONObject ans;
				try {
					ans = SparqlRequestHandler.requestRandomResource(campaign, endpointStatsRepo);
					String resourceURL = (String) ((JSONObject) ans.get("s")).get("value");
					parameters.add("selection", resourceURL);

					setResponsePage(ResultItemPage.class, parameters);
				} catch (JSONException | IOException e) {
					setResponsePage(ErrorPage.class);
				}
			}
		});
		add(searchForm);
	}
}
