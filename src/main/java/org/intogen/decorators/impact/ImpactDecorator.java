package org.intogen.decorators.impact;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.onexus.collection.api.Field;
import org.onexus.collection.api.IEntity;
import org.onexus.resource.api.Parameters;
import org.onexus.website.api.widgets.tableviewer.decorators.utils.FieldDecorator;

public class ImpactDecorator extends FieldDecorator {

    private Parameters parameters;

    public ImpactDecorator(Field field, Parameters parameters) {
        super(field);

        this.parameters = parameters;
    }

    @Override
    public void populateCell(WebMarkupContainer cellContainer, String componentId, IModel<IEntity> data) {
        cellContainer.add(new ImpactButton(componentId, data, parameters));
        cellContainer.add(new AttributeModifier("class", Model.of("st")));
    }

}
