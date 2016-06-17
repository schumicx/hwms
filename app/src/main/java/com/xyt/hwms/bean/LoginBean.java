package com.xyt.hwms.bean;

import java.io.Serializable;

/**
 * Created by Levin on 2016-05-17.
 */
public class LoginBean extends BaseBean implements Serializable {
    private User data;

    public LoginBean() {
    }

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }
}
