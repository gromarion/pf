package com.itba.web.page;

import com.itba.domain.EntityModel;
import com.itba.domain.ErrorRepo;
import com.itba.domain.model.Error;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

@SuppressWarnings("serial")
public class ErrorSelectionPage extends BasePage {

    @SpringBean
    private ErrorRepo errorRepo;

    private final IModel<Error> errorModel = new EntityModel<Error>(Error.class);

    public ErrorSelectionPage(PageParameters parameters) {

        String predicate = parameters.get("predicate").toString();
        String object = parameters.get("object").toString();
        String resource = parameters.get("resource").toString();



//        errorModel.setObject(errorRepo.get(parameters.get("errorId").toInteger()));

        // agregar un select bullet para los errores, y un textarea para el comentario
        // rescatar de la base de datos el resource

        List<Error> errorList = errorRepo.getAll();
        add(new ListView<Error>("errorList", errorList) {
            @Override
            protected void populateItem(ListItem<Error> listItem) {
                Error error = listItem.getModelObject();
                listItem.add(new Label("title", error.getTitle()));
            }
        });

    }





    @Override
    protected void onDetach() {
        super.onDetach();
        errorModel.detach();
    }
}
