package org.intogen.decorators.impact;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.onexus.collection.api.IEntity;
import org.onexus.resource.api.Parameters;
import org.onexus.website.api.utils.EntityModel;

public class ImpactButton extends Panel {

    public ImpactButton(String id, IModel<IEntity> entityModel, final Parameters parameters) {
        super(id, new EntityModel(entityModel.getObject()));

        IEntity entity = entityModel.getObject();
        Object value = entity.get(parameters.get(ImpactDecoratorParameters.FIELD_IMPACT));

        final WebMarkupContainer widgetModal = new WebMarkupContainer("widgetModal");
        widgetModal.setOutputMarkupId(true);
        widgetModal.add(new EmptyPanel("widget"));
        add(widgetModal);

        Label button = new Label("button", impactToLabel(value));
        button.add(new AttributeModifier("class", impactToLabelClass(value)));

        if (parameters.containsKey(ImpactDecoratorParameters.SHOW_DETAILS)) {
            button.add(new AjaxEventBehavior("onclick") {
                @Override
                protected void onEvent(AjaxRequestTarget target) {
                    widgetModal.addOrReplace(new CtPanel("widget", getEntity(), parameters));
                    target.add(widgetModal);
                    target.appendJavaScript("$('#" + widgetModal.getMarkupId() + "').modal('show')");
                }
            });
        }

        add(button);


    }

    private IEntity getEntity() {
        return (IEntity) getDefaultModelObject();
    }

    public static String impactToLabel(Object value) {

        if (value == null || !(value instanceof Integer)) {
            return "NA";
        }

        int val = (Integer) value;
        if (val == 1) {
            return "High";
        }

        if (val == 2) {
            return "Medium";
        }

        if (val == 3) {
            return "Low";
        }

        if (val == 4) {
            return "Unknown";
        }

        return "None";
    }

    public static String impactToLabelClass(Object value) {

        if (value == null || !(value instanceof Integer)) {
            return "label";
        }

        int val = (Integer) value;
        if (val == 1) {
            return "label label-important";
        }

        if (val == 2) {
            return "label label-warning";
        }

        if (val == 3) {
            return "label label-info";
        }

        if (val == 4) {
            return "label";
        }

        return "label label-success";

    }
}
