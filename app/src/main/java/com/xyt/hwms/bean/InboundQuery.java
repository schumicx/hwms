package com.xyt.hwms.bean;

import java.io.Serializable;

/**
 * Created by Levin on 2016-05-17.
 */
public class InboundQuery extends BaseBean implements Serializable {

    private String opera_record_id;
    private String store_label_code;
    private String position_label_code;
    private Float total_weight;
    private Float weight;
    private String transfer_detail_id;
    private String waste_name;
    private String category_code;
    private String is_key_waste;
    private String container_label_code;
    private String status;
    private String label_code;
    private String produce_source;
    private String package_type;
    private String harmful_ingredient;

    public String getOpera_record_id() {
        return opera_record_id;
    }

    public void setOpera_record_id(String opera_record_id) {
        this.opera_record_id = opera_record_id;
    }

    public String getStore_label_code() {
        return store_label_code;
    }

    public void setStore_label_code(String store_label_code) {
        this.store_label_code = store_label_code;
    }

    public String getPosition_label_code() {
        return position_label_code;
    }

    public void setPosition_label_code(String position_label_code) {
        this.position_label_code = position_label_code;
    }

    public Float getTotal_weight() {
        return total_weight;
    }

    public void setTotal_weight(Float total_weight) {
        this.total_weight = total_weight;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public String getTransfer_detail_id() {
        return transfer_detail_id;
    }

    public void setTransfer_detail_id(String transfer_detail_id) {
        this.transfer_detail_id = transfer_detail_id;
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

    public String getContainer_label_code() {
        return container_label_code;
    }

    public void setContainer_label_code(String container_label_code) {
        this.container_label_code = container_label_code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
