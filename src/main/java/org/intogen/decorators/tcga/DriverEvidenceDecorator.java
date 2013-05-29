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

public class DriverEvidenceDecorator extends FieldDecorator {

    public DriverEvidenceDecorator(Field field, Map<ParameterKey, String> parameters) {
        super(field);

    }

    @Override
    public void populateCell(WebMarkupContainer cellContainer, String componentId, IModel<IEntity> data) {

        String evidence = Strings.toString(data.getObject().get("DRIVER_EVIDENCE"));

        if (Strings.isEmpty(evidence)) {
            cellContainer.add(new EmptyPanel(componentId));
        } else {

            StringBuilder label = new StringBuilder();

            for (char c : evidence.toUpperCase().toCharArray()) {

                switch (c) {
                    case 'C':
                        label.append("<span class='badge badge-info' rel='tooltip' title='Gene with clustering bias detected by OncodriveCLUST'>C</span>&nbsp;");
                        break;
                    case 'F':
                        label.append("<span class='badge badge-warning' rel='tooltip' title='Gene with functional impact bias detected by OncodriveFM'>F</span>&nbsp;");
                        break;
                    case 'A':
                        label.append("<span class='badge badge-important' rel='tooltip' title='Gene with accumulation of mutations in active sites detected by ActiveDriver'>A</span>&nbsp;");
                        break;
                    case 'R':
                        label.append("<span class='badge badge-success' rel='tooltip' title='Gene recurrently mutated detected by MuSiC'>R</span>&nbsp;");
                        break;
                    case 'M':
                        label.append("<span class='badge badge-inverse' rel='tooltip' title='Gene recurrently mutated and with other evidences of selection detected by MutSig'>M</span>&nbsp;");
                        break;
                }

            }

            cellContainer.add(new Label(componentId, label.toString()).setEscapeModelStrings(false));
        }
    }

}
