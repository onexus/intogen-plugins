package org.intogen.decorators.impact.domain;

import org.intogen.decorators.impact.ImpactDecoratorParameters;
import org.onexus.collection.api.ICollectionManager;
import org.onexus.collection.api.IEntity;
import org.onexus.collection.api.IEntityTable;
import org.onexus.collection.api.query.And;
import org.onexus.collection.api.query.Equal;
import org.onexus.collection.api.query.Query;
import org.onexus.resource.api.ORI;
import org.onexus.resource.api.Parameters;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


public class Mutation implements Serializable {

    // Basic mutation fields
    private ORI collectionUri;
    private ORI genesCollection;
    private ORI ctCollection;
    private ORI project;
    private String ensembl;
    private String chromosome;
    private Long position;
    private String allele;

    // Extra mutation fields
    private String externalId;
    private String symbol;
    private Integer recurrence;
    private Integer samples;
    private Double frequency;

    // Mutation consequences
    private Set<Consequence> consequences;

    public Mutation(IEntity entity, ICollectionManager collectionManager, Parameters parameters) {
        super();

        // Parameters
        String fieldChr = parameters.get(ImpactDecoratorParameters.FIELD_CHR);
        String fieldPosition = parameters.get(ImpactDecoratorParameters.FIELD_POSITION);
        String fieldAllele = parameters.get(ImpactDecoratorParameters.FIELD_ALLELE);
        String fieldGeneid = parameters.get(ImpactDecoratorParameters.FIELD_GENEID);

        // Load mutation values
        this.collectionUri = entity.getCollection().getORI();
        this.chromosome = String.valueOf(entity.get( fieldChr ));
        this.position = Long.valueOf( String.valueOf(entity.get( fieldPosition )));
        this.allele = String.valueOf( entity.get(fieldAllele));
        this.ensembl = String.valueOf( entity.get(fieldGeneid));

        project = new ORI(collectionUri.getProjectUrl(), null);
        genesCollection = new ORI(collectionUri.getProjectUrl(), parameters.get(ImpactDecoratorParameters.COLLECTION_GENES));
        ctCollection = new ORI(collectionUri.getProjectUrl(), parameters.get(ImpactDecoratorParameters.COLLECTION_CT));

        //TODO Load mutation extra query
        this.externalId = "";
        this.symbol = "";
        this.recurrence = 0;
        this.samples = 0;
        this.frequency = 0.0;

        // Load mutation consequences
        this.consequences = new HashSet<Consequence>();

        String fromAlias = "c";
        Query query = new Query();
        query.setOn(project);
        query.addDefine(fromAlias, ctCollection);
        query.setFrom(fromAlias);
        query.addSelect(fromAlias, null);
        query.setWhere(new And(new Equal(fromAlias, fieldChr, getChromosome()),
                new And(new Equal(fromAlias, fieldPosition, getPosition()),
                        new And(new Equal(fromAlias, fieldAllele, getAllele()),
                                new Equal(fromAlias, fieldGeneid, ensembl)
                        ))));

        IEntityTable ctTable = collectionManager.load(query);
        while (ctTable.next()) {
            consequences.add(new Consequence(ctCollection, ctTable));
        }
    }



    private Query getExtraQuery() {

        Query query = new Query();
        query.setOn(project);
        query.addDefine("g", genesCollection);

        return query;
    }

    public String getSnv() {
        return chromosome + ":" + position + ":" + allele;
    }

    public String getChromosome() {
        return chromosome;
    }

    public Long getPosition() {
        return position;
    }

    public String getAllele() {
        return allele;
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
