package org.intogen.pages;

import org.apache.wicket.model.IModel;
import org.onexus.ui.IResourceRegister;
import org.onexus.ui.website.WebsiteConfig;
import org.onexus.ui.website.pages.AbstractPageCreator;
import org.onexus.ui.website.pages.Page;

public class SearchPageCreator extends AbstractPageCreator<SearchPageConfig, SearchPageStatus> {

    public SearchPageCreator() {
        super(SearchPageConfig.class, "search", "Search page");
    }

    @Override
    protected Page<?, ?> build(String componentId, IModel<SearchPageStatus> statusModel) {
        return new SearchPage(componentId, statusModel);
    }

    @Override
    public void register(IResourceRegister resourceRegister) {
        super.register(resourceRegister);

        resourceRegister.addAutoComplete(WebsiteConfig.class, "pages",
                "        <search>\n" +
                "               <id>search</id>\n" +
                "               <label>Search</label>\n" +
                "        </search>");
    }
}
