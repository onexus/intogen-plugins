package org.intogen.decorators.volcanoplot;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.onexus.collection.api.Field;
import org.onexus.collection.api.IEntity;
import org.onexus.resource.api.ParameterKey;
import org.onexus.website.api.widgets.tableviewer.decorators.utils.FieldDecorator;

import java.util.Map;

public class VolcanoplotDecorator extends FieldDecorator {

    public VolcanoplotDecorator(Field field, Map<ParameterKey, String> parameters) {
        super(field);

    }

    @Override
    public void populateCell(WebMarkupContainer cellContainer, String componentId, IModel<IEntity> data) {
        super.populateCell(cellContainer, componentId, data);

        IEntity entity = data.getObject();
        Double pvalue = (Double) entity.get("PVALUE");
        Double significane = (Double) entity.get("VOLCANOPLOT");

        if (pvalue < 0.02) {
            boolean red = false;
            if (significane > 1) {
                red = true;
            }

            cellContainer.add(new AttributeModifier("style", Model.of("color: #FFFFFF; background-color: " + (red?"#990000":"#009900"))));
        }


    }

}
