package org.intogen.decorators.volcanoplot;

import org.onexus.collection.api.Collection;
import org.onexus.collection.api.Field;
import org.onexus.resource.api.ParameterKey;
import org.onexus.resource.api.Parameters;
import org.onexus.website.api.widgets.tableviewer.decorators.IDecorator;
import org.onexus.website.api.widgets.tableviewer.decorators.IDecoratorCreator;

public class VolcanoplotDecoratorCreator implements IDecoratorCreator {

    @Override
    public String getDecoratorId() {
        return "VOLCANOPLOT";
    }

    @Override
    public ParameterKey[] getParameterKeys() {
        return new ParameterKey[0];
    }

    @Override
    public IDecorator createDecorator(Collection collection, Field columnField, Parameters parameters) {
        return new VolcanoplotDecorator(columnField, parameters);
    }


}
