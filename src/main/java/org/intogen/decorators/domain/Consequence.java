package org.intogen.decorators.domain;

import org.onexus.collection.api.IEntity;
import org.onexus.collection.api.IEntityTable;

import java.io.Serializable;

public class Consequence implements Serializable {

    private String transcript;
    private String uniprot;
    private String consequenceType;
    private String protein;
    private String aachange;
    private String proteinPosition;

    private String siftClass;
    private Double siftScore;
    private Double siftTrans;

    private String pph2Class;
    private Double pph2Score;
    private Double pph2Trans;

    private String maClass;
    private Double maScore;
    private Double maTrans;

    public Consequence(IEntityTable table) {
        super();

        IEntity ct = table.getEntity(Mutation.COLLECTION_CT);

        this.transcript = String.valueOf(ct.get("TRANSCRIPTID"));
        this.uniprot = String.valueOf(ct.get("UNIPROTID"));
        this.consequenceType = String.valueOf(ct.get("CT"));
        this.protein = String.valueOf(ct.get("PROTEINID"));

        Object protein_pos = ct.get("PROTEIN_POSITION");

        this.aachange = String.valueOf(ct.get("AA_CHANGE"));

        if (protein_pos != null) {
            this.proteinPosition = String.valueOf(protein_pos);
        } else {
            this.proteinPosition = "-";
        }

        // Sift
        this.siftClass = String.valueOf(ct.get("SIFT_TRANSFIC_CLASS"));
        this.siftScore = (Double) ct.get("SIFT");
        this.siftTrans = (Double) ct.get("SIFT_TRANSFIC");

        // Pph2
        this.pph2Class = String.valueOf(ct.get("PPH2_TRANSFIC_CLASS"));
        this.pph2Score = (Double) ct.get("PPH2");
        this.pph2Trans = (Double) ct.get("PPH2_TRANSFIC");

        // MA
        this.maClass = String.valueOf(ct.get("MA_TRANSFIC_CLASS"));
        this.maScore = (Double) ct.get("MA");
        this.maTrans = (Double) ct.get("MA_TRANSFIC");

    }

    public String getAachange() {
        return aachange;
    }

    public String getProteinPosition() {
        return proteinPosition;
    }

    public String getConsequenceType() {
        return consequenceType;
    }

    public String getPph2Class() {
        return pph2Class;
    }

    public Double getPph2Score() {
        return pph2Score;
    }

    public Double getPph2Trans() {
        return pph2Trans;
    }

    public String getProtein() {
        return protein;
    }

    public String getSiftClass() {
        return siftClass;
    }

    public Double getSiftScore() {
        return siftScore;
    }

    public Double getSiftTrans() {
        return siftTrans;
    }

    public String getMaClass() {
        return maClass;
    }

    public Double getMaScore() {
        return maScore;
    }

    public Double getMaTrans() {
        return maTrans;
    }

    public String getTranscript() {
        return transcript;
    }

    public String getUniprot() {
        return uniprot;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Consequence)) return false;

        Consequence that = (Consequence) o;

        if (consequenceType != null ? !consequenceType.equals(that.consequenceType) : that.consequenceType != null)
            return false;
        if (transcript != null ? !transcript.equals(that.transcript) : that.transcript != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = transcript != null ? transcript.hashCode() : 0;
        result = 31 * result + (consequenceType != null ? consequenceType.hashCode() : 0);
        return result;
    }
}
