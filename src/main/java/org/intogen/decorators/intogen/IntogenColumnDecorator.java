package org.intogen.decorators.intogen;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.encoding.UrlEncoder;
import org.apache.wicket.util.string.Strings;
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

        String label = "<span class=\"badge badge-intogen\">"+parameters.get(IntogenColumnParameters.TEXT)+"</span>";

        if (value != null && value.equals(1) ) {
            LinkPanel linkPanel = new LinkPanel(componentId, label, getLink(LinkPanel.LINK_ID, entity));
            linkPanel.setEscapeModelStrings(false);
            cellContainer.add(linkPanel);
        } else {
            cellContainer.add(new EmptyPanel(componentId));
        }

    }

    private AbstractLink getLink(String componentId, IEntity  entity) {

        String url = getUrl(
                entity,
                parameters.get(IntogenColumnParameters.INTOGEN_URL),
                parameters.get(IntogenColumnParameters.GENES),
                null
        );

        ExternalLink link = new ExternalLink(componentId, url);

        String tooltip = parameters.get(IntogenColumnParameters.TOOLTIP);
        if (!Strings.isEmpty(tooltip)) {
            link.add(new AttributeModifier("rel", "tooltip"));
            link.add(new AttributeModifier("title", tooltip));
            link.add(new AttributeModifier("data-placement", "bottom"));
        }

        return link;
    }

    public static String getUrl(IEntity entity, String url, String genes, String mutations) {

        Object chr = entity.get("CHR");
        Object start = entity.get("START");
        Object gene = entity.get("GENE_ID");

        IFilter filter;

        // We are linking the genes
        filter = new FilterEntity(new ORI(genes), String.valueOf(gene));
        url = url + "?pf=" + UrlEncoder.QUERY_INSTANCE.encode(filter.toUrlParameter(true, null), "UTF-8");

        if (mutations != null && start != null) {
            // We are linking the mutations tab
            FilterConfig filterConfig = new FilterConfig();
            filterConfig.setName("Position '" + chr + ":" + start + "'");
            filterConfig.setCollection(new ORI(mutations));
            filterConfig.setDefine("c='"+mutations+"'");
            filterConfig.setWhere("c.START='" + start + "' AND c.CHR='" + chr + "'");
            filter = new BrowserFilter(filterConfig);

            url = url + "&pfc=" + UrlEncoder.QUERY_INSTANCE.encode(filter.toUrlParameter(true, null), "UTF-8");
        }

        return url;
    }

}
