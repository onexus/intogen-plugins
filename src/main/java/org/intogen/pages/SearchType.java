package org.intogen.pages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SearchType implements Serializable {

    private String collection;
    private String fields;

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }

    public List<String> getFieldsList() {
        String[] fields = this.fields.split(",");

        List<String> fieldList = new ArrayList<String>(fields.length);

        for (String field : fields) {
            fieldList.add(field.trim());
        }

        return fieldList;
    }
}
