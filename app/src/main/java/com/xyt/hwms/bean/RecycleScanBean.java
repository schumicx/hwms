package com.xyt.hwms.bean;

import java.io.Serializable;

/**
 * Created by Levin on 2016-05-17.
 */
public class RecycleScanBean extends BaseBean implements Serializable {
    private RecycleDetail data;

    public RecycleScanBean() {
    }

    public RecycleDetail getData() {
        return data;
    }

    public void setData(RecycleDetail data) {
        this.data = data;
    }
}
