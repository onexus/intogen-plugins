package org.intogen.decorators.impact;

import org.onexus.resource.api.ParameterKey;


public enum ImpactDecoratorParameters implements ParameterKey {

    SHOW_DETAILS("show-details", "Open a modal window with details", null, false),

    FIELD_IMPACT("field-impact", "Impact score field name", "SNV_IMPACT", true),
    FIELD_CHR("field-chr", "Chromosome field name", "CHROMOSOME", true),
    FIELD_POSITION("field-position", "Genomic position field name", "POSITION", true),
    FIELD_ALLELE("field-allele", "Allele field name", "ALLELE", true),
    FIELD_GENEID("field-geneid", "Gene identifier field name", "GENEID", true),

    COLLECTION_CT("collection-ct", "Consequence types collection", "data/snv_consequence-types", true),
    COLLECTION_GENES("collection-genes", "Genes collection", "data/genes_annotations", true);

    private final String key;
    private final String description;
    private final String defaultValue;
    private final boolean optional;

    private ImpactDecoratorParameters(String key, String description, String defaultValue, boolean optional) {
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


