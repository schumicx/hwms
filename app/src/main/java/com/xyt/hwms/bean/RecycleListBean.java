package com.xyt.hwms.bean;

import java.io.Serializable;

/**
 * Created by Levin on 2016-05-17.
 */
public class RecycleListBean extends BaseBean implements Serializable {
    private RecycleList data;

    public RecycleListBean() {
    }

    public RecycleList getData() {
        return data;
    }

    public void setData(RecycleList data) {
        this.data = data;
    }
}
