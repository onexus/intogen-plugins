package org.intogen.boxes;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.intogen.pages.SearchPageStatus;
import org.intogen.pages.SearchType;
import org.onexus.core.ICollectionManager;
import org.onexus.core.IEntityTable;
import org.onexus.core.IResourceManager;
import org.onexus.core.query.Contains;
import org.onexus.core.query.Query;
import org.onexus.core.utils.QueryUtils;
import org.onexus.core.utils.ResourceUtils;

import javax.inject.Inject;
import java.util.List;

public class BoxesPanel extends Panel {

    @Inject
    public ICollectionManager collectionManager;

    @Inject
    public IResourceManager resourceManager;

    public BoxesPanel(String id, SearchPageStatus status, String baseUri) {
        super(id);
        setOutputMarkupId(true);

        SearchType type = status.getType();
        String collectionUri = ResourceUtils.getAbsoluteURI(baseUri, type.getCollection());

        IEntityTable table = getEntityTable(type, collectionUri, status.getSearch());

        RepeatingView boxes = new RepeatingView("boxes");

        long size = 0;
        while (table.next()) {
            boxes.add(new EntitySelectBox(boxes.newChildId(), table.getEntity(collectionUri)));
            size++;
        }

        if (size == 1) {
            boxes.add(new Label(boxes.newChildId(), "DETAIL BOXES"));
        }

        add(boxes);

    }

    private IEntityTable getEntityTable(SearchType type, String collectionUri, String search) {

        Query query = new Query();

        String collectionAlias = QueryUtils.newCollectionAlias(query, collectionUri);
        query.setFrom(collectionAlias);

        List<String> fieldList = type.getFieldsList();
        query.addSelect(collectionAlias, fieldList);

        for (String field : fieldList) {
            QueryUtils.or(query, new Contains(collectionAlias, field, search));
        }

        query.setCount(10);

        return collectionManager.load(query);
    }


}
