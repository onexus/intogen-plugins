package org.intogen.decorators.tcga;

import org.onexus.collection.api.Collection;
import org.onexus.collection.api.Field;
import org.onexus.resource.api.ParameterKey;
import org.onexus.resource.api.Parameters;
import org.onexus.website.api.widgets.tableviewer.decorators.IDecorator;
import org.onexus.website.api.widgets.tableviewer.decorators.IDecoratorCreator;

public class DriverCategoryDecoratorCreator implements IDecoratorCreator {

    @Override
    public String getDecoratorId() {
        return "DRIVER_CATEGORY";
    }

    @Override
    public ParameterKey[] getParameterKeys() {
        return new ParameterKey[0];
    }

    @Override
    public IDecorator createDecorator(Collection collection, Field columnField, Parameters parameters) {
        return new DriverCategoryDecorator(columnField, parameters);
    }


}
