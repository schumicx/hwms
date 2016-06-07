package com.xyt.hwms.bean;

import java.io.Serializable;

/**
 * Created by Levin on 2016-05-17.
 */
public class OutboundDetailListBean extends BaseBean implements Serializable {
    private OutboundDetailList data;

    public OutboundDetailListBean() {
    }

    public OutboundDetailList getData() {
        return data;
    }

    public void setData(OutboundDetailList data) {
        this.data = data;
    }
}
