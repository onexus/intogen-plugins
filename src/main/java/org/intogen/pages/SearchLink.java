package org.intogen.pages;

import java.io.Serializable;

public class SearchLink implements Serializable {

    private String title;
    private String url;

    public SearchLink() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
