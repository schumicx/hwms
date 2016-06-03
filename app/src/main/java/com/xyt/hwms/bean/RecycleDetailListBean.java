package com.xyt.hwms.bean;

import java.io.Serializable;

/**
 * Created by Levin on 2016-05-17.
 */
public class RecycleDetailListBean extends BaseBean implements Serializable {
    private RecycleDetailList data;

    public RecycleDetailListBean() {
    }

    public RecycleDetailList getData() {
        return data;
    }

    public void setData(RecycleDetailList data) {
        this.data = data;
    }
}
