package org.intogen.decorators;

import org.onexus.resource.api.ParameterKey;


public enum ImpactDecoratorParameters implements ParameterKey {

    SHOW_DETAILS("show-details", "Open a modal window with details", false);

    private final String key;
    private final String description;
    private final boolean optional;

    private ImpactDecoratorParameters(String key, String description, boolean optional) {
        this.key = key;
        this.description = description;
        this.optional = optional;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public boolean isOptional() {
        return optional;
    }


}


