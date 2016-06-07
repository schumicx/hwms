package com.xyt.hwms.bean;

import java.io.Serializable;

/**
 * Created by Levin on 2016-05-17.
 */
public class OutboundListBean extends BaseBean implements Serializable {
    private OutboundList data;

    public OutboundListBean() {
    }

    public OutboundList getData() {
        return data;
    }

    public void setData(OutboundList data) {
        this.data = data;
    }
}
