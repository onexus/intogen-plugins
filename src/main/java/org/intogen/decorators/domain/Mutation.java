package org.intogen.decorators.domain;

import org.onexus.collection.api.ICollectionManager;
import org.onexus.collection.api.IEntity;
import org.onexus.collection.api.IEntityTable;
import org.onexus.collection.api.query.And;
import org.onexus.collection.api.query.Equal;
import org.onexus.collection.api.query.Query;
import org.onexus.resource.api.utils.ResourceUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Mutation implements Serializable {

    static String COLLECTION_CT = "data/snv_consequence-types";
    static String COLLECTION_GENES = "data/genes_annotations";

    static String FIELD_SNVID = "SNVID";
    static String FIELD_CHR = "CHROMOSOME";
    static String FIELD_POSITION = "POSITION";
    static String FIELD_ALLELE = "ALLELE";
    static String FIELD_GENEID = "GENEID";

    // Basic mutation fields
    private String collectionUri;
    private String ensembl;
    private String snv;

    // Extra mutation fields
    private String externalId;
    private String symbol;
    private Integer recurrence;
    private Integer samples;
    private Double frequency;

    // Mutation consequences
    private Set<Consequence> consequences;

    public Mutation(IEntity entity, ICollectionManager collectionManager) {
        super();

        // Load mutation values
        this.collectionUri = entity.getCollection().getURI();
        this.snv = String.valueOf(entity.get(FIELD_SNVID));
        this.ensembl = String.valueOf(entity.get(FIELD_GENEID));

        //TODO Load mutation extra query
        this.externalId = "";
        this.symbol = "";
        this.recurrence = 0;
        this.samples = 0;
        this.frequency = 0.0;

        // Load mutation consequences
        this.consequences = new HashSet<Consequence>();

        IEntityTable ctTable = collectionManager.load(getConsquencesQuery());
        while (ctTable.next()) {
            consequences.add(new Consequence(ctTable));
        }
    }



    private Query getExtraQuery() {

        String projectUri = ResourceUtils.getProjectURI(collectionUri);
        String collectionUri = ResourceUtils.getAbsoluteURI(projectUri, COLLECTION_GENES);

        Query query = new Query();
        query.setOn(projectUri);
        query.addDefine("g", COLLECTION_GENES);

        return query;
    }


    private Query getConsquencesQuery() {

        String projectUri = ResourceUtils.getProjectURI(collectionUri);

        String fromAlias = "c";
        Query query = new Query();
        query.setOn(projectUri);
        query.addDefine(fromAlias, COLLECTION_CT);
        query.setFrom(fromAlias);
        query.addSelect(fromAlias, null);
        query.setWhere(new And(new Equal(fromAlias, FIELD_CHR, getChromosome()),
                new And(new Equal(fromAlias, FIELD_POSITION, getPosition()),
                        new And(new Equal(fromAlias, FIELD_ALLELE, getAllele()),
                                new Equal(fromAlias, FIELD_GENEID, ensembl)
                                ))));

        return query;
    }

    public String getSnv() {
        return snv;
    }

    public String getChromosome() {
        int firstColon = snv.indexOf(':');
        return snv.substring(0, firstColon);
    }

    public String getPosition() {
        int firstColon = snv.indexOf(':');
        int lastColon = snv.lastIndexOf(':');
        return snv.substring(firstColon+1, lastColon);
    }

    public String getAllele() {
        int lastColon = snv.lastIndexOf(':');
        return snv.substring(lastColon+1);
    }

    public Set<Consequence> getConsequences() {
        return consequences;
    }

    public String getEnsembl() {
        return ensembl;
    }

    public String getExternalId() {
        return externalId;
    }

    public Double getFrequency() {
        return frequency;
    }

    public Integer getRecurrence() {
        return recurrence;
    }

    public Integer getSamples() {
        return samples;
    }

    public String getSymbol() {
        return symbol;
    }
}
