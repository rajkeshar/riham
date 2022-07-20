package com.mobiato.sfa.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AddChiller implements Serializable {

    public String getDepot_id() {
        return depot_id;
    }

    public void setDepot_id(String depot_id) {
        this.depot_id = depot_id;
    }

    public String getSalesman_id() {
        return salesman_id;
    }

    public void setSalesman_id(String salesman_id) {
        this.salesman_id = salesman_id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getAsset_no() {
        return asset_no;
    }

    public void setAsset_no(String asset_no) {
        this.asset_no = asset_no;
    }

    @SerializedName("depot_id")
    @Expose
    private String depot_id;

    @SerializedName("salesman_id")
    @Expose
    private String salesman_id;

    @SerializedName("customer_id")
    @Expose
    private String customer_id;

    @SerializedName("asset_no")
    @Expose
    private String asset_no;

    public String getFridge_scan_img() {
        return fridge_scan_img;
    }

    public void setFridge_scan_img(String fridge_scan_img) {
        this.fridge_scan_img = fridge_scan_img;
    }

    @SerializedName("fridge_scan_img")
    @Expose
    private String fridge_scan_img;
}