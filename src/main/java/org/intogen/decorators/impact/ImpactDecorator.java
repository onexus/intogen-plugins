package org.intogen.decorators.impact;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.onexus.collection.api.Collection;
import org.onexus.collection.api.Field;
import org.onexus.collection.api.IEntity;
import org.onexus.resource.api.Parameters;
import org.onexus.website.api.widgets.tableviewer.decorators.utils.FieldDecorator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Override
    public List<String> getExtraFields(Collection collection) {

        String fieldChr = parameters.get(ImpactDecoratorParameters.FIELD_CHR);
        String fieldPosition = parameters.get(ImpactDecoratorParameters.FIELD_POSITION);
        String fieldAllele = parameters.get(ImpactDecoratorParameters.FIELD_ALLELE);
        String fieldGeneid = parameters.get(ImpactDecoratorParameters.FIELD_GENEID);

        List<String> fields = new ArrayList<String>();

        if (fieldChr!=null && collection.getField(fieldChr)!=null) {
            fields.add(fieldChr);
        }

        if (fieldPosition!=null && collection.getField(fieldPosition)!=null) {
            fields.add(fieldPosition);
        }

        if (fieldAllele!=null && collection.getField(fieldAllele)!=null) {
            fields.add(fieldAllele);
        }

        if (fieldGeneid!=null && collection.getField(fieldGeneid)!=null) {
            fields.add(fieldGeneid);
        }

        return fields;
    }
}
