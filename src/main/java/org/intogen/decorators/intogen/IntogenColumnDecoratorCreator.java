package org.intogen.decorators.intogen;

import org.onexus.collection.api.Collection;
import org.onexus.collection.api.Field;
import org.onexus.resource.api.ParameterKey;
import org.onexus.resource.api.Parameters;
import org.onexus.website.widget.tableviewer.decorators.IDecorator;
import org.onexus.website.widget.tableviewer.decorators.IDecoratorCreator;

public class IntogenColumnDecoratorCreator implements IDecoratorCreator {

    @Override
    public String getDecoratorId() {
        return "INTOGEN";
    }

    @Override
    public ParameterKey[] getParameterKeys() {
        return IntogenColumnParameters.values();
    }

    @Override
    public IDecorator createDecorator(Collection collection, Field columnField, Parameters parameters) {
        return new IntogenColumnDecorator(columnField, parameters);
    }


}
