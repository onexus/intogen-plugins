package org.intogen.decorators;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.onexus.collection.api.IEntity;
import org.onexus.ui.website.utils.EntityModel;

public class ImpactButton extends Panel {

    private static String FIELD_IMPACT = "SNV_IMPACT";

    public ImpactButton(String id, IModel<IEntity> entityModel) {
        super(id, new EntityModel(entityModel.getObject()));

        IEntity entity = entityModel.getObject();
        Object value = entity.get(FIELD_IMPACT);

        String label = "NA";
        String labelClass = "label";
        if (value != null) {
            int val = (Integer) value;
            switch (val) {
                case 1:
                    label = "High";
                    labelClass = "label label-important";
                    break;
                case 2:
                    label = "Medium";
                    labelClass = "label label-warning";
                    break;
                case 3:
                    label = "Low";
                    labelClass = "label label-info";
                    break;
                default:
                    label = "None";
                    labelClass = "label label-success";
            }
        }

        final WebMarkupContainer widgetModal = new WebMarkupContainer("widgetModal");
        widgetModal.setOutputMarkupId(true);
        widgetModal.add(new EmptyPanel("widget"));
        add(widgetModal);

        Label button = new Label("button", label);
        button.add(new AttributeModifier("class", labelClass));

        button.add( new AjaxEventBehavior("onclick") {
            @Override
            protected void onEvent(AjaxRequestTarget target) {
                widgetModal.addOrReplace(new CtPanel("widget", getEntity()));
                target.add(widgetModal);
                target.appendJavaScript("$('#" + widgetModal.getMarkupId() + "').modal('show')");
            }
        });

        add(button);


    }

    private IEntity getEntity() {
        return (IEntity) getDefaultModelObject();
    }

}
