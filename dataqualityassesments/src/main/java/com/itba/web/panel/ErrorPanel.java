package com.itba.web.panel;

import com.itba.domain.ErrorRepo;
import com.itba.domain.model.Error;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

@SuppressWarnings("serial")
public class ErrorPanel extends Panel {

	@SpringBean
	private ErrorRepo errorRepo;

	// este panel contiene todos los errores. Cuando se abre, se le setea el predicate y el objetc
	// tiene además un botón para cerrarlo y uno para confirmar la operación
	public ErrorPanel(String id) {
		super(id);

		List<Error> errorList = errorRepo.getAll();
		add(new ListView<Error>("resultItemList", errorList) {
			@Override
			protected void populateItem(ListItem<Error> listItem) {
				Error error = listItem.getModelObject();




//				listItem.add(new Label("predicate", resultItem.get(0)));
//				listItem.add(new Label("object", resultItem.get(1)));
//				listItem.add(new AjaxLink<Void>("showModalLink") {
//					@Override
//					public void onClick(AjaxRequestTarget target)
//					{
//						errorModalWindow.show(target);
//					}
//				});
			}
		});



		
		
		
	}

}
