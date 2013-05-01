package org.intogen.decorators.xrefs;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.string.Strings;
import org.intogen.decorators.intogen.IntogenColumnDecorator;
import org.onexus.collection.api.Field;
import org.onexus.collection.api.IEntity;
import org.onexus.resource.api.Parameters;
import org.onexus.website.api.widgets.tableviewer.decorators.utils.FieldDecorator;

import java.util.regex.Pattern;

public class XRefsDecorator extends FieldDecorator {

    private Parameters parameters;

    public XRefsDecorator(Field field, Parameters parameters) {
        super(field);
        this.parameters = parameters;
    }

    private static Pattern PATTERN_COMMA = Pattern.compile(",");
    private static Pattern PATTERN_COLON = Pattern.compile(":");

    @Override
    public void populateCell(WebMarkupContainer cellContainer, String componentId, IModel<IEntity> data) {

        IEntity entity = data.getObject();
        String xrefs = (String) entity.get("XREFS");
        StringBuilder label = new StringBuilder();

        if (!Strings.isEmpty(xrefs)) {
            String values[] = PATTERN_COMMA.split(xrefs);

            for (String value : values) {
                String pair[] = PATTERN_COLON.split(value);

                if (pair.length != 2 || Strings.isEmpty(pair[0]) || Strings.isEmpty(pair[1])) {
                    continue;
                }

                String text = "<span class=\"badge\">?</span>";
                String title = pair[0] + ": " + pair[1];
                String link = "";
                String target = "target=\"_blank\"";

                if ("ESP".equalsIgnoreCase(pair[0])) {
                    text = "<span class=\"badge badge-info\">E</span>";
                    link = "http://www.ensembl.org/Homo_sapiens/Variation/Summary?source=ESP;v=" + pair[1];
                }

                if ("dbSNP".equalsIgnoreCase(pair[0])) {
                    text = "<span class=\"badge badge-success\">S</span>";
                    link = "http://www.ncbi.nlm.nih.gov/projects/SNP/snp_ref.cgi?rs=" + pair[1].substring(2);

                }

                if ("COSMIC".equalsIgnoreCase(pair[0])) {
                    text = "<span class=\"badge badge-important\">C</span>";
                    link = "http://cancer.sanger.ac.uk/cosmic/mutation/overview?id=" + pair[1].substring(4);
                }

                if ("I".equalsIgnoreCase(pair[0])) {
                    text = "<span class=\"badge badge-intogen\">I</span>";
                    link = IntogenColumnDecorator.getUrl(
                            data.getObject(),
                            parameters.get(XRefsParameters.INTOGEN_URL),
                            parameters.get(XRefsParameters.GENES),
                            parameters.get(XRefsParameters.MUTATIONS)
                    );
                    title = "mutation seen in IntOGen, click to go to IntOGen to see in which cancer types";
                    target = "";
                }



                label.append("<a rel=\"tooltip\" title=\"").append(title);
                if (Strings.isEmpty(link)) {
                    label.append("\" disabled=\"disabled");
                }
                label.append("\" ").append(target).append(" href=\"").append(link).append("\">");
                label.append(text);
                label.append("</a>&nbsp;");
            }

        }

        cellContainer.add(new Label(componentId, label.toString()).setEscapeModelStrings(false));

    }

}
