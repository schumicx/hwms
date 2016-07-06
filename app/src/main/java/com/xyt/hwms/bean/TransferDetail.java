package com.xyt.hwms.bean;

/**
 * Created by Levin on 2016-06-02.
 */
public class TransferDetail {

    /**
     * waste_detail_id //固废 id
     * waste_name : //固废名称
     * category_code : //固废代码
     * is_key_waste : //重点监管
     * container_label_code : CON-0001
     * back_reason : xxxx
     * back_reason_index : 1
     * status : a
     * label_code : Y-HW06-MDI-20160527-0014
     * produce_source : 装置检修
     */

    private String transfer_detail_id;
    private String waste_detail_id;
    private String waste_name;
    private String category_code;
    private String is_key_waste;
    private String container_label_code;
    private String back_reason;
    private String back_reason_index;
    private String status;
    private String label_code;
    private String produce_source;
    private String transfer_time;
    private String package_type;
    private String harmful_ingredient;

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

    public String getBack_reason() {
        return back_reason;
    }

    public void setBack_reason(String back_reason) {
        this.back_reason = back_reason;
    }

    public String getBack_reason_index() {
        return back_reason_index;
    }

    public void setBack_reason_index(String back_reason_index) {
        this.back_reason_index = back_reason_index;
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

    public String getWaste_detail_id() {
        return waste_detail_id;
    }

    public void setWaste_detail_id(String waste_detail_id) {
        this.waste_detail_id = waste_detail_id;
    }
}