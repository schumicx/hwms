package com.xyt.hwms.bean;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by Levin on 2016-05-17.
 */
public class EADMsgObject implements Serializable {
    private int code;
    private String content;
    private String error;
    private Object data;

    public EADMsgObject() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
