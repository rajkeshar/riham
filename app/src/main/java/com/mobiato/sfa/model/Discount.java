package com.mobiato.sfa.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Discount implements Serializable {

    @SerializedName("discount_id")
    @Expose
    private String discountId;
    @SerializedName("item_id")
    @Expose
    private String itemId;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("discount_type")
    @Expose
    private String discountType;
    @SerializedName("discount")
    @Expose
    private String discount;
    @SerializedName("sales")
    @Expose
    private String sales;
    @SerializedName("qty")
    @Expose
    private String qty;
    @SerializedName("cust_id")
    @Expose
    private String customerId;
    @SerializedName("uom")
    @Expose
    private String uom;
    @SerializedName("category_id")
    @Expose
    private String categoryId;

    public String getDiscountId() {
        return discountId;
    }

    public void setDiscountId(String discountId) {
        this.discountId = discountId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getSales() {
        return sales;
    }

    public void setSales(String sales) {
        this.sales = sales;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
