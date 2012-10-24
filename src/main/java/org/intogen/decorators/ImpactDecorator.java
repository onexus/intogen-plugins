package org.intogen.decorators;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.onexus.collection.api.Field;
import org.onexus.collection.api.IEntity;
import org.onexus.resource.api.ParameterKey;
import org.onexus.ui.website.widgets.tableviewer.decorators.utils.FieldDecorator;

import java.util.Map;

public class ImpactDecorator extends FieldDecorator {

    private boolean showDetails = true;

    public ImpactDecorator(Field field, Map<ParameterKey, String> parameters) {
        super(field);

        if (parameters.containsKey(ImpactDecoratorParameters.SHOW_DETAILS)) {
            showDetails = Boolean.valueOf(parameters.get(ImpactDecoratorParameters.SHOW_DETAILS));
        }
    }

    @Override
    public void populateCell(WebMarkupContainer cellContainer, String componentId, IModel<IEntity> data) {
        cellContainer.add(new ImpactButton(componentId, data, showDetails));
        cellContainer.add(new AttributeModifier("class", Model.of("st")));
    }

}
