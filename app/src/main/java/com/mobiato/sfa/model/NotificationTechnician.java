package com.mobiato.sfa.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NotificationTechnician implements Serializable {

    @SerializedName("ir_code")
    @Expose
    private String ir_code;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("iro_id")
    @Expose
    private String iro_id;
    
    @SerializedName("salesman_id")
    @Expose
    private String salesman_id;
    
    @SerializedName("created_user")
    @Expose
    private String created_user;

    @SerializedName("schudule_date")
    @Expose
    private String schudule_date;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("created_date")
    @Expose
    private String created_date;

    @SerializedName("number_fridge")
    @Expose
    private String number_fridge;

    public String getIr_code() {
        return ir_code;
    }

    public void setIr_code(String ir_code) {
        this.ir_code = ir_code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIro_id() {
        return iro_id;
    }

    public void setIro_id(String iro_id) {
        this.iro_id = iro_id;
    }

    public String getSalesman_id() {
        return salesman_id;
    }

    public void setSalesman_id(String salesman_id) {
        this.salesman_id = salesman_id;
    }

    public String getCreated_user() {
        return created_user;
    }

    public void setCreated_user(String created_user) {
        this.created_user = created_user;
    }

    public String getSchudule_date() {
        return schudule_date;
    }

    public void setSchudule_date(String schudule_date) {
        this.schudule_date = schudule_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getNumber_fridge() {
        return number_fridge;
    }

    public void setNumber_fridge(String number_fridge) {
        this.number_fridge = number_fridge;
    }
}