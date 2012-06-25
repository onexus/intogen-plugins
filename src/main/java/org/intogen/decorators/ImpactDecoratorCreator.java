package org.intogen.decorators;

import org.onexus.core.resources.Collection;
import org.onexus.core.resources.Field;
import org.onexus.ui.website.widgets.tableviewer.decorators.IDecorator;
import org.onexus.ui.website.widgets.tableviewer.decorators.IDecoratorCreator;

public class ImpactDecoratorCreator implements IDecoratorCreator {
    @Override
    public String getDecoratorId() {
        return "IMPACT";
    }

    @Override
    public IDecorator createDecorator(Collection collection, Field columnField, String[] parameters) {
        return new ImpactDecorator(columnField);
    }
}
