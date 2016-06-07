package com.xyt.hwms.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Levin on 2016-05-17.
 */
public class OutboundTranferBean extends BaseBean implements Serializable {
    private List<OutboundTranfer> data;

    public OutboundTranferBean() {
    }

    public List<OutboundTranfer> getData() {
        return data;
    }

    public void setData(List<OutboundTranfer> data) {
        this.data = data;
    }
}
