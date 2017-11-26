package com.itba.web.page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.google.common.base.Optional;
import com.itba.domain.EntityModel;
import com.itba.domain.model.EvaluatedResource;
import com.itba.domain.model.EvaluatedResourceDetail;
import com.itba.domain.model.EvaluationSession;
import com.itba.domain.repository.EvaluatedResourceRepo;
import com.itba.domain.repository.EvaluationSessionRepo;
import com.itba.formulae.ManualErrorsFormulae;
import com.itba.web.WicketSession;

import lib.StringUtils;

@SuppressWarnings("serial")
public class ErrorsTablePanel extends Panel {

	@SpringBean
	private ManualErrorsFormulae manualErrorsFormulae;
	@SpringBean
	private EvaluatedResourceRepo evaluatedResourceRepo;
	@SpringBean
	private EvaluationSessionRepo sessionRepo;

	private final IModel<EvaluatedResource> resourceModel = new EntityModel<EvaluatedResource>(EvaluatedResource.class);

	public ErrorsTablePanel(String id, final String resource, int resourceSessionId) {
		super(id);
		Optional<EvaluationSession> resourceSession = Optional.absent();
		if (resourceSessionId >= 0) {
			resourceSession = Optional.of(sessionRepo.get(resourceSessionId));			
		}
		resourceModel.setObject(evaluatedResourceRepo.getResourceForSession(
				resourceSession.isPresent() ? resourceSession.get() : WicketSession.get().getEvaluationSession().get(),
				resource).orNull());

		final LoadableDetachableModel<Map<String, Double>> scoreImpactModel = new LoadableDetachableModel<Map<String, Double>>() {
			@Override
			protected Map<String, Double> load() {
				Map<String, Double> scoreImpact = new HashMap<>();
				try {
					if (resourceModel.getObject() != null) {
						for (EvaluatedResourceDetail detail : resourceModel.getObject().getDetails()) {
							String key = detail.getError().getName();
							if (!scoreImpact.containsKey(key))
								scoreImpact.put(key, new Double(0));
							scoreImpact.put(key, scoreImpact.get(key)
									+ manualErrorsFormulae.computeIndividual(resource, detail.getError().getId()));
						}
					}
				} catch (Exception e) {
					// TODO: reset & log somehow...
					scoreImpact.clear();
				}
				return scoreImpact;
			}
		};

		ListView<String> detailsList = new ListView<String>("detailsList",
				new ArrayList<String>(scoreImpactModel.getObject().keySet())) {
			@Override
			protected void populateItem(ListItem<String> listItem) {
				Label errorNameLabel = new Label("errorNameLabel", listItem.getModelObject());
				Label errorScoreLabel = new Label("errorScoreLabel",
						"-" + StringUtils.formatDouble(scoreImpactModel.getObject().get(listItem.getModelObject()), 3));
				listItem.add(errorNameLabel);
				listItem.add(errorScoreLabel);
			}
		};
		add(detailsList);
		setVisible(!scoreImpactModel.getObject().isEmpty());
	}
}
