package org.intogen.boxes;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.onexus.core.IEntity;
import org.onexus.core.resources.Collection;
import org.onexus.core.resources.Field;
import org.onexus.ui.website.Website;

public class EntitySelectBox extends Panel {

    private String entityId;

    public EntitySelectBox(String id, IEntity entity) {
        super(id);

        this.entityId = entity.getId();

        add(new Label("label", StringUtils.replace(entityId, "\t", "-")));

        Collection collection = entity.getCollection();

        StringBuilder description = new StringBuilder();
        description.append("<ul>");
        for (Field field : collection.getFields()) {

            if (field.isPrimaryKey() != null && field.isPrimaryKey()) {
                continue;
            }

            Object value = entity.get(field.getId());
            if (value != null && !StringUtils.isEmpty(value.toString())) {
                description.append("<li><span class=\"f\">").append(field.getLabel()).append(":</span>");
                description.append(StringUtils.abbreviate(value.toString(), 100)).append("</li>");
            }
        }
        description.append("</ul>");

        add(new Label("description", description.toString()).setEscapeModelStrings(false).setRenderBodyOnly(true));

    }

    @Override
    protected void onInitialize() {
        PageParameters parameters = new PageParameters(getPage().getPageParameters());
        add(new BookmarkablePageLink<String>("link", Website.class, parameters) );
        super.onInitialize();
    }

}
