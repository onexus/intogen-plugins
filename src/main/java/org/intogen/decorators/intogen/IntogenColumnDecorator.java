package org.intogen.decorators.intogen;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.encoding.UrlEncoder;
import org.onexus.collection.api.Field;
import org.onexus.collection.api.IEntity;
import org.onexus.resource.api.ORI;
import org.onexus.resource.api.Parameters;
import org.onexus.website.api.pages.browser.FilterEntity;
import org.onexus.website.api.pages.browser.IFilter;
import org.onexus.website.api.widgets.filters.BrowserFilter;
import org.onexus.website.api.widgets.filters.FilterConfig;
import org.onexus.website.api.widgets.tableviewer.decorators.utils.FieldDecorator;
import org.onexus.website.api.widgets.tableviewer.decorators.utils.LinkPanel;

public class IntogenColumnDecorator extends FieldDecorator {

    private Parameters parameters;

    public IntogenColumnDecorator(Field field, Parameters parameters) {
        super(field);
        this.parameters = parameters;
    }

    @Override
    public void populateCell(WebMarkupContainer cellContainer, String componentId, IModel<IEntity> data) {

        IEntity entity = data.getObject();

        Integer value = (Integer) entity.get(parameters.get(IntogenColumnParameters.FIELD));

        String label = "<span class=\"badge badge-intogen\">IntOGen</span>";

        if (value != null && value.equals(1) ) {
            LinkPanel linkPanel = new LinkPanel(componentId, label, getLink(LinkPanel.LINK_ID, entity));
            linkPanel.setEscapeModelStrings(false);
            cellContainer.add(linkPanel);
        } else {
            cellContainer.add(new EmptyPanel(componentId));
        }

    }

    private AbstractLink getLink(String componentId, IEntity entity) {

        Object chr = entity.get("CHR");
        Object start = entity.get("START");
        Object gene = entity.get("GENE_ID");

        IFilter filter;
        String url = parameters.get(IntogenColumnParameters.INTOGEN_URL);

        // We are linking the genes
        filter = new FilterEntity(new ORI(parameters.get(IntogenColumnParameters.GENES)), String.valueOf(gene));
        url = url + "?pf=" + UrlEncoder.QUERY_INSTANCE.encode(filter.toUrlParameter(true, null), "UTF-8");

        if (start != null) {
            // We are linking the mutations tab
            FilterConfig filterConfig = new FilterConfig();
            filterConfig.setName("Position '" + chr + ":" + start + "'");
            filterConfig.setCollection(new ORI(parameters.get(IntogenColumnParameters.MUTATIONS)));
            filterConfig.setDefine("c='"+parameters.get(IntogenColumnParameters.MUTATIONS)+"'");
            filterConfig.setWhere("c.START='" + start + "' AND c.CHR='" + chr + "'");
            filter = new BrowserFilter(filterConfig);

            url = url + "&pfc=" + UrlEncoder.QUERY_INSTANCE.encode(filter.toUrlParameter(true, null), "UTF-8");
        }

        ExternalLink link = new ExternalLink(componentId, url);
        return link;
    }

}
