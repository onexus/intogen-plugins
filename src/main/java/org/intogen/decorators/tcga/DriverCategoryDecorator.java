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
                label = "<span class='label label-info' rel='tooltip' title='High-Confident Drivers. Contains 291 cancer driver genes detected by combining the results of complementary methods that find signals of positive selection in the pattern of somatic mutations observed in 3205 tumors from TCGA Pan-Cancer project.'>HCD</span>";
            } else if (category.trim().equalsIgnoreCase("CD")) {
                label = "<span class='label label-success' rel='tooltip' title='Candidate Drivers. Contains 144 extra genes, forming a more comprehensive list of drivers but with an expectedly higher false-positives rate'>CD</span>";
            } else {
                label = "<span class='label'>" + category.trim() + "</span>";
            }

            cellContainer.add(new Label(componentId, label).setEscapeModelStrings(false));
        }

    }

}
