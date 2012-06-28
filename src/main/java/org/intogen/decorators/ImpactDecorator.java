package org.intogen.decorators;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.onexus.core.IEntity;
import org.onexus.core.resources.Field;
import org.onexus.ui.website.widgets.tableviewer.decorators.FieldDecorator;

public class ImpactDecorator extends FieldDecorator {

    public ImpactDecorator(Field field) {
        super(field);
    }

    @Override
    public void populateCell(WebMarkupContainer cellContainer, String componentId, IModel<IEntity> data) {
        cellContainer.add(new ImpactButton(componentId, data));
        cellContainer.add(new AttributeModifier("class", Model.of("st")));
    }

}
