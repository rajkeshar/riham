package com.mobiato.sfa.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;


public class OrderData implements Serializable {

    @SerializedName("order_id")
    @Expose
    public String orderId;
    @SerializedName("orderNo")
    @Expose
    public String orderNo;
    @SerializedName("customerid")
    @Expose
    public String customerId;
    @SerializedName("gross_total")
    @Expose
    public String grossTotal;
    @SerializedName("vat")
    @Expose
    public String vatAmt;
    @SerializedName("pre_vat")
    @Expose
    public String preVatAmt;
    @SerializedName("excise")
    @Expose
    public String excise;
    @SerializedName("net_total")
    @Expose
    public String netTotal;
    @SerializedName("total_amount")
    @Expose
    public String totalAmt;
    @SerializedName("deliverytems")
    @Expose
    public ArrayList<Item> deliveryItems;


    public OrderData() {
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getGrossTotal() {
        return grossTotal;
    }

    public void setGrossTotal(String grossTotal) {
        this.grossTotal = grossTotal;
    }

    public String getVatAmt() {
        return vatAmt;
    }

    public void setVatAmt(String vatAmt) {
        this.vatAmt = vatAmt;
    }

    public String getPreVatAmt() {
        return preVatAmt;
    }

    public void setPreVatAmt(String preVatAmt) {
        this.preVatAmt = preVatAmt;
    }

    public String getExcise() {
        return excise;
    }

    public void setExcise(String excise) {
        this.excise = excise;
    }

    public String getNetTotal() {
        return netTotal;
    }

    public void setNetTotal(String netTotal) {
        this.netTotal = netTotal;
    }

    public String getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(String totalAmt) {
        this.totalAmt = totalAmt;
    }

    public ArrayList<Item> getDeliveryItems() {
        return deliveryItems;
    }

    public void setDeliveryItems(ArrayList<Item> deliveryItems) {
        this.deliveryItems = deliveryItems;
    }
}