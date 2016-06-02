package com.xyt.hwms.bean;

import java.util.List;

/**
 * Created by Levin on 2016-05-20.
 */
public class Transfer {

    /**
     * transfer_id : 01550a9495e58a8ae61a55063d660485
     * apply_code : 201606010001
     * apply_org_name : 装置（暂时没有）
     * car_code : //车号（暂时没有）
     * card_code : //车卡号（暂时没有）
     * card_name : //车卡名（暂时没有）
     * create_time : 1464762572120
     * detail_status : 0
     * operator : 张三
     * phone : 15000000000
     * duty_person : 刘广安
     * step : //工序（暂时没有）
     * lock : //电子锁好（暂时没有）
     * detail : [{"waste_name":"//固废名称","category_code":"//固废代码","is_key_waste":"//重点监管","container_label_code":"CON-0001","back_reason":"xxxx","back_reason_index":1,"status":"a","label_code":"Y-HW06-MDI-20160527-0014","produce_source":"装置检修"}]
     */

    private String transfer_id;
    private String apply_code;
    private String apply_org_name;
    private String car_code;
    private String card_code;
    private String card_name;
    private long create_time;
    private String detail_status;
    private String operator;
    private String transfer_type;
    private String phone;
    private String duty_person;
    private String step;
    private String lock;
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

    public String getApply_org_name() {
        return apply_org_name;
    }

    public void setApply_org_name(String apply_org_name) {
        this.apply_org_name = apply_org_name;
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

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getLock() {
        return lock;
    }

    public void setLock(String lock) {
        this.lock = lock;
    }

    public List<TransferDetail> getDetail() {
        return detail;
    }

    public void setDetail(List<TransferDetail> detail) {
        this.detail = detail;
    }
}
