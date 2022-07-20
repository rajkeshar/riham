package com.mobiato.sfa.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DiscountSlab implements Serializable {

    @SerializedName("discount_id")
    @Expose
    private String discountId;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("item_id")
    @Expose
    private String itemId;
    @SerializedName("discount_type")
    @Expose
    private String discountType;
    @SerializedName("discount_on")
    @Expose
    private String discountOn;
    @SerializedName("discount_amt")
    @Expose
    private String discount_amt;
    @SerializedName("qty_min")
    @Expose
    private String qty_min;
    @SerializedName("qty_max")
    @Expose
    private String qty_max;

    public String getDiscountId() {
        return discountId;
    }

    public void setDiscountId(String discountId) {
        this.discountId = discountId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public String getDiscountOn() {
        return discountOn;
    }

    public void setDiscountOn(String discountOn) {
        this.discountOn = discountOn;
    }

    public String getDiscount_amt() {
        return discount_amt;
    }

    public void setDiscount_amt(String discount_amt) {
        this.discount_amt = discount_amt;
    }

    public String getQty_min() {
        return qty_min;
    }

    public void setQty_min(String qty_min) {
        this.qty_min = qty_min;
    }

    public String getQty_max() {
        return qty_max;
    }

    public void setQty_max(String qty_max) {
        this.qty_max = qty_max;
    }
}
