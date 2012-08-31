package org.intogen.decorators;

import org.onexus.collection.api.Collection;
import org.onexus.collection.api.Field;
import org.onexus.resource.api.ParameterKey;
import org.onexus.ui.website.widgets.tableviewer.decorators.IDecorator;
import org.onexus.ui.website.widgets.tableviewer.decorators.IDecoratorCreator;

import java.util.Map;

public class ImpactDecoratorCreator implements IDecoratorCreator {

    @Override
    public String getDecoratorId() {
        return "IMPACT";
    }

    @Override
    public ParameterKey[] getParameterKeys() {
        return new ParameterKey[0];
    }

    @Override
    public IDecorator createDecorator(Collection collection, Field columnField, Map<ParameterKey, String> parameters) {
        return new ImpactDecorator(columnField);
    }


}
