package org.intogen.decorators.impact;

import org.onexus.collection.api.Collection;
import org.onexus.collection.api.Field;
import org.onexus.resource.api.ParameterKey;
import org.onexus.resource.api.Parameters;
import org.onexus.website.widget.tableviewer.decorators.IDecorator;
import org.onexus.website.widget.tableviewer.decorators.IDecoratorCreator;

public class ImpactDecoratorCreator implements IDecoratorCreator {

    @Override
    public String getDecoratorId() {
        return "IMPACT";
    }

    @Override
    public ParameterKey[] getParameterKeys() {
        return ImpactDecoratorParameters.values();
    }

    @Override
    public IDecorator createDecorator(Collection collection, Field columnField, Parameters parameters) {
        return new ImpactDecorator(columnField, parameters);
    }


}
