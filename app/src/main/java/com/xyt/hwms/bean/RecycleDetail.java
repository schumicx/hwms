package com.xyt.hwms.bean;

/**
 * Created by Levin on 2016-06-02.
 */
public class RecycleDetail {

    private String record_id;
    private String waste_name;
    private String category_code;
    private String is_key_waste;
    private String label_code;
    private String produce_source;
    private String transfer_time;
    private String package_type;
    private String harmful_ingredient;

    public String getRecord_id() {
        return record_id;
    }

    public void setRecord_id(String record_id) {
        this.record_id = record_id;
    }

    public String getWaste_name() {
        return waste_name;
    }

    public void setWaste_name(String waste_name) {
        this.waste_name = waste_name;
    }

    public String getCategory_code() {
        return category_code;
    }

    public void setCategory_code(String category_code) {
        this.category_code = category_code;
    }

    public String getIs_key_waste() {
        return is_key_waste;
    }

    public void setIs_key_waste(String is_key_waste) {
        this.is_key_waste = is_key_waste;
    }

    public String getLabel_code() {
        return label_code;
    }

    public void setLabel_code(String label_code) {
        this.label_code = label_code;
    }

    public String getProduce_source() {
        return produce_source;
    }

    public void setProduce_source(String produce_source) {
        this.produce_source = produce_source;
    }

    public String getTransfer_time() {
        return transfer_time;
    }

    public void setTransfer_time(String transfer_time) {
        this.transfer_time = transfer_time;
    }

    public String getPackage_type() {
        return package_type;
    }

    public void setPackage_type(String package_type) {
        this.package_type = package_type;
    }

    public String getHarmful_ingredient() {
        return harmful_ingredient;
    }

    public void setHarmful_ingredient(String harmful_ingredient) {
        this.harmful_ingredient = harmful_ingredient;
    }
}