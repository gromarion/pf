package com.itba.web.panel;

import java.util.List;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.string.Strings;

import com.itba.domain.EntityModel;
import com.itba.domain.model.Campaign;

@SuppressWarnings("serial")
public class CampaignDropDownChoice extends DropDownChoice<IModel<Campaign>> {

	private IModel<Campaign> campaignModel = new EntityModel<>(Campaign.class);
	
	public CampaignDropDownChoice(String id, List<? extends IModel<Campaign>> choices, IModel<Campaign> selected) {
		super(id, choices, new CampaignRender());
		setDefaultModel(campaignModel);
		campaignModel.setObject(selected.getObject());
	}
	
	@Override
	public String getModelValue() {
		final Campaign object = (Campaign) getModelObject();
		if (object != null) {
			IModel<Campaign> selected = new EntityModel<Campaign>(Campaign.class, object);
			int index = getChoices().indexOf(selected);
			return getChoiceRenderer().getIdValue(selected, index);
		}
		return "";
	}
	
	private static class CampaignRender implements IChoiceRenderer<IModel<Campaign>> {

		@Override
		public Object getDisplayValue(IModel<Campaign> object) {
			return object.getObject() == null ? "" : object.getObject().getName();
		}

		@Override
		public String getIdValue(IModel<Campaign> object, int index) {
			return Strings.toString(index);
		}

		@Override
		public IModel<Campaign> getObject(String id,
				IModel<? extends List<? extends IModel<Campaign>>> choices) {
			// TODO Auto-generated method stub
			return null;
		}
	}
}
