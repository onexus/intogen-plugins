package org.intogen.decorators;


import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.onexus.collection.api.*;
import org.onexus.collection.api.query.And;
import org.onexus.collection.api.query.Equal;
import org.onexus.collection.api.query.Query;
import org.onexus.collection.api.utils.EntityIterator;
import org.onexus.resource.api.utils.ResourceUtils;

import javax.inject.Inject;
import java.util.Iterator;

public class CtPanel extends Panel {

    private static String COLLECTION_CT = "data/snv_project_consequence-types";

    private static String FIELD_CHR = "CHROMOSOME";
    private static String FIELD_POSITION = "POSITION";
    private static String FIELD_ALLELE = "ALLELE";
    private static String FIELD_GENEID = "GENEID";
    private static String FIELD_PROJECTID = "PROJECTID";

    @Inject
    public ICollectionManager collectionManager;

    public CtPanel(String id, IEntity entity) {
        super(id);

        Iterator<IEntity> entities = loadConsequenceTypes(entity);

        RepeatingView entitiesContainer = new RepeatingView("entities");
        add(entitiesContainer);


        while (entities.hasNext()) {
            IEntity ct = entities.next();

            WebMarkupContainer item = new WebMarkupContainer(entitiesContainer.newChildId());
            entitiesContainer.add(item);

            // Prepare accordion containers
            WebMarkupContainer accordionToggle = new WebMarkupContainer("accordion-toggle");
            WebMarkupContainer accordionBody = new WebMarkupContainer("accordion-body");
            String bodyId = item.getMarkupId() + "-body";
            accordionBody.setMarkupId(bodyId);
            accordionToggle.add(new AttributeModifier("href", "#" + bodyId));
            item.add(accordionToggle);
            item.add(accordionBody);

            // Label
            Collection collection = ct.getCollection();
            String label = String.valueOf(ct.get("TRANSCRIPTID")) + " - " + String.valueOf(ct.get("CT"));
            accordionToggle.add(new Label("label", label));

            // Fields values
            RepeatingView fields = new RepeatingView("fields");
            for (Field field : collection.getFields()) {

                Object value = ct.get(field.getId());
                if (value != null && !StringUtils.isEmpty(value.toString())) {

                    WebMarkupContainer fc = new WebMarkupContainer(fields.newChildId());
                    fc.setRenderBodyOnly(true);
                    fc.add(new Label("label", field.getLabel()).add(new AttributeModifier("title", field.getTitle())));
                    fc.add(new Label("value", StringUtils.abbreviate(value.toString(), 50)));
                    fields.add(fc);
                }
            }
            accordionBody.add(fields);

        }

    }

    private Iterator<IEntity> loadConsequenceTypes(IEntity entity) {

        String projectUri = ResourceUtils.getProjectURI(entity.getCollection().getURI());
        String collectionUri = ResourceUtils.getAbsoluteURI(projectUri, COLLECTION_CT);

        String fromAlias = "c";
        Query query = new Query();
        query.setOn(projectUri);
        query.addDefine(fromAlias, COLLECTION_CT);
        query.setFrom(fromAlias);
        query.addSelect(fromAlias, null);
        query.setWhere(new And(new Equal(fromAlias, FIELD_CHR, entity.get(FIELD_CHR)),
                new And(new Equal(fromAlias, FIELD_POSITION, entity.get(FIELD_POSITION)),
                        new And(new Equal(fromAlias, FIELD_ALLELE, entity.get(FIELD_ALLELE)),
                                new And(new Equal(fromAlias, FIELD_GENEID, entity.get(FIELD_GENEID)),
                                        new Equal(fromAlias, FIELD_PROJECTID, entity.get(FIELD_PROJECTID))
                                )))));

        IEntityTable table = collectionManager.load(query);

        return new EntityIterator(table, collectionUri );
    }
}
