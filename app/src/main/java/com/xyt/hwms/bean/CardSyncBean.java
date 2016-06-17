package com.xyt.hwms.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Levin on 2016-05-17.
 */
public class CardSyncBean extends BaseBean implements Serializable {
    private List<User> data;

    public CardSyncBean() {
    }

    public List<User> getData() {
        return data;
    }

    public void setData(List<User> data) {
        this.data = data;
    }
}
