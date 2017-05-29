package com.itba.web.page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import com.google.common.base.Optional;
import com.itba.domain.EntityModel;
import com.itba.domain.SparqlRequestHandler;
import com.itba.domain.model.Campaign;
import com.itba.domain.model.EvaluatedResource;
import com.itba.domain.model.EvaluatedResourceDetail;
import com.itba.domain.model.EvaluationSession;
import com.itba.domain.repository.CampaignRepo;
import com.itba.domain.repository.EvaluatedResourceRepo;
import com.itba.sparql.JsonSparqlResult;
import com.itba.sparql.ResultItem;
import com.itba.web.WicketSession;

import lib.ManualErrorsFormulae;

@SuppressWarnings("serial")
public class ResultItemPage extends BasePage {
	@SpringBean
	CampaignRepo campaignRepo;
	@SpringBean
	private EvaluatedResourceRepo evaluatedResourceRepo;
	
	private static final int FACTOR = 1000000;

    public ResultItemPage(PageParameters parameters) {
        final String resource = parameters.get("selection").toString();
        final Campaign campaign = campaignRepo.get(Campaign.class, WicketSession.get().getEvaluationSession().get().getCampaign().getId());
        List<List<ResultItem>> results = new JsonSparqlResult(SparqlRequestHandler.requestResource(resource, campaign).toString()).data;
        Form<Void> form = new Form<>("form");
        final TextArea<String> comments = new TextArea<String>("comments", Model.of(""));
        comments.setOutputMarkupId(true);
        Button submit = new Button("submit") {
          @Override
          public void onSubmit() {
            super.onSubmit();
          }
        };
		
        form.add(comments);
        form.add(submit);
        add(new ResourceSearchPanel("search"));
        add(form);
        String resourceName = resource.substring(resource.lastIndexOf('/') + 1);
        add(new Label("resourceName", resourceName.replace('_', ' ')));
        
        // Add resource score label
        add(new Label("resourceScore", resourceScore(resource, results)));
        // TODO: agregar acá la lista de errores ya ingresados
        
        add(new ListView<List<ResultItem>>("resultItemList", results) {
            @Override
            protected void populateItem(ListItem<List<ResultItem>> listItem) {
                final List<ResultItem> resultItem = listItem.getModelObject();
                String predicateURL = resultItem.get(0).value;

                listItem.add(new ExternalLink("predicate", predicateURL, predicateURL));
                listItem.add(new Label("object", resultItem.get(1)));
                listItem.add(new AjaxLink<Void>("errorPageLink") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        PageParameters parameters = new PageParameters();
                        parameters.add("predicate", resultItem.get(0));
                        parameters.add("object", resultItem.get(1));
                        parameters.add("resource", resource);
                        setResponsePage(ErrorSelectionPage.class, parameters);
                    }
                });
            }
        });
  }
    
    private String resourceScore(String resource, List<List<ResultItem>> properties) {
    	IModel<EvaluationSession> currentSession = new EntityModel<EvaluationSession>(EvaluationSession.class);
    	currentSession.setObject(WicketSession.get().getEvaluationSession().get());
    	Optional<EvaluatedResource> evaluatedResource = evaluatedResourceRepo.getResourceForSession(currentSession.getObject(), resource);

        Map<Integer, Double> errors = new HashMap<Integer, Double>();
        double ans = 0;
    	
        if (!evaluatedResource.isPresent()) {
        	return "-";
        }
        
        if (evaluatedResource.get().isCorrect()) {
        	return FACTOR * properties.size() + "";
        }
        
        for (EvaluatedResourceDetail detail : evaluatedResource.get().getDetails()) {
        	int errorId = detail.getError().getId();

        	errors.put(errorId, new ManualErrorsFormulae(campaignRepo, evaluatedResourceRepo).compute(resource, errorId));
        }
        for (Integer errorId : errors.keySet()) {
        	ans += getWeightForError(errorId) * errors.get(errorId);
        }
        
        return Math.round((1 - ans) * FACTOR / properties.size()) + "";
    }
    
    private double getWeightForError(int errorId) {
    	// TODO: Implementar entidad que almacena los pesos asociados a cada error.
    	return 0.25;
    }
}
