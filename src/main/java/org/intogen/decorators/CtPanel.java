package org.intogen.decorators;


import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.intogen.decorators.domain.Consequence;
import org.intogen.decorators.domain.Mutation;
import org.onexus.collection.api.ICollectionManager;
import org.onexus.collection.api.IEntity;
import org.onexus.website.api.widgets.tableviewer.formaters.DoubleFormater;
import org.ops4j.pax.wicket.api.PaxWicketBean;

public class CtPanel extends Panel {

    @PaxWicketBean(name = "collectionManager")
    private ICollectionManager collectionManager;

    public CtPanel(String id, IEntity entity) {
        super(id);

        Mutation mutation = new Mutation(entity, collectionManager);

        add(new Label("snv", mutation.getSnv()));
        add(new WebMarkupContainer("snv_link").add(new AttributeModifier("href", "http://www.ensembl.org/Homo_sapiens/Location/View?r="+mutation.getChromosome()+"%3A"+mutation.getPosition()+"-"+mutation.getPosition())));
        //add(new Label("symbol", mutation.getSymbol()));
        add(new Label("ensembl", mutation.getEnsembl()));
        add(new WebMarkupContainer("ensembl_link").add(new AttributeModifier("href", "http://www.ensembl.org/Homo_sapiens/Gene/Summary?g="+mutation.getEnsembl())));
        //add(new Label("externalId", mutation.getExternalId()));
        //add(new Label("recurrence", Integer.toString(mutation.getRecurrence())));
        //add(new Label("samples", Integer.toString(mutation.getSamples())));
        //add(new Label("frequency", DoubleFormater.format(mutation.getFrequency(), 3)));

        RepeatingView consquencesContainer = new RepeatingView("consequences");
        for (Consequence consequence : mutation.getConsequences()) {

            WebMarkupContainer item = new WebMarkupContainer(consquencesContainer.newChildId());
            consquencesContainer.add(item);

            // Prepare accordion container
            WebMarkupContainer accordionToggle = new WebMarkupContainer("accordion-toggle");
            WebMarkupContainer accordionBody = new WebMarkupContainer("accordion-body");
            String bodyId = item.getMarkupId() + "-body";
            accordionBody.setMarkupId(bodyId);
            accordionToggle.add(new AttributeModifier("href", "#" + bodyId));
            item.add(accordionToggle);
            item.add(accordionBody);

            // Label
            String label = String.valueOf(consequence.getTranscript());
            if (!"null".equals(consequence.getUniprot())) {
                label = label + " (" + consequence.getUniprot() + ")";
            }
            label = label + " - " + String.valueOf(consequence.getConsequenceType());
            accordionToggle.add(new Label("label", label));

            // Fields
            accordionBody.add(new Label("protein", consequence.getProtein()));
            accordionBody.add(new Label("uniprot", consequence.getUniprot()));
            accordionBody.add(new Label("aachange", consequence.getAachange()));
            accordionBody.add(new Label("proteinpos", consequence.getProteinPosition()));

            accordionBody.add(new Label("siftClass", createClassLabel(consequence.getSiftClass())).setEscapeModelStrings(false));
            accordionBody.add(new Label("siftScore", DoubleFormater.format(consequence.getSiftScore(), 3)));
            accordionBody.add(new Label("siftTrans", DoubleFormater.format(consequence.getSiftTrans(), 3)));

            accordionBody.add(new Label("pph2Class", createClassLabel(consequence.getPph2Class())).setEscapeModelStrings(false));
            accordionBody.add(new Label("pph2Score", DoubleFormater.format(consequence.getPph2Score(), 3)));
            accordionBody.add(new Label("pph2Trans", DoubleFormater.format(consequence.getPph2Trans(), 3)));

            accordionBody.add(new Label("maClass", createClassLabel(consequence.getMaClass())).setEscapeModelStrings(false));
            accordionBody.add(new Label("maScore", DoubleFormater.format(consequence.getMaScore(), 3)));
            accordionBody.add(new Label("maTrans", DoubleFormater.format(consequence.getMaTrans(), 3)));

        }
        add(consquencesContainer);
    }

    private String createClassLabel(String className) {

        if (className == null || StringUtils.isEmpty(className)) {
            return "<span class=\"label\">NA</span>";
        }

        if (className.equals("low_impact")) {
            return "<span class=\"label label-success\">low</span>";
        }

        if (className.equals("medium_impact")) {
            return "<span class=\"label label-warning\">medium</span>";
        }

        if (className.equals("high_impact")) {
            return "<span class=\"label label-important\">high</span>";
        }

        return "<span class=\"label\">NA</span>";

    }
}
