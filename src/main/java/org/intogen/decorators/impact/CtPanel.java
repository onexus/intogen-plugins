package org.intogen.decorators.impact;


import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.util.string.Strings;
import org.intogen.decorators.impact.domain.Consequence;
import org.intogen.decorators.impact.domain.Mutation;
import org.onexus.collection.api.ICollectionManager;
import org.onexus.collection.api.IEntity;
import org.onexus.resource.api.Parameters;
import org.onexus.website.api.widgets.tableviewer.formaters.DoubleFormater;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CtPanel extends Panel {

    @Inject
    private ICollectionManager collectionManager;

    public CtPanel(String id, IEntity entity, Parameters parameters) {
        super(id);

        Mutation mutation = new Mutation(entity, collectionManager, parameters);

        RepeatingView boxes = new RepeatingView("boxes");
        add(boxes);

        for (String snv : mutation.getSnvs()) {

            WebMarkupContainer snvItem = new WebMarkupContainer(boxes.newChildId());
            boxes.add(snvItem);

            String snvArray[] = snv.split(":");

            snvItem.add(new Label("snv", snv));
            snvItem.add(new WebMarkupContainer("snv_link").add(new AttributeModifier("href", "http://www.ensembl.org/Homo_sapiens/Location/View?r=" + snvArray[0] + "%3A" + snvArray[1] + "-" + snvArray[1])));
            snvItem.add(new Label("ensembl", mutation.getEnsembl()));
            snvItem.add(new Label("symbol", mutation.getSymbol()));

            Label button = new Label("impact", ImpactButton.impactToLabel(mutation.getImpact()));
            button.add(new AttributeModifier("class", ImpactButton.impactToLabelClass(mutation.getImpact())));
            snvItem.add(button);

            snvItem.add(new WebMarkupContainer("ensembl_link").add(new AttributeModifier("href", "http://www.ensembl.org/Homo_sapiens/Gene/Summary?g=" + mutation.getEnsembl())));

            RepeatingView consquencesContainer = new RepeatingView("consequences");

            List<Consequence> consequences = new ArrayList<Consequence>(mutation.getConsequences(snv));

            Collections.sort(consequences, new Comparator<Consequence>() {
                @Override
                public int compare(Consequence o1, Consequence o2) {
                    return impactToInteger(o2.getImpact()).compareTo(impactToInteger(o1.getImpact()));
                }
            });

            for (Consequence consequence : consequences) {

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
                String label = createLabelSpan(consequence.getImpact());
                label = label + "&nbsp;&nbsp;" + String.valueOf(consequence.getTranscript());
                if (!Strings.isEmpty(consequence.getUniprot()) && !"null".equals(consequence.getUniprot())) {
                    label = label + " (" + consequence.getUniprot() + ")";
                }
                label = label + " - " + String.valueOf(consequence.getConsequenceType());
                accordionToggle.add(new Label("label", label).setEscapeModelStrings(false));

                // Type of CT
                String ct = consequence.getConsequenceType();
                int type = 0;

                if (ct.contains("missense_variant")) {
                    type = 1;
                } else if (ct.contains("framseshift_variant") ||
                        ct.contains("synonymous_variant") ||
                        ct.contains("stop_gained")
                        ) {
                    type = 2;
                }

                // Fields
                RepeatingView fields = new RepeatingView("fields");
                fields.setRenderBodyOnly(true);
                accordionBody.add(fields);

                if (type == 1 || type == 2) {
                    addField(fields, "Protein", consequence.getProtein(), true);
                    if (!"null".equalsIgnoreCase(consequence.getUniprot())) {
                        addField(fields, "Uniprot", consequence.getUniprot(), true);
                    }
                }

                if (type == 1) {
                    addField(fields, "AA change", consequence.getAachange(), true);
                    addField(fields, "Protein position", consequence.getProteinPosition(), true);
                }

                if (type == 2 || type == 0) {
                    addField(fields, "Impact", createLabelSpan(consequence.getImpact()), false);
                }

                // Table
                WebMarkupContainer table = new WebMarkupContainer("table");
                accordionBody.add(table);
                table.setVisible(type == 1);

                table.add(new Label("siftClass", createClassLabel(consequence.getSiftClass())).setEscapeModelStrings(false));
                table.add(new Label("siftScore", DoubleFormater.format(consequence.getSiftScore(), 3)));
                table.add(new Label("siftTrans", DoubleFormater.format(consequence.getSiftTrans(), 3)));

                table.add(new Label("pph2Class", createClassLabel(consequence.getPph2Class())).setEscapeModelStrings(false));
                table.add(new Label("pph2Score", DoubleFormater.format(consequence.getPph2Score(), 3)));
                table.add(new Label("pph2Trans", DoubleFormater.format(consequence.getPph2Trans(), 3)));

                table.add(new Label("maClass", createClassLabel(consequence.getMaClass())).setEscapeModelStrings(false));
                table.add(new Label("maScore", DoubleFormater.format(consequence.getMaScore(), 3)));
                table.add(new Label("maTrans", DoubleFormater.format(consequence.getMaTrans(), 3)));
            }

            snvItem.add(consquencesContainer);
        }
    }

    private static void addField(RepeatingView view, String title, String value, boolean escape) {

        if (!Strings.isEmpty(value)) {
            WebMarkupContainer item = new WebMarkupContainer(view.newChildId());
            item.add(new Label("title", title));
            item.add(new Label("value", value).setEscapeModelStrings(escape));
            view.add(item);
        }

    }

    private static String createLabelSpan(Object impact) {
        return "<span class=\"" +
                ImpactButton.impactToLabelClass(impact) +
                "\">" +
                ImpactButton.impactToLabel(impact) +
                "</span>";
    }

    private static String createClassLabel(String className) {

        if (className == null || Strings.isEmpty(className)) {
            return "<span class=\"label\">NA</span>";
        }

        if (className.equals("low")) {
            return "<span class=\"label label-info\">Low</span>";
        }

        if (className.equals("medium")) {
            return "<span class=\"label label-warning\">Medium</span>";
        }

        if (className.equals("high")) {
            return "<span class=\"label label-important\">High</span>";
        }

        return "<span class=\"label\">NA</span>";

    }

    private static Integer impactToInteger(Object impact) {

        if (impact instanceof Integer) {
            return (Integer) impact;
        }

        return 6;

    }
}
