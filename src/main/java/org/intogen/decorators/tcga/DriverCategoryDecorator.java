package org.intogen.decorators.tcga;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.string.Strings;
import org.onexus.collection.api.Field;
import org.onexus.collection.api.IEntity;
import org.onexus.resource.api.ParameterKey;
import org.onexus.website.api.widgets.tableviewer.decorators.utils.FieldDecorator;

import java.util.Map;

public class DriverCategoryDecorator extends FieldDecorator {

    public DriverCategoryDecorator(Field field, Map<ParameterKey, String> parameters) {
        super(field);
    }

    @Override
    public void populateCell(WebMarkupContainer cellContainer, String componentId, IModel<IEntity> data) {

        String category = Strings.toString(data.getObject().get("DRIVER_CATEGORY"));

        if (Strings.isEmpty(category)) {
            cellContainer.add(new EmptyPanel(componentId));
        } else {

            String label;
            if (category.trim().equalsIgnoreCase("HCD")) {
                label = "<span class='label label-info' rel='tooltip' title='High confident novel drivers and known drivers with signs of positive selection'>High Conf Driver</span>";
            } else if (category.trim().equalsIgnoreCase("CD")) {
                label = "<span class='label label-success' rel='tooltip' title='Gene detected by only one method and not in the Cancer Gene Census'>Candidate Driver</span>";
            } else {
                label = "<span class='label'>" + category.trim() + "</span>";
            }

            cellContainer.add(new Label(componentId, label).setEscapeModelStrings(false));
        }

    }

}
