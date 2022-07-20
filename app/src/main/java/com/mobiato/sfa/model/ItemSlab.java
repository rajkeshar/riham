package com.mobiato.sfa.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ItemSlab implements Serializable {


    @SerializedName("discount_amt")
    @Expose
    private String discount_amt;
    @SerializedName("qty_min")
    @Expose
    private String qty_min;
    @SerializedName("qty_max")
    @Expose
    private String qty_max;

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
