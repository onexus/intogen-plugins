package org.intogen.pages;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.*;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.util.convert.ConversionException;
import org.intogen.boxes.BoxesPanel;
import org.onexus.core.ICollectionManager;
import org.onexus.core.IEntity;
import org.onexus.core.IResourceManager;
import org.onexus.core.query.Contains;
import org.onexus.core.query.Query;
import org.onexus.core.resources.Collection;
import org.onexus.core.resources.Field;
import org.onexus.core.utils.EntityIterator;
import org.onexus.core.utils.QueryUtils;
import org.onexus.core.utils.ResourceUtils;
import org.onexus.ui.website.pages.Page;
import org.onexus.ui.website.utils.EntityModel;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class SearchPage extends Page<SearchPageConfig, SearchPageStatus> {

    @Inject
    public ICollectionManager collectionManager;

    @Inject
    public IResourceManager resourceManager;

    public SearchPage(String componentId, IModel<SearchPageStatus> statusModel) {
        super(componentId, statusModel);

        add(new Image("logo", new PackageResourceReference(SearchPage.class, "logo.png")));

        Form form = new Form<SearchPageStatus>("form", new CompoundPropertyModel<SearchPageStatus>(new PropertyModel<SearchPageStatus>(this, "status"))) {
            @Override
            protected void onSubmit() {
                String baseUri = ResourceUtils.getParentURI(SearchPage.this.getConfig().getWebsiteConfig().getURI());
                SearchPage.this.addOrReplace(new BoxesPanel("boxes", SearchPage.this.getStatus(), baseUri));
            }
        };

        // By default use the first search type
        List<SearchType> types = getConfig().getTypes();
        if (getStatus().getType() == null && !types.isEmpty()) {
            getStatus().setType(types.get(0));
        }

        DropDownChoice<SearchType> typeSelect = new DropDownChoice<SearchType>("type", types, new SearchTypeRenderer());
        typeSelect.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                SearchPage.this.addOrReplace(new EmptyPanel("boxes").setOutputMarkupId(true));
                target.add(SearchPage.this.get("boxes"));
            }
        });
        form.add(typeSelect);


        TextField<String> search = new TextField<String>("search");

        search.add(new AutoCompleteBehavior<IEntity>(new EntityRenderer(), new AutoCompleteSettings()) {

            @Override
            protected Iterator<IEntity> getChoices(String input) {
                return getAutocompleteChoices(input);
            }

        });

        form.add(search);

        add(form);

        add(new EmptyPanel("boxes").setOutputMarkupId(true));

    }

    private Iterator<IEntity> getAutocompleteChoices(String input) {

        Query query = new Query();
        SearchType type = getStatus().getType();

        String collectionUri = getAbsoluteUri(type.getCollection());
        String collectionAlias = QueryUtils.newCollectionAlias(query, collectionUri);
        query.setFrom(collectionAlias);

        List<String> fieldList = type.getFieldsList();
        query.addSelect(collectionAlias, fieldList);

        for (String field : fieldList) {
            QueryUtils.or(query, new Contains(collectionAlias, field, input));
        }

        query.setCount(10);

        return new EntityIterator(collectionManager.load(query), collectionUri);
    }

    private String getAbsoluteUri(String partialUri) {
        String baseUri = ResourceUtils.getParentURI(SearchPage.this.getConfig().getWebsiteConfig().getURI());
        return ResourceUtils.getAbsoluteURI(baseUri, partialUri);
    }


    private class SearchTypeRenderer implements IChoiceRenderer<SearchType> {

        @Override
        public Object getDisplayValue(SearchType type) {

            String collectionUri = type.getCollection();

            Collection collection = resourceManager.load(Collection.class, getAbsoluteUri(collectionUri));

            if (collection==null) {
                return collectionUri;
            }

            String title = collection.getTitle();
            return (title == null ? collection.getName() : title);
        }

        @Override
        public String getIdValue(SearchType object, int index) {
            return Integer.toString(index);
        }
    }

    /**
     * Generic IEntity renderer that show all the fields.
     */
    private class EntityRenderer implements IAutoCompleteRenderer<IEntity> {

        public final void render(final IEntity object, final Response response, final String criteria)
        {
            String textValue = getTextValue(object, criteria);
            if (textValue == null)
            {
                throw new IllegalStateException(
                        "A call to textValue(Object) returned an illegal value: null for object: " +
                                object.toString());
            }
            textValue = textValue.replaceAll("\\\"", "&quot;");

            response.write("<li textvalue=\"" + textValue + "\"");
            response.write(">");
            renderChoice(object, response, criteria);
            response.write("</li>");
        }

        private String getTextValue(IEntity object, String criteria) {


            SearchType type = getStatus().getType();
            List<String> fields = type.getKeysList();

            for (String field : fields) {
                String value = String.valueOf(object.get(field));

                if (StringUtils.containsIgnoreCase(value, criteria)) {
                    return value;
                }
            }

            return String.valueOf(object.get(fields.get(0)));
        }

        public final void renderHeader(final Response response)
        {
            response.write("<ul>");
        }

        public final void renderFooter(final Response response, int count)
        {
            response.write("</ul>");
        }

        protected void renderChoice(IEntity object, Response response, String criteria) {

            SearchType type = getStatus().getType();
            List<String> keys = type.getKeysList();
            List<String> fields = type.getFieldsList();


            boolean keyFieldAdded = false;
            for (String field : fields) {
                String value = String.valueOf(object.get(field));

                if (StringUtils.containsIgnoreCase(value, criteria)) {
                    if (keys.contains(field)) {
                        keyFieldAdded = true;
                    }

                    renderValue(response, value, criteria, field);

                }
            }

            if (!keyFieldAdded) {
                String field = keys.get(0);
                String value = String.valueOf(object.get(field));
                renderValue(response, value, criteria, field);
            }
        }

        private void renderValue(Response response, String value, String criteria, String field ) {

            String hlValue = value.replaceAll("(?i)(" + criteria + ")", "<strong>$1</strong>");
            response.write( "<span class='f'>" + field.toLowerCase() + ":</span>" + hlValue);
            response.write("<br />");
        }


    }



}
