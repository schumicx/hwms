package com.xyt.hwms.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Levin on 2016-05-20.
 */
public class Transfer implements Serializable {

    private String transfer_id;
    private String apply_code;
    private String parent_org_name;
    private String car_code;
    private String card_code;
    private String card_name;
    private long create_time;
    private String detail_status;
    private String operator;
    private String transfer_type;
    private String phone;
    private String duty_person;
    private String org_name;
    private String lock_code;
    private List<TransferDetail> detail;

    public String getTransfer_id() {
        return transfer_id;
    }

    public void setTransfer_id(String transfer_id) {
        this.transfer_id = transfer_id;
    }

    public String getApply_code() {
        return apply_code;
    }

    public void setApply_code(String apply_code) {
        this.apply_code = apply_code;
    }

    public String getParent_org_name() {
        return parent_org_name;
    }

    public void setParent_org_name(String parent_org_name) {
        this.parent_org_name = parent_org_name;
    }

    public String getCar_code() {
        return car_code;
    }

    public void setCar_code(String car_code) {
        this.car_code = car_code;
    }

    public String getCard_code() {
        return card_code;
    }

    public void setCard_code(String card_code) {
        this.card_code = card_code;
    }

    public String getCard_name() {
        return card_name;
    }

    public void setCard_name(String card_name) {
        this.card_name = card_name;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public String getDetail_status() {
        return detail_status;
    }

    public void setDetail_status(String detail_status) {
        this.detail_status = detail_status;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getTransfer_type() {
        return transfer_type;
    }

    public void setTransfer_type(String transfer_type) {
        this.transfer_type = transfer_type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDuty_person() {
        return duty_person;
    }

    public void setDuty_person(String duty_person) {
        this.duty_person = duty_person;
    }

    public String getOrg_name() {
        return org_name;
    }

    public void setOrg_name(String org_name) {
        this.org_name = org_name;
    }

    public String getLock_code() {
        return lock_code;
    }

    public void setLock_code(String lock_code) {
        this.lock_code = lock_code;
    }

    public List<TransferDetail> getDetail() {
        return detail;
    }

    public void setDetail(List<TransferDetail> detail) {
        this.detail = detail;
    }
}
