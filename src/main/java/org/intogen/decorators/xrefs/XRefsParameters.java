package org.intogen.decorators.xrefs;

import org.onexus.resource.api.ParameterKey;


public enum XRefsParameters implements ParameterKey {

    INTOGEN_URL("intogen", "IntOGen url", "/web/mutations/v04/browser/mutations", true),
    MUTATIONS("mutations", "Mutations collection to filter", "", true),
    GENES("genes", "Genes collection to filter", "", false);

    private final String key;
    private final String description;
    private final String defaultValue;
    private final boolean optional;

    private XRefsParameters(String key, String description, String defaultValue, boolean optional) {
        this.key = key;
        this.description = description;
        this.defaultValue = defaultValue;
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
    public String getDefault() {
        return defaultValue;
    }

    @Override
    public boolean isOptional() {
        return optional;
    }




}


