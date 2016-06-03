package com.xyt.hwms.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Levin on 2016-05-17.
 */
public class InboundQueryBean extends BaseBean implements Serializable {
    private List<InboundQuery> data;

    public InboundQueryBean() {
    }

    public List<InboundQuery> getData() {
        return data;
    }

    public void setData(List<InboundQuery> data) {
        this.data = data;
    }
}
