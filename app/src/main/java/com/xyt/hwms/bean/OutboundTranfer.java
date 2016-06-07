package com.xyt.hwms.bean;

import java.io.Serializable;

/**
 * Created by Levin on 2016-06-06.
 */
public class OutboundTranfer implements Serializable {

    /**
     * transfer_id : 01550a9495e58a8ae61a55063d660485
     * apply_code : 201606010001
     * phone : 15000000000
     * plan_transfer_time : 1464768000000
     * duty_person : 刘广安
     * create_time : 1464761357787
     * five_bills_code : 201606010002
     * user_name : 开发者
     * org_name : MDI装置
     * parent_org_name : 烟台工业园指挥部
     * transfer_enterprise_name : 宁波市北仑环保固废处置有限公司
     * dispose_enterprise_name : 宁波市北仑环保固废处置有限公司
     * car_code : 浙B7B956
     * card_id : 001
     * card_code : 001
     * card_name : 1#车卡
     * lock_code : LOCK-001
     */

    private String transfer_id;
    private String apply_code;
    private String phone;
    private long plan_transfer_time;
    private String duty_person;
    private long create_time;
    private String five_bills_code;
    private String user_name;
    private String org_name;
    private String parent_org_name;
    private String transfer_enterprise_name;
    private String dispose_enterprise_name;
    private String car_code;
    private String card_id;
    private String card_code;
    private String card_name;
    private String lock_code;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public long getPlan_transfer_time() {
        return plan_transfer_time;
    }

    public void setPlan_transfer_time(long plan_transfer_time) {
        this.plan_transfer_time = plan_transfer_time;
    }

    public String getDuty_person() {
        return duty_person;
    }

    public void setDuty_person(String duty_person) {
        this.duty_person = duty_person;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public String getFive_bills_code() {
        return five_bills_code;
    }

    public void setFive_bills_code(String five_bills_code) {
        this.five_bills_code = five_bills_code;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getOrg_name() {
        return org_name;
    }

    public void setOrg_name(String org_name) {
        this.org_name = org_name;
    }

    public String getParent_org_name() {
        return parent_org_name;
    }

    public void setParent_org_name(String parent_org_name) {
        this.parent_org_name = parent_org_name;
    }

    public String getTransfer_enterprise_name() {
        return transfer_enterprise_name;
    }

    public void setTransfer_enterprise_name(String transfer_enterprise_name) {
        this.transfer_enterprise_name = transfer_enterprise_name;
    }

    public String getDispose_enterprise_name() {
        return dispose_enterprise_name;
    }

    public void setDispose_enterprise_name(String dispose_enterprise_name) {
        this.dispose_enterprise_name = dispose_enterprise_name;
    }

    public String getCar_code() {
        return car_code;
    }

    public void setCar_code(String car_code) {
        this.car_code = car_code;
    }

    public String getCard_id() {
        return card_id;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
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

    public String getLock_code() {
        return lock_code;
    }

    public void setLock_code(String lock_code) {
        this.lock_code = lock_code;
    }
}
