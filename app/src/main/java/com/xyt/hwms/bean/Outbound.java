package com.xyt.hwms.bean;

/**
 * Created by Levin on 2016-06-06.
 */
public class Outbound {
    /**
     * out_record_id : 0155251c53f08a8a8b1f55251b8d0004
     * out_type : CON
     * label_code : CON-B-Y-001-002
     * object_name : 固废
     */

    private String out_record_id;
    private String out_type;
    private String label_code;
    private String object_name;

    public String getOut_record_id() {
        return out_record_id;
    }

    public void setOut_record_id(String out_record_id) {
        this.out_record_id = out_record_id;
    }

    public String getOut_type() {
        return out_type;
    }

    public void setOut_type(String out_type) {
        this.out_type = out_type;
    }

    public String getLabel_code() {
        return label_code;
    }

    public void setLabel_code(String label_code) {
        this.label_code = label_code;
    }

    public String getObject_name() {
        return object_name;
    }

    public void setObject_name(String object_name) {
        this.object_name = object_name;
    }
}
