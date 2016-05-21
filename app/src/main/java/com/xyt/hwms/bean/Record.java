package com.xyt.hwms.bean;

import java.util.List;
import java.util.Map;

/**
 * Created by Levin on 2016-05-20.
 */
public class Record {
    private List<Map> collection;
    private Map model;

    public List<Map> getCollection() {
        return collection;
    }

    public void setCollection(List<Map> collection) {
        this.collection = collection;
    }

    public Map getModel() {
        return model;
    }

    public void setModel(Map model) {
        this.model = model;
    }
}
