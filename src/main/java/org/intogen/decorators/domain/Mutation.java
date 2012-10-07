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

    static String COLLECTION_CT = "data/snv_project_consequence-types";
    static String COLLECTION_GENES = "data/genes_annotations";

    static String FIELD_CHR = "CHROMOSOME";
    static String FIELD_POSITION = "POSITION";
    static String FIELD_ALLELE = "ALLELE";
    static String FIELD_GENEID = "GENEID";
    static String FIELD_PROJECTID = "PROJECTID";

    // Basic mutation fields
    private String collectionUri;
    private String ensembl;
    private String chromosome;
    private Integer position;
    private String allele;
    private String projectId;

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
        this.chromosome = String.valueOf(entity.get(FIELD_CHR));
        this.position = (Integer) entity.get(FIELD_POSITION);
        this.allele = String.valueOf(entity.get(FIELD_ALLELE));
        this.ensembl = String.valueOf(entity.get(FIELD_GENEID));
        this.projectId = String.valueOf(entity.get(FIELD_PROJECTID));

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
        query.setWhere(new And(new Equal(fromAlias, FIELD_CHR, chromosome),
                new And(new Equal(fromAlias, FIELD_POSITION, position),
                        new And(new Equal(fromAlias, FIELD_ALLELE, allele),
                                new And(new Equal(fromAlias, FIELD_GENEID, ensembl),
                                        new Equal(fromAlias, FIELD_PROJECTID, projectId)
                                )))));

        return query;
    }

    public String getAllele() {
        return allele;
    }

    public String getChromosome() {
        return chromosome;
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

    public Integer getPosition() {
        return position;
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
