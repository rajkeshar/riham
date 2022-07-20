package com.mobiato.sfa.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Pricing implements Serializable {

    @SerializedName("pricingplan_id")
    @Expose
    public String planId;
    @SerializedName("pricingplan_name")
    @Expose
    public String planName;
    @SerializedName("customers_id")
    @Expose
    public String customerId;
    @SerializedName("total_records")
    @Expose
    public String totalRecords;
    @SerializedName("pricingitem")
    @Expose
    public ArrayList<Item> itemList;


    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public ArrayList<Item> getItemList() {
        return itemList;
    }

    public void setItemList(ArrayList<Item> itemList) {
        this.itemList = itemList;
    }

    public String getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(String totalRecords) {
        this.totalRecords = totalRecords;
    }
}
