package com.itba.web.page;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import com.itba.domain.SparqlRequestHandler;
import com.itba.domain.model.Campaign;
import com.itba.domain.model.Error;
import com.itba.domain.repository.CampaignRepo;
import com.itba.domain.repository.ErrorRepo;
import com.itba.domain.repository.EvaluatedResourceDetailRepo;
import com.itba.domain.repository.EvaluatedResourceRepo;
import com.itba.sparql.JsonSparqlResult;
import com.itba.sparql.ResultItem;
import com.itba.web.WicketSession;
import com.itba.web.feedback.CustomFeedbackPanel;

import lib.ManualErrorsFormulae;

@SuppressWarnings("serial")
public class ResultItemPage extends BasePage {
	
	@SpringBean
	private CampaignRepo campaignRepo;
	@SpringBean
	private EvaluatedResourceRepo evaluatedResourceRepo;
	@SpringBean
	private ErrorRepo errorRepo;
	@SpringBean
	private EvaluatedResourceDetailRepo evaluatedResourceDetailRepo;

    public ResultItemPage(PageParameters parameters) {
        final String resource = parameters.get("selection").toString();
        final Campaign campaign = campaignRepo.get(Campaign.class, WicketSession.get().getEvaluationSession().get().getCampaign().getId());
        List<List<ResultItem>> results = new JsonSparqlResult(SparqlRequestHandler.requestResource(resource, campaign).toString()).data;
        final CustomFeedbackPanel customFeedbackPanel = new CustomFeedbackPanel("feedbackPanel");
        customFeedbackPanel.setOutputMarkupId(true);
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
        add(new Label("resourceScore", new ManualErrorsFormulae(campaignRepo, evaluatedResourceRepo).stringCompute(resource)));
        add(customFeedbackPanel);

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
                    	Long totalErrors = errorRepo.count(Error.class);
                    	List<Error> prevErrors = evaluatedResourceDetailRepo.getPreviousErrors(resource, resultItem.get(0).toString(), resultItem.get(1).toString());
                    	if (totalErrors == prevErrors.size()) {
                    		error(getString("allErrorsRegisteredMessage"));
                    		target.add(customFeedbackPanel);
                    	} else {
                    		PageParameters parameters = new PageParameters();
                            parameters.add("predicate", resultItem.get(0));
                            parameters.add("object", resultItem.get(1));
                            parameters.add("resource", resource);
                            setResponsePage(ErrorSelectionPage.class, parameters);
                    	}
                    }
                });
            }
        });
  }
}
