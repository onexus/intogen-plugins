package org.intogen.decorators.xrefs;

import org.onexus.collection.api.Collection;
import org.onexus.collection.api.Field;
import org.onexus.resource.api.ParameterKey;
import org.onexus.resource.api.Parameters;
import org.onexus.website.widget.tableviewer.decorators.IDecorator;
import org.onexus.website.widget.tableviewer.decorators.IDecoratorCreator;

public class XRefsDecoratorCreator implements IDecoratorCreator {

    @Override
    public String getDecoratorId() {
        return "XREFS";
    }

    @Override
    public ParameterKey[] getParameterKeys() {
        return XRefsParameters.values();
    }

    @Override
    public IDecorator createDecorator(Collection collection, Field columnField, Parameters parameters) {
        return new XRefsDecorator(columnField, parameters);
    }


}
