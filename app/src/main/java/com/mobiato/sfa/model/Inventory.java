package com.mobiato.sfa.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;


public class Inventory implements Serializable {

    @SerializedName("inventory_id")
    @Expose
    private String inventoryId;
    @SerializedName("customer_id")
    @Expose
    private String customerId;
    @SerializedName("inventory_item")
    @Expose
    private ArrayList<Item> arrItem;

    public String getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(String inventoryId) {
        this.inventoryId = inventoryId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public ArrayList<Item> getArrItem() {
        return arrItem;
    }

    public void setArrItem(ArrayList<Item> arrItem) {
        this.arrItem = arrItem;
    }
}