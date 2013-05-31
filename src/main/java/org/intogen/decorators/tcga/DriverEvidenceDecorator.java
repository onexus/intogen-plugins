package org.intogen.decorators.tcga;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.model.IModel;
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

        Integer count = (Integer) data.getObject().get("COUNT");
        Integer oncodrive = (Integer) data.getObject().get("ONCODRIVE");
        Integer oncoclust = (Integer) data.getObject().get("ONCOCLUST");
        Integer activeDriver = (Integer) data.getObject().get("ACTIVE_DRIVER");
        Integer music = (Integer) data.getObject().get("MUSIC");
        Integer mutsig = (Integer) data.getObject().get("MUTSIG");

        if (count == null) {
            cellContainer.add(new EmptyPanel(componentId));
        } else {

            StringBuilder label = new StringBuilder();


            if (oncoclust != null && oncoclust.equals(1))
                label.append("<span class='badge badge-info' rel='tooltip' title='Gene with clustering bias detected by OncodriveCLUST'>C</span>&nbsp;");

            if (oncodrive != null && oncodrive.equals(1))
                label.append("<span class='badge badge-warning' rel='tooltip' title='Gene with functional impact bias detected by OncodriveFM'>F</span>&nbsp;");

            if (activeDriver != null && activeDriver.equals(1))
                label.append("<span class='badge badge-important' rel='tooltip' title='Gene with accumulation of mutations in active sites detected by ActiveDriver'>A</span>&nbsp;");

            if (music != null && music.equals(1))
                label.append("<span class='badge badge-success' rel='tooltip' title='Gene recurrently mutated detected by MuSiC'>R</span>&nbsp;");

            if (mutsig != null && mutsig.equals(1))
                label.append("<span class='badge badge-inverse' rel='tooltip' title='Gene recurrently mutated and with other evidences of selection detected by MutSig'>M</span>&nbsp;");

            cellContainer.add(new Label(componentId, label.toString()).setEscapeModelStrings(false));
        }
    }

}
