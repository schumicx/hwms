package com.xyt.hwms.bean;

import java.io.Serializable;

/**
 * Created by Levin on 2016-05-17.
 */
public class TransferListBean extends BaseBean implements Serializable {
    private TransferList data;

    public TransferListBean() {
    }

    public TransferList getData() {
        return data;
    }

    public void setData(TransferList data) {
        this.data = data;
    }
}
