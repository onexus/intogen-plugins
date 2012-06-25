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

        Object value = getValue(data.getObject());

        String label = "";

        if (value != null) {
            int val = (Integer) value;
            switch (val) {
                case 4:
                    label = "<span class=\"label label-important\">High</span>";
                    break;
                case 3:
                    label = "<span class=\"label label-warning\">Medium</span>";
                    break;
                case 2:
                    label = "<span class=\"label label-info\">Low</span>";
                    break;
                default:
                    label = "<span class=\"label label-success\">None</span>";
            }
        }

        cellContainer.add(new Label(componentId, label).setEscapeModelStrings(false));
        cellContainer.add(new AttributeModifier("class", Model.of("st")));

    }

}
